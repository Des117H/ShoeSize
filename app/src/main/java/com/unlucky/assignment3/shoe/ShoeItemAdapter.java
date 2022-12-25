package com.unlucky.assignment3.shoe;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unlucky.assignment3.R;
import com.unlucky.assignment3.utilities.DownloadImageTask;

import java.util.List;

public class ShoeItemAdapter extends ArrayAdapter<Shoe> {
    private int resourceLayout;
    private Context mContext;

    public ShoeItemAdapter(Context context, int resource, List<Shoe> shoes) {
        super(context, resource, shoes);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(resourceLayout, null);
        }

        Shoe shoe = getItem(position);
        ViewHolder holder = new ViewHolder();

        if (shoe != null) {
            holder.shoeView = (ImageView) convertView.findViewById(R.id.shoeImageItem);
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.priceView = convertView.findViewById(R.id.price);

            holder.nameView.setText(shoe.getName());
            holder.priceView.setText((int) shoe.getPrice());
            new DownloadImageTask(holder.shoeView)
                    .execute(shoe.getPictureLink());
        }

        return view;
    }

    static class ViewHolder{
        ImageView shoeView;
        TextView nameView;
        TextView priceView;
    }
}
