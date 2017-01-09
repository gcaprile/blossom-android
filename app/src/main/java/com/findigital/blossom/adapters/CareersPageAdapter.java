package com.findigital.blossom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.raweng.built.BuiltObject;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 14-AB109LA on 29/12/2016.
 */

public class CareersPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<BuiltObject> careers;

    public CareersPageAdapter(Context context, List<BuiltObject> careers) {
        this.context = context;
        this.careers = careers;
    }

    @Override
    public int getCount() { return careers.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView txtCareerName;
        TextView txtCareerHeader;
        TextView txtCareerIntro;
        ImageView imgCareerImage;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.careerpager_item, container,
                false);

        txtCareerName = (TextView) itemView.findViewById(R.id.txtCareerName);
        txtCareerHeader = (TextView) itemView.findViewById(R.id.txtCareerHeader);
        txtCareerIntro = (TextView) itemView.findViewById(R.id.txtCareerIntro);
        imgCareerImage = (ImageView) itemView.findViewById(R.id.imgCareerImage);

        String careerName = careers.get(position).get("career_name").toString();
        String careerHeader = careers.get(position).get("career_intro_header").toString();
        String careerIntro = careers.get(position).get("career_intro").toString();
        String careerImage = careers.get(position).get("featured_image").toString();

        try {
            JSONObject careerImageObject = new JSONObject(careerImage);
            String url = careerImageObject.get("url").toString();

            Picasso.with(context)
                    .load(url)
                    .into(imgCareerImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtCareerName.setText(careerName);
        txtCareerHeader.setText(careerHeader);
        txtCareerIntro.setText(careerIntro);

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
