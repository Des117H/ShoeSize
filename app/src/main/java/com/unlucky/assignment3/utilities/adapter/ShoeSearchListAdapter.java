package com.unlucky.assignment3.utilities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoeSearchListAdapter extends BaseAdapter {

    private List<Shoe> shoeDataList = null;
    private ArrayList<Shoe>shoeDataArraylist;
    Context mContext;
    LayoutInflater inflater;

    // View lookup cache
    private static class ViewHolder {
        TextView shoeNameTextSearch, shoePriceTextSearch;
        ImageView shoeImageViewSearch;
    }

    public ShoeSearchListAdapter(Context context, List<Shoe>  shoeDataList) {
        mContext = context;
        this.shoeDataList =  shoeDataList;
        inflater = LayoutInflater.from(mContext);
        this.shoeDataArraylist = new ArrayList<Shoe>();
        this.shoeDataArraylist.addAll( shoeDataList);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Shoe dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_search_recycler_view, null);

            viewHolder.shoeNameTextSearch = convertView.findViewById(R.id.shoeSearchNameRecycle);
            viewHolder.shoePriceTextSearch = convertView.findViewById(R.id.shoeSearchPriceRecycle);
            viewHolder.shoeImageViewSearch = convertView.findViewById(R.id.shoeSearchImageRecycle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.shoeNameTextSearch.setText(dataModel.getName());
        viewHolder.shoePriceTextSearch.setText(dataModel.getPriceString());

        Glide.with(convertView).load(dataModel.getPictureLink()).into(viewHolder.shoeImageViewSearch);
        // Return the completed view to render on screen
        return convertView;
    }


    public void ListViewAdapter(Context context, List<Shoe>  shoeDataList) {
        mContext = context;
        this. shoeDataList =  shoeDataList;
        inflater = LayoutInflater.from(mContext);
        this.shoeDataArraylist = new ArrayList<Shoe>();
        this.shoeDataArraylist.addAll(shoeDataList);
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        shoeDataList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (charText.length() == 0) {
             shoeDataList.addAll(shoeDataArraylist);
        } else {
            for (Shoe wp : shoeDataArraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                     shoeDataList.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }
}