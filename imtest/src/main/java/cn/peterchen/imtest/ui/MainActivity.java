package cn.peterchen.imtest.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.ConnectionListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import cn.peterchen.imtest.R;


public class MainActivity extends ActionBarActivity {

	private static final int AUTHENTICATION_REQUEST = 1;
	private ConnectionListener connectionListener;
	private ListView usersList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_users);

		usersList = (ListView) findViewById(R.id.usersList);
		usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,ChatActivity.class);
				intent.putExtra("user", position);
				startActivity(intent);
			}
		});
		// Prepare users list for simple adapter.
		ArrayList<Map<String, String>> usersListForAdapter = new ArrayList<Map<String, String>>();
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("user", "test1");
		usersListForAdapter.add(userMap);
		userMap = new HashMap<String, String>();
		userMap.put("user", "test2");
		usersListForAdapter.add(userMap);

		// Put users list into adapter.
		SimpleAdapter usersAdapter = new SimpleAdapter(this,
				usersListForAdapter, R.layout.list_item_user,
				new String[] { "user" }, new int[] { R.id.userLogin });

		usersList.setAdapter(usersAdapter);

	}

	public void showAuthenticateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Authorize first");
		builder.setItems(new String[] { "Login", "Register" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(MainActivity.this,
									LoginActivity.class);
							startActivityForResult(intent,
									AUTHENTICATION_REQUEST);
							break;
						case 1:
							intent = new Intent(MainActivity.this,
									RegistrationActivity.class);
							startActivityForResult(intent,
									AUTHENTICATION_REQUEST);
							break;
						}
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		builder.show();
	}

	private void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		});
	}
}
