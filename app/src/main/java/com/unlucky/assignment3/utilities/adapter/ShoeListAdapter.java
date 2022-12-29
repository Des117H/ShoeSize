package com.unlucky.assignment3.utilities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;

import java.util.ArrayList;
import java.util.List;

public class ShoeListAdapter extends BaseAdapter {
    private List<Shoe> shoeDataList = null;
    Context mContext;
    LayoutInflater inflater;

    // View lookup cache
    private static class ViewHolder {
        TextView shoeNameTextSearch, shoePriceTextSearch;
        ImageView shoeImageViewSearch;
    }

    public ShoeListAdapter(Context context, List<Shoe>  shoeDataList) {
        mContext = context;
        this.shoeDataList =  shoeDataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Shoe dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ShoeListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ShoeListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_recycler_view, null);

            viewHolder.shoeNameTextSearch = convertView.findViewById(R.id.shoeNameRecycle);
            viewHolder.shoePriceTextSearch = convertView.findViewById(R.id.shoePriceRecycle);
            viewHolder.shoeImageViewSearch = convertView.findViewById(R.id.shoeImageRecycle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShoeListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.shoeNameTextSearch.setText(dataModel.getName());
        viewHolder.shoePriceTextSearch.setText(dataModel.getPriceString());

        Glide.with(convertView).load(dataModel.getPictureLink()).into(viewHolder.shoeImageViewSearch);

        // Return the completed view to render on screen
        return convertView;
    }

    public void ListViewAdapter(Context context, List<Shoe>  shoeDataList) {
        mContext = context;
        this.shoeDataList =  shoeDataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return  shoeDataList.size();
    }

    @Override
    public Shoe getItem(int position) {
        return  shoeDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
