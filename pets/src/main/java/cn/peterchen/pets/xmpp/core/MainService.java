package cn.peterchen.pets.xmpp.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import org.jivesoftware.smackx.ping.PingManager;

import cn.peterchen.pets.R;
import cn.peterchen.pets.ui.main.MainActivity;
import cn.peterchen.pets.xmpp.receivers.NetworkConnectivityReceiver;
import cn.peterchen.pets.xmpp.receivers.PublicIntentReceiver;
import cn.peterchen.pets.xmpp.receivers.StorageLowReceiver;
import cn.peterchen.pets.xmpp.tool.CrashedStartCounter;
import cn.peterchen.pets.xmpp.tool.DisplayToast;
import cn.peterchen.pets.xmpp.tool.Log;
import cn.peterchen.pets.xmpp.tool.Tools;


public class MainService extends Service {
    private static MainService sIntance = null;

    private final static int NOTIFICATION_CONNECTION = 1;
    private final static int NOTIFICATION_STOP_RINGING = 2;

    // The following actions are documented and registered in our manifest
    public final static String ACTION_CONNECT = "cn.peterchen.pets.action.CONNECT";
    public final static String ACTION_DISCONNECT = "cn.peterchen.pets.action.DISCONNECT";
    public final static String ACTION_TOGGLE = "cn.peterchen.pets.action.TOGGLE";
    public final static String ACTION_SEND = "cn.peterchen.pets.action.SEND";

    // The following actions are undocumented and internal to our
    // implementation.
    public final static String ACTION_BROADCAST_STATUS = "cn.peterchen.pets.action.BROADCAST_STATUS";
    public final static String ACTION_NETWORK_STATUS_CHANGED = "cn.peterchen.pets.action.NETWORK_STATUS_CHANGED";

    // A list of intent actions that the XmppManager broadcasts.
    public static final String ACTION_XMPP_MESSAGE_RECEIVED = "cn.peterchen.pets.action.XMPP.MESSAGE_RECEIVED";
    public static final String ACTION_XMPP_PRESENCE_CHANGED = "cn.peterchen.pets.action.XMPP.PRESENCE_CHANGED";
    public static final String ACTION_XMPP_CONNECTION_CHANGED = "cn.peterchen.pets.action.XMPP.CONNECTION_CHANGED";

    public static final String SERVICE_THREAD_NAME = Tools.APP_NAME
            + ".Service";

    private static final int STATUS_ICON_GREEN = 0;
    private static final int STATUS_ICON_ORANGE = 1;
    private static final int STATUS_ICON_RED = 2;
    private static final int STATUS_ICON_BLUE = 3;

    // A bit of a hack to allow global receivers to know whether or not
    // the service is running, and therefore whether to tell the service
    // about some events
    public static boolean IsRunning = false;

    private static boolean sListenersActive = false;

    private static SettingsManager sSettingsMgr;
    private static NotificationManager sNotificationManager;
    private static XmppManager sXmppMgr;
    private static BroadcastReceiver sXmppConChangedReceiver;
    private static BroadcastReceiver sStorageLowReceiver;
    private static PowerManager sPm;
    private static PowerManager.WakeLock sWl;
    private static PendingIntent sPendingIntentLaunchApplication = null;

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    private long mHandlerThreadId;

    private static Context sUiContext;

    private static volatile Handler sToastHandler = new Handler();
    private static Handler sDelayedDisconnectHandler;

    // some stuff for the async service implementation - borrowed heavily from
    // the standard IntentService, but that class doesn't offer fine enough
    // control for "foreground" services.
    private static volatile Looper sServiceLooper;
    private static volatile ServiceHandler sServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // ensure XMPP manager is setup (but not yet connected)
            if (sXmppMgr == null) {
                setupXmppManagerAndCommands();
            }

            Intent intent = (Intent) msg.obj;
            String action = intent.getAction();
            int id = msg.arg1;

            if (action.equals(ACTION_CONNECT)
                    || action.equals(ACTION_DISCONNECT)
                    || action.equals(ACTION_TOGGLE)
                    || action.equals(ACTION_NETWORK_STATUS_CHANGED)) {
                onHandleIntentTransportConnection(intent);
            } else if (!action.equals(ACTION_XMPP_CONNECTION_CHANGED)) {
                onHandleIntentMessage(intent);
            }

            // stop the service if we are disconnected (but stopping the service
            // doesn't mean the process is terminated - onStart can still
            // happen.)
            if (getConnectionStatus() == XmppManager.DISCONNECTED) {
                if (stopSelfResult(id)) {
                    Log.i("service is stopping because we are disconnected and no pending intents exist");
                } else {
                    Log.i("we are disconnected, but more pending intents to be delivered - service will not stop");
                }
            }
        }
    }

    static Thread sThread = null;
    static boolean sIsConnecting = false;
    static boolean sIsDisconnecting = false;

    void connectTransport() {
        sXmppMgr.xmppRequestStateChange(XmppManager.CONNECTED);
    }

    void disconnectTransport() {
        sXmppMgr.xmppRequestStateChange(XmppManager.DISCONNECTED);
    }

    void onHandleIntentTransportConnection(final Intent intent) {
        // Set Disconnected state by force to manage pending tasks
        // This is not actively used any more
        if (intent.getBooleanExtra("force", false)
                && intent.getBooleanExtra("disconnect", false)) {
            disconnectTransport();
        }

        if (Thread.currentThread().getId() != mHandlerThreadId) {
            throw new IllegalThreadStateException();
        }

        String action = intent.getAction();
        int initialState = getConnectionStatus();
        Log.i("handling action '" + action + "' while in state "
                + XmppManager.statusAsString(initialState));

        // Start with handling the actions the could result in a change
        // of the connection status
        if (action.equals(ACTION_CONNECT)) {
            if (intent.getBooleanExtra("disconnect", false)) {
                disconnectTransport();
            } else {
                connectTransport();
            }
        } else if (action.equals(ACTION_DISCONNECT)) {
            disconnectTransport();
        } else if (action.equals(ACTION_TOGGLE)) {
            switch (initialState) {
                case XmppManager.CONNECTED:
                case XmppManager.CONNECTING:
                case XmppManager.WAITING_TO_CONNECT:
                case XmppManager.WAITING_FOR_NETWORK:
                    disconnectTransport();
                    break;
                case XmppManager.DISCONNECTED:
                case XmppManager.DISCONNECTING:
                    connectTransport();
                    break;
                default:
                    throw new IllegalStateException(
                            "Unknown initialState while handling"
                                    + MainService.ACTION_TOGGLE);
            }
        } else if (action.equals(ACTION_NETWORK_STATUS_CHANGED)) {
            boolean networkChanged = intent.getBooleanExtra("networkChanged",
                    false);
            boolean connectedOrConnecting = intent.getBooleanExtra(
                    "connectedOrConnecting", true);
            boolean connected = intent.getBooleanExtra("connected", true);
            Log.i("NETWORK_CHANGED networkChanged=" + networkChanged
                    + " connected=" + connected + " connectedOrConnecting="
                    + connectedOrConnecting + " state="
                    + XmppManager.statusAsString(initialState));

            if (!connectedOrConnecting
                    && (initialState == XmppManager.CONNECTED || initialState == XmppManager.CONNECTING)) {
                // We are connected but the network has gone down - disconnect
                // and go into WAITING state so we auto-connect when we get a
                // future
                // notification that a network is available.
                sXmppMgr.xmppRequestStateChange(XmppManager.WAITING_FOR_NETWORK);
            } else if (connected
                    && (initialState == XmppManager.WAITING_TO_CONNECT || initialState == XmppManager.WAITING_FOR_NETWORK)) {
                sXmppMgr.xmppRequestStateChange(XmppManager.CONNECTED);
            } else if (networkChanged && initialState == XmppManager.CONNECTED) {
                // The network has changed (WiFi <-> GSM switch) and we are
                // connected, reconnect now
                disconnectTransport();
                connectTransport();
            }
        } else {
            Log.w("Unexpected intent: " + action);
        }

        // Now that the connection state may has changed either because of a
        // Intent Action or because of connection changes that happened
        // "externally"
        // (eg, due to a connection error, or running out of retries, or a retry
        // handler actually succeeding etc.) we may need to update the listener
        // TODO issue with asynch connection
        updateListenersToCurrentState(getConnectionStatus());
    }

    void onHandleIntentMessage(final Intent intent) {
        String action = intent.getAction();
        int initialState = getConnectionStatus();
        Log.i("handling action '" + action + "' while in state "
                + XmppManager.statusAsString(initialState));

        if (action.equals(ACTION_SEND)) {
            XmppMsg xmppMsg = intent.getParcelableExtra("xmppMsg");
            if (xmppMsg == null) {
                xmppMsg = new XmppMsg(intent.getStringExtra("message"));
            }
            sXmppMgr.send(xmppMsg, intent.getStringExtra("to"));
        } else if (action.equals(ACTION_XMPP_MESSAGE_RECEIVED)) {
            maybeAcquireWakeLock();
            String message = intent.getStringExtra("message");
            if (message != null) {
                XmppManager.broadcastReceivedMsg(this, message);
            }
            sWl.release();
        } else {
            Log.w("Unexpected intent: " + action);
        }
        Log.i("handled action '" + action + "' - state now: "
                + sXmppMgr.statusString());
    }

    public int getConnectionStatus() {
        return sXmppMgr == null ? XmppManager.DISCONNECTED : sXmppMgr
                .getConnectionStatus();
    }

    public String getConnectionStatusAction() {
        return sXmppMgr == null ? "" : sXmppMgr.getConnectionStatusAction();
    }

    public boolean getTLSStatus() {
        // null check necessary
        return sXmppMgr != null && sXmppMgr.getTLSStatus();
    }

    public boolean getCompressionStatus() {
        // null check necessary
        return sXmppMgr != null && sXmppMgr.getCompressionStatus();
    }

    public PingManager getPingManager() {
        return sXmppMgr == null ? null : sXmppMgr.getPingManger();
    }

    public void updateBuddies() {
        if (sXmppMgr != null) {
            XmppBuddies.getInstance(this).retrieveFriendList();
        }
    }

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sIntance = this;

        NetworkConnectivityReceiver.setLastActiveNetworkName(this);

        sPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sWl = sPm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Tools.APP_NAME
                + " WakeLock");

        sSettingsMgr = SettingsManager.getSettingsManager(this);
        Log.initialize(sSettingsMgr);
        Tools.setLocale(sSettingsMgr, this);

        // Start a new thread for the service
        HandlerThread thread = new HandlerThread(SERVICE_THREAD_NAME);
        thread.start();
        mHandlerThreadId = thread.getId();
        sServiceLooper = thread.getLooper();
        sServiceHandler = new ServiceHandler(sServiceLooper);
        sDelayedDisconnectHandler = new Handler(sServiceLooper);

        sUiContext = this;

        sPendingIntentLaunchApplication = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        sNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.i("onCreate(): service thread created - IsRunning is set to true");
        IsRunning = true;

        // it seems that with gingerbread android doesn't issue null intents any
        // more when restarting a service but only calls the service's
        // onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            int lastStatus = XmppStatus.getInstance(this).getLastKnowState();
            int currentStatus = (sXmppMgr == null) ? XmppManager.DISCONNECTED
                    : sXmppMgr.getConnectionStatus();
            if (lastStatus != currentStatus
                    && lastStatus != XmppManager.DISCONNECTING) {
                Log.i("onCreate(): issuing connect intent because we are on gingerbread (or higher). "
                        + "lastStatus is "
                        + lastStatus
                        + " and currentStatus is " + currentStatus);
                startService(new Intent(MainService.ACTION_CONNECT));
                CrashedStartCounter.getInstance(this).count();
            }
        }

        PublicIntentReceiver.initReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            // The application has been killed by Android and
            // we try to restart the connection
            // this null intent behavior is only for SDK < 9
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                CrashedStartCounter.getInstance(this).count();
                startService(new Intent(MainService.ACTION_CONNECT));
            } else {
                Log.w("onStartCommand() null intent with Gingerbread or higher");
            }
            return START_STICKY;
        }
        Log.i("onStartCommand(): Intent " + intent.getAction());
        // A special case for the 'broadcast status' intent - we avoid setting
        // up the _xmppMgr etc
        if (intent.getAction().equals(ACTION_BROADCAST_STATUS)) {
            // A request to broadcast our current status even if _xmpp is null.
            int state = getConnectionStatus();
            XmppManager.broadcastStatus(this, state, state,
                    getConnectionStatusAction());
            // A real action request
        } else {
            // redirect the intent to the service handler thread
            sendToServiceHandler(startId, intent);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("MainService onDestroy(): IsRunning is set to false");
        PublicIntentReceiver.onServiceStop();
        IsRunning = false;
        // If the _xmppManager is non-null, then our service was "started" (as
        // opposed to simply "created" - so tell the user it has stopped.
        if (sXmppMgr != null) {
            // do some cleanup
            unregisterReceiver(sXmppConChangedReceiver);
            sXmppConChangedReceiver = null;

            unregisterReceiver(sStorageLowReceiver);
            sStorageLowReceiver = null;

            sXmppMgr.xmppRequestStateChange(XmppManager.DISCONNECTED);
            sXmppMgr.mSmackAndroid.onDestroy();
            sXmppMgr = null;
        }

        // All data must be cleaned, because onDestroy can be call without
        // releasing the current object
        // It's due to BIND_AUTO_CREATE used for Service Binder
        // http://developer.android.com/reference/android/content/Context.html#stopService(android.content.Intent)

        sServiceLooper.quit();
        sIntance = null;

        super.onDestroy();
        Log.i("MainService onDestroy(): service destroyed");
    }

    /**
     * Wrapper for send(XmppMsg msg... method
     *
     * @param msg
     * @param to  The receiving JID, if null the default notification address is
     *            used
     */
    public void send(String msg, String to) {
        send(new XmppMsg(msg), to);
    }

    /**
     * Sends an XmppMsg to the specified JID or to the default notification
     * address
     *
     * @param msg
     * @param to  - the receiving jid. if null the default notification address
     *            is used
     */
    public void send(XmppMsg msg, String to) {
        if (sXmppMgr != null) {
            sXmppMgr.send(msg, to);
        } else {
            Log.w("MainService send XmppMsg: _xmppMgr == null");
        }
    }

    /**
     * Provides a clean way to display toast messages that don't get stuck
     *
     * @param text       The Text to show as toast
     * @param extraInfo  can be null
     * @param showPrefix show the app name as prefix to the toast message
     */
    public static void displayToast(String text, String extraInfo,
                                    boolean showPrefix) {
        sToastHandler.post(new DisplayToast(text, extraInfo, sUiContext,
                showPrefix));
    }

    /**
     * Display a string resource i as toast messages
     *
     * @param i         The resource ID of the string to show as toast
     * @param extraInfo can be null
     */
    public static void displayToast(int i, String extraInfo) {
        displayToast(sUiContext.getString(i), extraInfo, true);
    }

    /**
     * Does an initial one-time setup on the MainService by - Creating a
     * XmppManager Instance - Registering the commands - Registering a Listener
     * for ACTION_XMPP_CONNECTION_CHANGED which is issued by XmppManager for
     * every state change of the XMPP connection
     */
    private void setupXmppManagerAndCommands() {
        sXmppConChangedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                intent.setClass(MainService.this, MainService.class);
                onConnectionStatusChanged(intent.getIntExtra("old_state", 0),
                        intent.getIntExtra("new_state", 0));
                startService(intent);
            }
        };
        IntentFilter intentFilter = new IntentFilter(
                ACTION_XMPP_CONNECTION_CHANGED);
        registerReceiver(sXmppConChangedReceiver, intentFilter);

        sStorageLowReceiver = new StorageLowReceiver();
        intentFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
        registerReceiver(sStorageLowReceiver, intentFilter);

        sXmppMgr = XmppManager.getInstance(this);
    }

    private int getImageStatus(int color) {
        String index = sSettingsMgr.displayIconIndex;
        int res = 0;
        try {
            switch (color) {
                case STATUS_ICON_GREEN:
                    res = R.drawable.class.getField("status_green_" + index)
                            .getInt(null);
                    break;
                case STATUS_ICON_ORANGE:
                    res = R.drawable.class.getField("status_orange_" + index)
                            .getInt(null);
                    break;
                case STATUS_ICON_RED:
                    res = R.drawable.class.getField("status_red_" + index).getInt(
                            null);
                    break;
                case STATUS_ICON_BLUE:
                    res = R.drawable.class.getField("status_blue_" + index).getInt(
                            null);
                    break;
            }
        } catch (Exception e) {
            Log.e("Failed to retrieve Image Status: color=" + color
                    + ", index=" + index + ". Ex: ", e);
        }

        return res;
    }

    /**
     * Hides the stop ringing notification
     */
    public void hideRingingNotification() {
        sNotificationManager.cancel(NOTIFICATION_STOP_RINGING);
    }

    /**
     * Updates the status about the service state (and the status bar)
     */
    private void onConnectionStatusChanged(int oldStatus, int status) {
        if (sSettingsMgr.showStatusIcon) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    this);
            builder.setWhen(System.currentTimeMillis());

            switch (status) {
                case XmppManager.CONNECTED:
                    builder.setContentText(getString(
                            R.string.main_service_connected,
                            getConnectionStatusAction()));
                    builder.setSmallIcon(getImageStatus(STATUS_ICON_GREEN));
                    break;
                case XmppManager.CONNECTING:
                    builder.setContentText(getString(R.string.main_service_connecting));
                    builder.setSmallIcon(getImageStatus(STATUS_ICON_ORANGE));
                    break;
                case XmppManager.DISCONNECTED:
                    builder.setContentText(getString(R.string.main_service_disconnected));
                    builder.setSmallIcon(getImageStatus(STATUS_ICON_RED));
                    break;
                case XmppManager.DISCONNECTING:
                    builder.setContentText(getString(R.string.main_service_disconnecting));
                    builder.setSmallIcon(getImageStatus(STATUS_ICON_ORANGE));
                    break;
                case XmppManager.WAITING_TO_CONNECT:
                case XmppManager.WAITING_FOR_NETWORK:
                    builder.setContentText(getString(R.string.main_service_waiting_to_connect)
                            + "\n" + getConnectionStatusAction());
                    builder.setSmallIcon(getImageStatus(STATUS_ICON_BLUE));
                    break;
                default:
                    return;
            }
            builder.setContentIntent(sPendingIntentLaunchApplication);
            builder.setContentTitle(Tools.APP_NAME);

            startForeground(NOTIFICATION_CONNECTION, builder.build());
        }
    }

    private int updateListenersToCurrentState(int currentState) {
        boolean wantListeners;
        switch (currentState) {
            case XmppManager.CONNECTED:
            case XmppManager.CONNECTING:
            case XmppManager.DISCONNECTING:
            case XmppManager.WAITING_TO_CONNECT:
            case XmppManager.WAITING_FOR_NETWORK:
                wantListeners = true;
                break;
            case XmppManager.DISCONNECTED:
                wantListeners = false;
                break;
            default:
                throw new IllegalStateException(
                        "updateListeners found invalid  int: " + currentState);
        }

        if (wantListeners && !sListenersActive) {
            Log.i("setupListenersForConnection()");
            sListenersActive = true;
        } else if (!wantListeners) {
            Log.i("tearDownListenersForConnection()");
            sListenersActive = false;
        }

        return currentState;
    }

    public static Looper getServiceLooper() {
        return sServiceLooper;
    }

    public static Handler getDelayedDisconnectHandler() {
        return sDelayedDisconnectHandler;
    }

    public static boolean sendToServiceHandler(int i, Intent intent) {
        if (sServiceHandler != null) {
            Message msg = sServiceHandler.obtainMessage();
            msg.arg1 = i;
            msg.obj = intent;
            sServiceHandler.sendMessage(msg);
            return true;
        } else {
            Log.w("sendToServiceHandler() called with " + intent.getAction()
                    + " when service handler is null");
            return false;
        }
    }

    public static boolean sendToServiceHandler(Intent intent) {
        return sendToServiceHandler(0, intent);
    }

    public static void maybeAcquireWakeLock() {
        if (!sWl.isHeld()) {
            sWl.acquire();
        }
    }
}
