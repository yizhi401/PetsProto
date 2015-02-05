package cn.peterchen.pets.ui.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.peterchen.pets.R;
import cn.peterchen.pets.global.Constant;
import cn.peterchen.pets.ui.chat.ChatFragment;
import cn.peterchen.pets.ui.function.FunctionFragment;
import cn.peterchen.pets.ui.game.GameController;
import cn.peterchen.pets.ui.game.GameSurface;
import cn.peterchen.pets.ui.search.SearchFragment;
import cn.peterchen.pets.ui.shop.ShopFragment;
import cn.peterchen.pets.ui.status.StatusFragment;
import cn.peterchen.pets.ui.storage.StorageFragment;

/**
 * created by Peter
 */
public class MainFragment extends Fragment {


    private int viewType;

    private OnFragmentInteractionListener mListener;

    private GameSurface surface;
    private ViewGroup rootView;
    private Button statusBtn;
    private Button shopBtn;
    private Button storageBtn;
    private Button chatBtn;
    private Button searchBtn;
    private Button changeBtn;
    private TextView petNameText;
    private TextView petStatusText;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button funcBtn;


    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static final int SENSOR_SHAKE = 10;

    private SensorEventListener sensorEventListener = new MySensorEventListener();

    /**
     * 动作执行
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
//                    Toast.makeText(getActivity(), "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
//                    Log.i("mInfo", "检测到摇晃，执行操作！");
                    GameController.getInstance().setGameStatus(GameController.STATUS_MINI_GAME);
                    break;
            }
        }

    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(Bundle args) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewType = getArguments().getInt("viewType");
        }
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);
        initFragments(rootView);

        changeViewType();

        return rootView;
    }

    private void changeViewType() {
        if (viewType == Constant.MASTER_VIEW) {
            shopBtn.setVisibility(View.VISIBLE);
            storageBtn.setVisibility(View.VISIBLE);
            petNameText.setText("小萌");
            btn1.setText("吃饭");
            btn2.setText("学习");
            btn3.setText("打工");
            changeBtn.setText("宠物");
        } else if (viewType == Constant.PET_VIEW) {
            shopBtn.setVisibility(View.GONE);
            storageBtn.setVisibility(View.GONE);
            petNameText.setText("咩咩");
            btn1.setText("找吃的");
            btn2.setText("睡懒觉");
            btn3.setText("卖卖萌");
            changeBtn.setText("主人");
        }
    }

    /**
     * TODO 使用工厂模式优化这里的代码
     *
     * @param rootView
     */
    private void initFragments(ViewGroup rootView) {

        statusBtn = (Button) rootView.findViewById(R.id.status);
        statusBtn.setOnClickListener(getFragmentClickListener(StatusFragment.class, StatusFragment.TAG));
        shopBtn = (Button) rootView.findViewById(R.id.shop);
        shopBtn.setOnClickListener(getFragmentClickListener(ShopFragment.class, ShopFragment.TAG));
        chatBtn = (Button) rootView.findViewById(R.id.chat);
        chatBtn.setOnClickListener(getFragmentClickListener(ChatFragment.class, ChatFragment.TAG));
        storageBtn = (Button) rootView.findViewById(R.id.storage);
        storageBtn.setOnClickListener(getFragmentClickListener(StorageFragment.class, StorageFragment.TAG));
        searchBtn = (Button) rootView.findViewById(R.id.search);
        searchBtn.setOnClickListener(getFragmentClickListener(SearchFragment.class, SearchFragment.TAG));
        funcBtn = (Button) rootView.findViewById(R.id.function);
        funcBtn.setOnClickListener(getFragmentClickListener(FunctionFragment.class, FunctionFragment.TAG));
        changeBtn = (Button) rootView.findViewById(R.id.change);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType == Constant.MASTER_VIEW) {
                    viewType = Constant.PET_VIEW;
                } else {
                    viewType = Constant.MASTER_VIEW;
                }
                changeViewType();
            }
        });

        petNameText = (TextView) rootView.findViewById(R.id.name);
        petStatusText = (TextView) rootView.findViewById(R.id.status);
        btn1 = (Button) rootView.findViewById(R.id.btn1);
        btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn3 = (Button) rootView.findViewById(R.id.btn3);
        btn4 = (Button) rootView.findViewById(R.id.btn4);

        surface = (GameSurface) rootView.findViewById(R.id.game_surface);
        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("mInfo", "Action down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("mInfo", "Action Move ");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.i("mInfo", "Action Cancel");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("mInfo", "Action Up");
                        if (GameController.getInstance().getCommand() == GameController.COMMAND_NORMAL)
                            GameController.getInstance().setCommand(GameController.COMMAND_PRESSED);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private View.OnClickListener getFragmentClickListener(final Class<? extends DialogFragment> dialogFragmentClass, final String tag) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DialogFragment dialogFragment = dialogFragmentClass.newInstance();
                    dialogFragment.show(getActivity().getFragmentManager(), tag);
                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    class MySensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            int threShold = 14;
            if (Math.abs(x) > threShold || Math.abs(y) > threShold || Math.abs(z) > threShold) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


}
