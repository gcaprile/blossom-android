package com.findigital.blossom.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.raweng.built.BuiltObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 14-AB109LA on 01/17/2017.
 */

public class LiteracyListAdapter extends ArrayAdapter {

    private Context context;
    private List<BuiltObject> items;
    private ArrayList<BuiltObject> arrayItems;

    public LiteracyListAdapter(Context context, List<BuiltObject> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
        this.arrayItems = new ArrayList<BuiltObject>();
        this.arrayItems.addAll(items);
    }

    private class ViewHolder {
        TextView itemName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        BuiltObject item = (BuiltObject) items.get(position);
        View mView = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            mView = mInflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.itemName = (TextView)mView.findViewById(R.id.txtListItemName);
            mView.setTag(holder);
        } else {
            mView = convertView;
            holder = (ViewHolder) mView.getTag();
        }

        holder.itemName.setText(item.get("fin_lit_title").toString());
        return mView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BuiltObject getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault());
        items.clear();
        if (query.length() == 0) {
            items.addAll(arrayItems);
        }
        else
        {
            for (int i = 0; i < arrayItems.size(); i++) {
                BuiltObject item = (BuiltObject) arrayItems.get(i);
                String data = item.get("fin_lit_title").toString();
                if (data.toLowerCase().startsWith(query)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
