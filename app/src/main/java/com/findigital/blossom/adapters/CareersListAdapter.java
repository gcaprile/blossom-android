package com.findigital.blossom.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.raweng.built.BuiltObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 14-AB109LA on 2/1/2017.
 */

public class CareersListAdapter extends ArrayAdapter {

    private Context context;
    private List<BuiltObject> items;
    private ArrayList<BuiltObject> arrayItems;

    public CareersListAdapter(Context context, List<BuiltObject> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
        this.arrayItems = new ArrayList<BuiltObject>();
        this.arrayItems.addAll(items);
    }

    private class ViewHolder {
        TextView careerName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        BuiltObject item = (BuiltObject) items.get(position);
        View mView = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            mView = mInflater.inflate(R.layout.careers_list_item, null);

            holder = new ViewHolder();
            holder.careerName = (TextView)mView.findViewById(R.id.txtCareersListName);
            mView.setTag(holder);
        } else {
            mView = convertView;
            holder = (ViewHolder) mView.getTag();
        }

        holder.careerName.setText(item.get("career_name").toString());
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
                String data = item.get("career_name").toString();
                if (data.toLowerCase().startsWith(query)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
