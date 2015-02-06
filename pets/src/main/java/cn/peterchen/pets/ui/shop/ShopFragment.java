package cn.peterchen.pets.ui.shop;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.peterchen.pets.R;
import cn.peterchen.pets.entity.Master;
import cn.peterchen.pets.entity.ShopItem;

/**
 * Created by peter on 15-1-27.
 */
public class ShopFragment extends DialogFragment {

    public static final String TAG = ShopFragment.class.getName();

    private ListView shopList;

    private ShopListAdapter shopListAdapter;

    private List<ShopItem> shopItemList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        shopItemList = Master.getMaster().getShopItemList();
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.shop_fragment, container, false);
        shopList = (ListView) rootView.findViewById(R.id.shop_list);
        shopListAdapter = new ShopListAdapter(getActivity(), shopItemList);
        shopList.setAdapter(shopListAdapter);

        return rootView;
    }
}
