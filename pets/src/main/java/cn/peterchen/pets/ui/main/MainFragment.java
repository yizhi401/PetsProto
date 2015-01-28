package cn.peterchen.pets.ui.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.peterchen.pets.R;
import cn.peterchen.pets.ui.chat.ChatFragment;
import cn.peterchen.pets.ui.message.SearchFragment;
import cn.peterchen.pets.ui.shop.ShopFragment;
import cn.peterchen.pets.ui.status.StatusFragment;
import cn.peterchen.pets.ui.storage.StorageFragment;

/**
 * created by Peter
 */
public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);
        initFragments(rootView);

        return rootView;
    }

    /**
     * TODO 使用工厂模式优化这里的代码
     *
     * @param rootView
     */
    private void initFragments(ViewGroup rootView) {

        (rootView.findViewById(R.id.status)).setOnClickListener(getFragmentClickListener(StatusFragment.class, StatusFragment.TAG));
        (rootView.findViewById(R.id.shop)).setOnClickListener(getFragmentClickListener(ShopFragment.class, ShopFragment.TAG));
        (rootView.findViewById(R.id.chat)).setOnClickListener(getFragmentClickListener(ChatFragment.class, ChatFragment.TAG));
        (rootView.findViewById(R.id.storage)).setOnClickListener(getFragmentClickListener(StorageFragment.class, StorageFragment.TAG));
        (rootView.findViewById(R.id.search)).setOnClickListener(getFragmentClickListener(SearchFragment.class, SearchFragment.TAG));

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
