package cn.peterchen.imtest.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.peterchen.imtest.R;
import cn.peterchen.imtest.model.User;
import cn.peterchen.imtest.xmpp.core.MainService;
import cn.peterchen.imtest.xmpp.core.XmppMsg;
import cn.peterchen.imtest.xmpp.tool.Tools;


public class ChatActivity extends Activity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        int position = getIntent().getIntExtra("user", 0);
        if (position == 0) {
            friend = new User("boy2", "123456");
        } else {
            friend = new User("test2", "123456");
        }
    }

    private void initViews() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageEditText = (EditText) findViewById(R.id.messageEdit);
        Button sendButton = (Button) findViewById(R.id.chatSendButton);
        TextView meLabel = (TextView) findViewById(R.id.meLabel);
        TextView companionLabel = (TextView) findViewById(R.id.companionLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        adapter = new ChatAdapter(this, new ArrayList<XmppMsg>());
        messagesContainer.setAdapter(adapter);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("EXTRA_USER_ID", 0);
        companionLabel.setText("user(id" + userId + ")");
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
                        ChatActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(msgReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainService.ACTION_XMPP_MESSAGE_RECEIVED);
        this.registerReceiver(msgReceiver, filter);
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
}
