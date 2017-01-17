package com.findigital.blossom.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.raweng.built.BuiltObject;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 14-AB109LA on 29/12/2016.
 */

public class ScholarshipsPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<BuiltObject> scholarships;

    public ScholarshipsPageAdapter(Context context, List<BuiltObject> scholarships) {
        this.context = context;
        this.scholarships = scholarships;
    }

    @Override
    public int getCount() { return scholarships.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView txtScholarshipName;
        TextView txtScholarshipDesc;
        TextView txtScholarshipDue;
        TextView txtScholarshipAmount;
        TextView txtScholarshipLink;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.scholarshippager_item, container,
                false);

        txtScholarshipName = (TextView) itemView.findViewById(R.id.txtScholarshipName);
        txtScholarshipDesc = (TextView) itemView.findViewById(R.id.txtScholarshipDesc);
        txtScholarshipDue = (TextView) itemView.findViewById(R.id.txtScholarshipDue);
        txtScholarshipAmount = (TextView) itemView.findViewById(R.id.txtScholarshipAmount);
        txtScholarshipLink = (TextView) itemView.findViewById(R.id.txtScholarshipLink);

        String scholarshipName = scholarships.get(position).get("scholarship_name").toString();
        String scholarshipDesc = scholarships.get(position).get("scholarship_description").toString();
        String scholarshipDue = scholarships.get(position).get("scholarship_due_date").toString();
        String scholarshipAmount = scholarships.get(position).get("scholarship_amount").toString();
        String scholarshipLink = scholarships.get(position).get("scholarship_link").toString();

        String url = "";

        try {
            JSONObject link = new JSONObject(scholarshipLink);
            String linkTitle = link.get("title").toString();
            url = link.get("href").toString();
            txtScholarshipLink.setText(linkTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            Date dateParsed = originalFormat.parse(scholarshipDue);

            originalFormat = new SimpleDateFormat("MMM d, y, hh:mm a", Locale.US);
            scholarshipDue = originalFormat.format(dateParsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        scholarshipAmount = decimalFormat.format(Integer.parseInt(scholarshipAmount));
        scholarshipAmount = "$" + scholarshipAmount;

        txtScholarshipName.setText(scholarshipName);
        txtScholarshipDesc.setText(scholarshipDesc);
        txtScholarshipDue.setText(scholarshipDue);
        txtScholarshipAmount.setText(scholarshipAmount);

        final String finalUrl = url;
        txtScholarshipLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(finalUrl));
                context.startActivity(intent);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((LinearLayout) object);
    }
}
