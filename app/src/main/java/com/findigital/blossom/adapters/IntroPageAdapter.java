package com.findigital.blossom.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findigital.blossom.R;

/**
 * Created by 14-AB109LA on 27/12/2016.
 */

public class IntroPageAdapter extends PagerAdapter {

    Context context;
    String[] introContent;
    String[] introTitles;
    LayoutInflater inflater;

    public IntroPageAdapter(Context context, String[] introContent, String[] introTitles) {
        this.context = context;
        this.introContent = introContent;
        this.introTitles = introTitles;
    }

    @Override
    public int getCount() {
        return introContent.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView txtIntroText;
        TextView txtIntroTitle;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.intropager_item, container,
                false);

        txtIntroText = (TextView) itemView.findViewById(R.id.txtIntroText);
        txtIntroTitle = (TextView) itemView.findViewById(R.id.txtIntroTitle);

        // Capture position and set to the TextViews
        txtIntroText.setText(introContent[position]);
        txtIntroTitle.setText(introTitles[position]);

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((RelativeLayout) object);

    }
}
