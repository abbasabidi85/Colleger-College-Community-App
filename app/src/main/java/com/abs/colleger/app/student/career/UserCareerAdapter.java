package com.abs.colleger.app.student.career;

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

public class UserCareerAdapter extends RecyclerView.Adapter<UserCareerAdapter.UserCareerViewAdapter>{

    private List<UserCareerData> userCareerDataList;
    private Context context;


    public UserCareerAdapter(List<UserCareerData> userCareerDataList, Context context) {
        this.userCareerDataList = userCareerDataList;
        this.context=context;
    }

    @NonNull
    @Override
    public UserCareerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_career_item_layout, parent, false);
        return new UserCareerViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCareerViewAdapter holder, int position) {

        UserCareerData item = userCareerDataList.get(position);
        holder.userJobTitle.setText(item.getJobTitle());
        holder.userJobDescription.setText(item.getJobDescription());
        holder.userJobCompanyName.setText(item.getJobCompanyName());

        try {
            Picasso.get().load(item.getImage()).fit().centerCrop().into(holder.careerCompanyLogoImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return userCareerDataList.size();
    }

    public class UserCareerViewAdapter extends RecyclerView.ViewHolder {

        private TextView userJobTitle, userJobDescription, userJobCompanyName;
        private ImageView careerCompanyLogoImageView;

        public UserCareerViewAdapter(@NonNull View itemView) {
            super(itemView);
            userJobTitle=itemView.findViewById(R.id.userJobTitle);
            userJobDescription=itemView.findViewById(R.id.userJobDescription);
            userJobCompanyName=itemView.findViewById(R.id.userCompanyName);
            careerCompanyLogoImageView=itemView.findViewById(R.id.userCareerCompanyIcon);
        }
    }
}
