package com.example.to_be_decided;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PillsAdapter extends ArrayAdapter<Pills> {

    private static final String TAG = "PillsAdapter";
    private Context mContext;
    int mResource;

    public PillsAdapter(@NonNull Context context, int resource, @NonNull List<Pills> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String barcode = getItem(position).getBarcode();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvName = (TextView) convertView.findViewById(R.id.textView_Name);
        TextView tvBarcode = (TextView) convertView.findViewById(R.id.textView_Bar);

        tvName.setText(name);
        tvBarcode.setText(barcode);
        return convertView;
    }
}
