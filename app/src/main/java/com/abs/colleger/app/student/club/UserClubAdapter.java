package com.abs.colleger.app.student.club;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.abs.colleger.app.R;

public class UserClubAdapter extends RecyclerView.Adapter<UserClubAdapter.UserClubViewAdapter>{

    private List<UserClubData> userClubDataList;
    private Context context;


    public UserClubAdapter(List<UserClubData> userClubDataList, Context context) {
        this.userClubDataList = userClubDataList;
        this.context=context;
    }

    @NonNull
    @Override
    public UserClubViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_club_item_layout, parent, false);
        return new UserClubViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserClubViewAdapter holder, int position) {

        UserClubData item = userClubDataList.get(position);
        holder.userClubName.setText(item.getClubName());
        holder.userClubDescription.setText(item.getClubDescription());

        try {
            Picasso.get().load(item.getImage()).fit().centerCrop().into(holder.userClubLogoImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return userClubDataList.size();
    }

    public class UserClubViewAdapter extends RecyclerView.ViewHolder {

        private TextView userClubName, userClubDescription;
        private ImageView userClubLogoImageView;

        public UserClubViewAdapter(@NonNull View itemView) {
            super(itemView);
            userClubName=itemView.findViewById(R.id.userClubName);
            userClubDescription=itemView.findViewById(R.id.userClubDescription);
            userClubLogoImageView=itemView.findViewById(R.id.userClubIcon);
        }
    }
}
