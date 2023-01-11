package com.unlucky.assignment3.utilities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;

import java.util.List;

public class ShoeSearchRecyclerViewAdapter  extends RecyclerView.Adapter<ShoeSearchRecyclerViewAdapter.ViewHolder> {
    private List<Shoe> mData;
    private LayoutInflater mInflater;
    Context mContext;

    // data is passed into the constructor
    public ShoeSearchRecyclerViewAdapter(Context context, List<Shoe> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ShoeSearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_recycler_view, parent, false);
        return new ShoeSearchRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ShoeSearchRecyclerViewAdapter.ViewHolder holder, int position) {
        if (!mData.isEmpty()) {
            holder.shoeSearchNameRecycle.setText(mData.get(position).getName());
            holder.shoeSearchPriceRecycle.setText(mData.get(position).getPriceString());

            Glide.with(mContext).load(mData.get(position).getPictureLink()).into(holder.shoeSearchImageRecycle);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shoeSearchNameRecycle, shoeSearchPriceRecycle;
        ImageView shoeSearchImageRecycle;

        ViewHolder(View itemView) {
            super(itemView);
            shoeSearchNameRecycle = itemView.findViewById(R.id.shoeSearchNameRecycle);
            shoeSearchImageRecycle = itemView.findViewById(R.id.shoeSearchImageRecycle);
            shoeSearchPriceRecycle = itemView.findViewById(R.id.shoeSearchPriceRecycle);
        }
    }
}
