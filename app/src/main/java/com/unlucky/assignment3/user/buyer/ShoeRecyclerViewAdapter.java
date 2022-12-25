package com.unlucky.assignment3.user.buyer;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.utilities.DownloadImageTask;

import java.util.List;

public class ShoeRecyclerViewAdapter extends RecyclerView.Adapter<ShoeRecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    ShoeRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.shoeNameRecycle.setText(mData.get(position));
        new DownloadImageTask(holder.shoeImageRecycle)
                .execute(convertNameToPicLink(mData.get(position)));

        System.out.println(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shoeNameRecycle;
        ImageView shoeImageRecycle;

        ViewHolder(View itemView) {
            super(itemView);
            shoeNameRecycle = itemView.findViewById(R.id.shoeNameRecylce);
            shoeImageRecycle = itemView.findViewById(R.id.shoeImageRecycle);
        }
    }

    public String convertNameToPicLink(String name) {
        String nameLink = name.replaceAll(" ", "-");
        nameLink = nameLink.replaceAll("[()]", "");
        return "https://images.stockx.com/360/" + nameLink +
                "/Images/" + nameLink + "/Lv2/img01.jpg?fm=jpg&amp;";
    }
}