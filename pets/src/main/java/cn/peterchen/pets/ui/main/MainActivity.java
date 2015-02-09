package cn.peterchen.pets.ui.main;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ThreadPoolExecutor;

import cn.peterchen.pets.R;
import cn.peterchen.pets.global.Constant;
import cn.peterchen.pets.xmpp.core.SettingsManager;


public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener {

    private MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initXmppSettings();

        Bundle data = new Bundle();
        data.putInt("viewType", Constant.MASTER_VIEW);
        mainFragment = MainFragment.newInstance(data);
        getFragmentManager().beginTransaction().add(R.id.main_fragment_container, mainFragment).commit();

    }

    private void initXmppSettings() {
        Runnable initSettingRunnable = new Runnable() {
            @Override
            public void run() {
                SettingsManager.getSettingsManager(MainActivity.this);
            }
        };
        new Thread(initSettingRunnable).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * interacts with mainfragment
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
