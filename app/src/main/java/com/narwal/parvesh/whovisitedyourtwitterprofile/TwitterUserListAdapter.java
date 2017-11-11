package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Parvesh on 09-Sep-17.
 */

public class TwitterUserListAdapter extends BaseAdapter {

    private List<String> user_names, screen_names, image_urls;
    private Context context;
    private LayoutInflater layoutInflater;
    private Typeface tf = null;
    private Boolean isUserRewarded;

    TwitterUserListAdapter(Context context, List<String> user_names, List<String> screen_names, List<String> image_urls, Boolean isUserRewarded){
        this.context = context;
        this.user_names = user_names;
        this.screen_names = screen_names;
        this.image_urls = image_urls;
        this.isUserRewarded = isUserRewarded;
        layoutInflater = LayoutInflater.from(context);
        tf = FontCache.get("font/Roboto-Light.ttf" ,context);
    }

    @Override
    public int getCount() {
        return user_names.size();
    }

    @Override
    public Object getItem(int i) {
        return user_names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.single_row, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.tvName);
            holder.screenname = (TextView) convertView.findViewById(R.id.tvUsername);
            holder.imageView = (CircleImageView) convertView.findViewById(R.id.imageView);
            holder.index = (TextView) convertView.findViewById(R.id.tvIndex);
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.username.setText(user_names.get(i));

        int index = i + 1;

        if(!isUserRewarded)  index = i + 2 ;

        holder.index.setText(String.format("%02d", index));
        String screenName = "@" + screen_names.get(i);
        holder.screenname.setText(screenName);
        holder.screenname.setTypeface(tf);
        holder.username.setTypeface(tf);
        holder.index.setTypeface(tf);

        Picasso.with(context).load(image_urls.get(i)).into(holder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        TextView username;
        TextView screenname;
        CircleImageView imageView;
        TextView index;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
