package com.findigital.blossom.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.findigital.blossom.fragments.CareerDetailFragment;
import com.findigital.blossom.models.Career;
import com.findigital.blossom.models.SurveyResponse;

import java.util.ArrayList;

/**
 * Created by 14-AB109LA on 9/1/2017.
 */

public class SurveyResultListAdapter extends ArrayAdapter {

    Context context;
    ArrayList<Career> careers;

    public SurveyResultListAdapter(Context context, ArrayList<Career> careers) {
        super(context, android.R.layout.simple_list_item_1, careers);
        this.context = context;
        this.careers = careers;
    }

    private class ViewHolder {
        TextView careerName;
        TextView careerIntro;
        Button btnCareerInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Career career = careers.get(position);

        if (convertView == null) {
            holder = new SurveyResultListAdapter.ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.survey_result_list_item, null);
            holder.careerName = (TextView) convertView.findViewById(R.id.txtCareerName);
            holder.careerIntro = (TextView) convertView.findViewById(R.id.txtCareerIntro);
            holder.btnCareerInfo = (Button) convertView.findViewById(R.id.btnCareerMoreInfo);
            convertView.setTag(holder);
        } else {
            holder = (SurveyResultListAdapter.ViewHolder) convertView.getTag();
        }

        holder.careerName.setText(career.getCareerName());
        holder.careerIntro.setText(career.getCareerIntro());

        holder.btnCareerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CareerDetailFragment.class);
                intent.putExtra("careerId", career.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return careers.size();
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
