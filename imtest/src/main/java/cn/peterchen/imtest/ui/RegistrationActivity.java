package cn.peterchen.imtest.ui;

import org.jivesoftware.smack.SmackAndroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.peterchen.imtest.R;
import cn.peterchen.imtest.model.User;


public class RegistrationActivity extends Activity implements
		View.OnClickListener {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private Button registerButton;
	private EditText loginEdit;
	private EditText passwordEdit;
	private ProgressDialog progressDialog;

	private String login;
	private String password;
	private User user;
	private SmackAndroid smackAndroid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		loginEdit = (EditText) findViewById(R.id.loginEdit);
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
		registerButton = (Button) findViewById(R.id.registerButton);
		registerButton.setOnClickListener(this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		login = loginEdit.getText().toString();
		password = passwordEdit.getText().toString();

		user = new User(login, password);

		progressDialog.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish();
	}

	public void onComplete() {
		// Login
	}

}
