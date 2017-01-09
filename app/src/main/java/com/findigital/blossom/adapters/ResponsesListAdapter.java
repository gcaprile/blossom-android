package com.findigital.blossom.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.models.SurveyResponse;

import java.util.List;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class ResponsesListAdapter extends ArrayAdapter {

    Context context;
    List<SurveyResponse> responses;

    public ResponsesListAdapter(Context context, List<SurveyResponse> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.responses = items;
    }

    private class ViewHolder {
        TextView responseTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final SurveyResponse response = responses.get(position);

        if (convertView == null) {
            holder = new ResponsesListAdapter.ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.responses_list_item, null);
            holder.responseTitle = (TextView) convertView.findViewById(R.id.txtResponseTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.responseTitle.setText(response.getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return responses.size();
    }

    @Override
    public SurveyResponse getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
