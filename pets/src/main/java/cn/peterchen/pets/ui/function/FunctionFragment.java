package cn.peterchen.pets.ui.function;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.peterchen.pets.R;
import cn.peterchen.pets.xmpp.core.MainService;
import cn.peterchen.pets.xmpp.core.XmppManager;

/**
 * Created by peter on 15-1-27.
 */
public class FunctionFragment extends DialogFragment {

    public static final String TAG = FunctionFragment.class.getName();
    public static final int VIEW_TYPE_MAIN = 0;
    public static final int VIEW_TYPE_LOGIN = 1;

    private int viewType;

    private ViewGroup rootView;
    private ViewGroup mainView;
    private ViewGroup loginView;

    private final BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MainService.ACTION_XMPP_CONNECTION_CHANGED)) {
                updateStatus(intent.getIntExtra("new_state", 0),
                        intent.getStringExtra("current_action"));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.function_fragment, container, false);
        mainView = new FunctionMainView(getActivity(), this);
        loginView = new FunctionLoginView(getActivity(), this);
        rootView.addView(mainView);
        return rootView;
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mXmppreceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainService.ACTION_XMPP_CONNECTION_CHANGED);
        getActivity().registerReceiver(mXmppreceiver, filter);
        super.onResume();
    }

    protected void updateStatus(int newStatus, String currentStatus) {
        if (newStatus == XmppManager.CONNECTED) {
//            startActivity(new Intent(getActivity(), MainActivity.class));
//            changeViewType(VIEW_TYPE_MAIN);
            Toast.makeText(getActivity(), "Login Succeed", Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getActivity(), currentStatus, Toast.LENGTH_SHORT).show();
        }
    }


    public void changeViewType(int viewType) {
        this.viewType = viewType;
        rootView.removeAllViews();
        switch (viewType) {
            case VIEW_TYPE_MAIN:
                rootView.addView(mainView);
                break;
            case VIEW_TYPE_LOGIN:
                rootView.addView(loginView);
                break;
            default:
                break;
        }
    }

}
