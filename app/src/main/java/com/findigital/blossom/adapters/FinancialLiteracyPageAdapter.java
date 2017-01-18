package com.findigital.blossom.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class FinancialLiteracyPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<BuiltObject> literacy;

    public FinancialLiteracyPageAdapter(Context context, List<BuiltObject> literacy) {
        this.context = context;
        this.literacy = literacy;
    }

    @Override
    public int getCount() { return literacy.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView txtItemName;
        TextView txtItemHeader;
        TextView txtItemIntro;
        ImageView imgItemImage;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.literacypager_item, container,
                false);

        txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
        txtItemHeader = (TextView) itemView.findViewById(R.id.txtItemHeader);
        txtItemIntro = (TextView) itemView.findViewById(R.id.txtItemIntro);
        imgItemImage = (ImageView) itemView.findViewById(R.id.imgItemImage);

        String itemName = literacy.get(position).get("fin_lit_title").toString();
        String itemHeader = literacy.get(position).get("fin_lit_shortname").toString();
        String itemIntro = literacy.get(position).get("fin_lit_intro_text").toString();
        String itemImage = literacy.get(position).get("fin_lit_image").toString();

        try {
            JSONObject jsonObject = new JSONObject(itemImage);
            String url = jsonObject.get("url").toString();

            Picasso.with(context)
                    .load(url)
                    .into(imgItemImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtItemName.setText(itemName);
        txtItemHeader.setText(itemHeader);
        txtItemIntro.setText(itemIntro);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((LinearLayout) object);
    }
}
