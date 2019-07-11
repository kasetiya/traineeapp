package com.softices.traineeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softices.traineeapp.R;

import java.util.ArrayList;

/**
 * Created by Amit on 16/07/2018.
 */

public class ContactListAdapter extends BaseAdapter {
    ArrayList<String> userName;
    ArrayList<String> userNumber;
    Context context;

    public ContactListAdapter(Context c2, ArrayList<String> name, ArrayList<String> number) {
        this.context = c2;
        this.userName = name;
        this.userNumber = number;
    }

    @Override
    public int getCount() {
        return userName.size();
    }

    @Override
    public Object getItem(int position) {
        return userName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View child, ViewGroup parent) {
        Holder holder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listview_layout, null);
            holder = new Holder();
            holder.txtListName = child.findViewById(R.id.tv_contact_name);
            holder.txtListNumber = child.findViewById(R.id.tv_contact_no);
            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        holder.txtListName.setText(userName.get(position));
        holder.txtListNumber.setText(userNumber.get(position));
        return child;
    }

    public class Holder {
        TextView txtListName, txtListNumber;
    }
}
