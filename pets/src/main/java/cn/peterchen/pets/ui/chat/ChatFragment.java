package cn.peterchen.pets.ui.chat;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.peterchen.pets.R;
import cn.peterchen.pets.entity.User;
import cn.peterchen.pets.xmpp.core.MainService;
import cn.peterchen.pets.xmpp.core.XmppMsg;
import cn.peterchen.pets.xmpp.tool.Tools;

/**
 * Created by peter on 15-1-27.
 */
public class ChatFragment extends DialogFragment {

    public static final String EXTRA_MODE = "mode";
    private EditText messageEditText;
    private ChatAdapter adapter;
    private ListView messagesContainer;
    private User friend;

    private final BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MainService.ACTION_XMPP_MESSAGE_RECEIVED)) {
                addMsgReceived(intent.getStringExtra("message"));
            }
        }
    };

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    private void initViews(ViewGroup rootView) {
        messagesContainer = (ListView) rootView.findViewById(R.id.messagesContainer);
        messageEditText = (EditText) rootView.findViewById(R.id.messageEdit);
        Button sendButton = (Button) rootView.findViewById(R.id.chatSendButton);
        TextView meLabel = (TextView) rootView.findViewById(R.id.meLabel);
        TextView companionLabel = (TextView) rootView.findViewById(R.id.companionLabel);
        RelativeLayout container = (RelativeLayout) rootView.findViewById(R.id.container);

        adapter = new ChatAdapter(this, new ArrayList<XmppMsg>());
        messagesContainer.setAdapter(adapter);

//        Intent intent = getIntent();
//        int userId = intent.getIntExtra("EXTRA_USER_ID", 0);
        int userId = 0;
        companionLabel.setText("user(id" + "test" + ")");
        restoreMessagesFromHistory(userId);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastMsg = messageEditText.getText().toString();
                if (TextUtils.isEmpty(lastMsg)) {
                    return;
                }

                messageEditText.setText("");
                // TODO send message
                XmppMsg msg = new XmppMsg(lastMsg);
                msg.setIncoming(false);
                showMessage(msg);
                Tools.send(lastMsg, friend.username + "@182.92.74.54",
                        getActivity());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(msgReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainService.ACTION_XMPP_MESSAGE_RECEIVED);
//        this.registerReceiver(msgReceiver, filter);
    }

    public void showMessage(XmppMsg message) {
        saveMessageToHistory(message);
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scrollDown();
    }

    protected void addMsgReceived(String message) {
        XmppMsg xmppMsg = new XmppMsg(message);
        xmppMsg.setIncoming(true);
        adapter.add(xmppMsg);
        adapter.notifyDataSetChanged();
        scrollDown();
    }

    public void showMessage(List<XmppMsg> messages) {
        adapter.add(messages);
        adapter.notifyDataSetChanged();
        scrollDown();
    }

    private void saveMessageToHistory(XmppMsg message) {
        // if (mode == Mode.SINGLE) {
        // ((App)getApplication()).addMessage(getIntent().getIntExtra(SingleChat.EXTRA_USER_ID,
        // 0), message);
        // }
    }

    private void restoreMessagesFromHistory(int userId) {
        // List<XmppMsg> messages = ((App)getApplication()).getMessages(userId);
        // if (messages != null) {
        // showMessage(messages);
        // }
    }

    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    public static final String TAG = ChatFragment.class.getName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog);
        friend = new User("boy2", "boy2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.chat_fragment, container, false);

        initViews(rootView);
        return rootView;
    }
}
