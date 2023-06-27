package com.abs.colleger.app.admin.contact;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.abs.colleger.app.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewAdapter> {

    private List<ContactData> contactDataList;
    private Context context;
    private String category;

    public ContactAdapter(List<ContactData> contactDataList, Context context, String category) {
        this.contactDataList = contactDataList;
        this.context=context;
        this.category=category;
    }

    @NonNull
    @Override
    public ContactViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item_layout, parent, false);
        return new ContactViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewAdapter holder, int position) {

        ContactData item = contactDataList.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.phone.setText(item.getPhone());
        holder.designation.setText(item.getDesignation());

        try {
            Picasso.get().load(item.getImage()).fit().centerCrop().into(holder.profileImageView);
        } catch (Exception e) {
           e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateExistedContact.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("email", item.getEmail());
                intent.putExtra("phone", item.getPhone());
                intent.putExtra("image",item.getImage());
                intent.putExtra("designation", item.getDesignation());
                intent.putExtra("key", item.getKey());
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactDataList.size();
    }

    public class ContactViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, phone, designation;
        private Button update;
        private ImageView profileImageView;

        public ContactViewAdapter(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.facultyName);
            email=itemView.findViewById(R.id.facultyEmail);
            phone=itemView.findViewById(R.id.facultyPhone);
            designation=itemView.findViewById(R.id.facultyDesignation);
            update=itemView.findViewById(R.id.filledTonalButton);
            profileImageView=itemView.findViewById(R.id.facultyProfileImage);
        }
    }
}
