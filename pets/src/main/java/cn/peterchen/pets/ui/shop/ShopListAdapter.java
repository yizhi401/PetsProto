package cn.peterchen.pets.ui.shop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.peterchen.pets.R;
import cn.peterchen.pets.common.MyBaseAdapter;
import cn.peterchen.pets.entity.ShopItem;

/**
 * Created by peter on 15-1-27.
 */
public class ShopListAdapter extends MyBaseAdapter<ShopItem> {

    public ShopListAdapter(Context context, List<ShopItem> data) {
        super(context, data);
    }

    class Holder {
        TextView name;
        ImageView image;
        TextView buyPrice;
        TextView sellPrice;
        EditText numEdit;
        TextView storage;
        Button sellBtn;
        Button buyBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.shop_list_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.buyPrice = (TextView) convertView.findViewById(R.id.buy_price);
            holder.sellPrice = (TextView) convertView.findViewById(R.id.sell_price);
            holder.numEdit = (EditText) convertView.findViewById(R.id.num_edit);
            holder.storage = (TextView) convertView.findViewById(R.id.storage);
            holder.sellBtn = (Button) convertView.findViewById(R.id.sell_btn);
            holder.buyBtn = (Button) convertView.findViewById(R.id.buy_btn);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ShopItem item = data.get(position);
        holder.name.setText(item.name + "");
        holder.buyPrice.setText("购买:" + item.buyPrice);
        holder.sellPrice.setText("出售:" + item.sellPrice);
        holder.storage.setText("我的库存:" + item.myStorage);

        return convertView;
    }
}
