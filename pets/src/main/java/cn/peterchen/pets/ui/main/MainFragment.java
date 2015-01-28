package cn.peterchen.pets.ui.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.peterchen.pets.R;
import cn.peterchen.pets.global.Constant;
import cn.peterchen.pets.ui.chat.ChatFragment;
import cn.peterchen.pets.ui.function.FunctionFragment;
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

    private ViewGroup rootView;
    private Button statusBtn;
    private Button shopBtn;
    private Button storageBtn;
    private Button chatBtn;
    private Button searchBtn;
    private Button changeBtn;
    private TextView petNameText;
    private ImageView petImage;
    private TextView petStatusText;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button funcBtn;

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
            petImage.setImageResource(R.drawable.pets);
            btn1.setText("吃饭");
            btn2.setText("学习");
            btn3.setText("打工");
            changeBtn.setText("宠物");
        } else if (viewType == Constant.PET_VIEW) {
            shopBtn.setVisibility(View.GONE);
            storageBtn.setVisibility(View.GONE);
            petNameText.setText("咩咩");
            petImage.setImageResource(R.drawable.pets_me);
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
        petImage = (ImageView) rootView.findViewById(R.id.pets);
        btn1 = (Button) rootView.findViewById(R.id.btn1);
        btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn3 = (Button) rootView.findViewById(R.id.btn3);
        btn4 = (Button) rootView.findViewById(R.id.btn4);


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

}
