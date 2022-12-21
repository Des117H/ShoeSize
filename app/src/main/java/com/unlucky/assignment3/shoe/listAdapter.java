package com.unlucky.assignment3.shoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unlucky.assignment3.R;

import java.util.List;

public class listAdapter extends BaseAdapter {

    private List<itemInList> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public listAdapter(Context aContex,List<itemInList>listData){
        this.context = aContex;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContex);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.shoeView = (ImageView) convertView.findViewById(R.id.imageView4);
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.subNameView = (TextView) convertView.findViewById(R.id.subname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        itemInList shoe = this.listData.get(position);
        holder.nameView.setText(shoe.getName());
        holder.subNameView.setText("subname: " + shoe.getSubName());
        holder.priceView.setText(shoe.getPrice());
        //int imageId = this.getMipmapResIdByName(country.getFlagName());
        //holder.flagView.setImageResource(imageId);

        return convertView;
    }
    static class ViewHolder{
        ImageView shoeView;
        TextView nameView;
        TextView subNameView;
        TextView priceView;
    }
}
