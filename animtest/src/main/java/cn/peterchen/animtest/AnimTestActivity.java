package cn.peterchen.animtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by peter on 15-1-28.
 */
public class AnimTestActivity extends Activity {

    Button pressMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim_activity);


        pressMe = (Button) findViewById(R.id.popup);
        pressMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog("图哈特和", "哈哈哈");
            }
        });
    }

    public void popupDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_launcher_small);
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.create().show();

    }

}
