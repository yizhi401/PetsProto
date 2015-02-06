package cn.peterchen.pets.ui.storage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.peterchen.pets.R;
import cn.peterchen.pets.common.base.MyBaseAdapter;
import cn.peterchen.pets.entity.ShopItem;

/**
 * Created by peter on 15-1-27.
 */
public class StorageListAdapter extends MyBaseAdapter<ShopItem> {

    public StorageListAdapter(Context context, List<ShopItem> data) {
        super(context, data);
    }

    class Holder {
        TextView name;
        ImageView image;
        TextView effects;
        TextView storage;
        Button useBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.storage_list_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.storage = (TextView) convertView.findViewById(R.id.storage);
            holder.useBtn = (Button) convertView.findViewById(R.id.use_btn);
            holder.effects = (TextView) convertView.findViewById(R.id.effects);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ShopItem item = data.get(position);
//        holder.name.setText(item.name + "");
//        holder.storage.setText("我的库存:" + item.myStorage);
//        holder.effects.setText("效果:" + item.effectsDes);

        return convertView;
    }
}
