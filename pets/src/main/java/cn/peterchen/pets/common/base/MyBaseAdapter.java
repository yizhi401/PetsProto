package cn.peterchen.pets.common.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

public abstract class MyBaseAdapter<E> extends BaseAdapter {

    protected List<E> data;
    protected Context context;
    protected LayoutInflater inflater;

    public MyBaseAdapter(Context context, List<E> data) {
        super();
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (data == null)
            return 0;
        else
            return data.size();

    }

    @Override
    public E getItem(int position) {
        if (data == null || data.size() == 0)
            return null;
        else
            return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    protected String getResString(int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

}
