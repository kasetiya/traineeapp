package com.softices.traineeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.softices.traineeapp.R;
import com.softices.traineeapp.database.DatabaseManager;
import com.softices.traineeapp.models.UserModel;
import com.softices.traineeapp.sharedPreferences.AppPref;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_all_users, parent, false);
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
            holder.ivDelete.setOnClickListener(view -> {
                dbManager.deleteUser(model.getEmail());
                userModelList.remove(position);
                notifyDataSetChanged();
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
            tvMail = view.findViewById(R.id.tv_mail_value);
            tvName = view.findViewById(R.id.tv_name_value);
            ivDelete = view.findViewById(R.id.iv_delete);
            cardView = view.findViewById(R.id.card_view);
        }
    }
}
