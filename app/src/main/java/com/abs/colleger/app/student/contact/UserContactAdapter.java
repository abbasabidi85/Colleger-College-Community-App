package com.abs.colleger.app.student.contact;

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

public class UserContactAdapter extends RecyclerView.Adapter<UserContactAdapter.ContactViewAdapter> {

    private List<UserContactData> userContactDataList;
    private Context context;

    public UserContactAdapter(List<UserContactData> userContactDataList, Context context) {
        this.userContactDataList = userContactDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_faculty_item_layout, parent, false);
        return new ContactViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewAdapter holder, int position) {

        UserContactData item = userContactDataList.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.phone.setText(item.getPhone());
        holder.designation.setText(item.getDesignation());

        try {
            Picasso.get().load(item.getImage()).fit().centerCrop().into(holder.profileImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return userContactDataList.size();
    }

    public class ContactViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, phone, designation;
        private ImageView profileImageView;

        public ContactViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userFacultyName);
            email = itemView.findViewById(R.id.userFacultyEmail);
            phone = itemView.findViewById(R.id.userFacultyPhone);
            designation = itemView.findViewById(R.id.userFacultyDesignation);
            profileImageView = itemView.findViewById(R.id.userFacultyProfileImage);
        }
    }
}
