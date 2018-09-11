package com.softices.traineeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softices.traineeapp.R;
import com.softices.traineeapp.activities.ProfileActivity;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Amit on 16/07/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<UserModel> userModelList;
    private Context context;
    private DatabaseManager dbManager;

    public UserAdapter(ArrayList<UserModel> userModelList, Context context) {
        this.userModelList = userModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_property, parent, false);
        ViewHolder gvh = new ViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, final int position) {
        final UserModel model = userModelList.get(position);

        if (model.getEmail() == AppPref.getUserEmail(context)) {
            holder.cardView.setVisibility(View.GONE);
        } else {
            holder.tvName.setText(model.getFirstName());
            holder.tvMail.setText(model.getEmail());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbManager.deleteUser(model.getEmail());
                    userModelList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMail;
        ImageView ivDelete;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);

            dbManager = new DatabaseManager(context);
            tvMail = (TextView) view.findViewById(R.id.tv_mail_value);
            tvName = (TextView) view.findViewById(R.id.tv_name_value);
            ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
