package com.findigital.blossom.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.raweng.built.BuiltObject;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;

import java.util.List;

/**
 * Created by 14-AB109LA on 29/12/2016.
 */

public class LinksPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<BuiltObject> links;

    public LinksPageAdapter(Context context, List<BuiltObject> links) {
        this.context = context;
        this.links = links;
    }

    @Override
    public int getCount() { return links.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView txtLinktTitle;
        TextView txtLinkDescription;
        TextView txtLinkUrl;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.linkspager_item, container,
                false);

        txtLinktTitle = (TextView) itemView.findViewById(R.id.txtLinkTitle);
        txtLinkDescription = (TextView) itemView.findViewById(R.id.txtLinkDescription);
        txtLinkUrl = (TextView) itemView.findViewById(R.id.txtLinkUrl);

        String linkTitle = links.get(position).get("link_title").toString();
        String linkDescription = links.get(position).get("link_description").toString();
        String linkUrl = links.get(position).get("link_url").toString();

        String url = "";

        try {
            JSONObject link = new JSONObject(linkUrl);
            String title = link.get("title").toString();
            url = link.get("href").toString();
            txtLinkUrl.setText(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtLinktTitle.setText(linkTitle);
        txtLinkDescription.setText(linkDescription);

        final String finalUrl = url;
        txtLinkUrl.setOnClickListener(new View.OnClickListener() {
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
