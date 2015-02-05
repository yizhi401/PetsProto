package cn.peterchen.pets.ui.function;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.peterchen.pets.R;
import cn.peterchen.pets.xmpp.core.MainService;
import cn.peterchen.pets.xmpp.core.SettingsManager;
import cn.peterchen.pets.xmpp.tool.Tools;

/**
 * Created by peter on 15-2-5.
 */
public class FunctionLoginView extends LinearLayout {

    private Context context;
    private FunctionFragment fragment;
    private Button loginBtn;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private SettingsManager mSettingsMgr;

    public FunctionLoginView(Context context, FunctionFragment fragment) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        init();
    }

    public FunctionLoginView(Context context) {
        super(context);
        init();
    }

    public FunctionLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FunctionLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mSettingsMgr = SettingsManager.getSettingsManager(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.function_login_view, this);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
    }

    private void login() {
        mSettingsMgr.setLogin(usernameEdit.getText().toString());
        mSettingsMgr.setPassword(passwordEdit.getText().toString());
        Tools.startSvcIntent(context, MainService.ACTION_CONNECT);

    }

}
