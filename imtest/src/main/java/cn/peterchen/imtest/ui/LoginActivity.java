package cn.peterchen.imtest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.peterchen.imtest.R;
import cn.peterchen.imtest.xmpp.core.MainService;
import cn.peterchen.imtest.xmpp.core.SettingsManager;
import cn.peterchen.imtest.xmpp.core.XmppManager;
import cn.peterchen.imtest.xmpp.tool.Tools;


public class LoginActivity extends ActionBarActivity {

	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginBtn;

	private SettingsManager mSettingsMgr;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		mSettingsMgr = SettingsManager.getSettingsManager(this);

		usernameEdit = (EditText) findViewById(R.id.username);
		passwordEdit = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.login);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				login();
			}
		});
	}

	@Override
	protected void onPause() {
		unregisterReceiver(mXmppreceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MainService.ACTION_XMPP_CONNECTION_CHANGED);
		this.registerReceiver(mXmppreceiver, filter);
		super.onResume();
	}

	protected void updateStatus(int newStatus, String currentStatus) {
		if (newStatus == XmppManager.CONNECTED) {
			startActivity(new Intent(this, MainActivity.class));
		} else {
			Toast.makeText(this, currentStatus, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void login() {
		mSettingsMgr.setLogin(usernameEdit.getText().toString());
		mSettingsMgr.setPassword(passwordEdit.getText().toString());
		Tools.startSvcIntent(this, MainService.ACTION_CONNECT);

	}

}
