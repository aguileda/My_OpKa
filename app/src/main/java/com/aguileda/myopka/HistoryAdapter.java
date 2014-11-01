package com.aguileda.myopka;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SeveDam on 28/10/2014.
 */
public class HistoryAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    ArrayList<String> data = null;

    public HistoryAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        HistoryHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.history_item, parent, false);

            holder = new HistoryHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.mTrashImg = (ImageView) convertView.findViewById(R.id.trashIcon);
            holder.mTrashImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,String.valueOf(position),Toast.LENGTH_SHORT).show();
                    String currentText = (String) getItem(position);
                    ((MainActivity)context).removeItemFromHistory(data.indexOf(currentText));

                }
            });



            convertView.setTag(holder);
        }
        else {
            holder = (HistoryHolder) convertView.getTag();
        }

        String text2 = (String) getItem(position);

        holder.mTextView.setText(text2);

        return convertView;

    }

    private static class HistoryHolder{
        public TextView mTextView;
        public ImageView mTrashImg;
    }


}
