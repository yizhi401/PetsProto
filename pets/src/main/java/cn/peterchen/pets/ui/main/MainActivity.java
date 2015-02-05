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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
