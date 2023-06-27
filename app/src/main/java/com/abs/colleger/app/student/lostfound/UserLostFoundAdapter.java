package com.abs.colleger.app.student.lostfound;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.abs.colleger.app.R;

public class UserLostFoundAdapter extends RecyclerView.Adapter<UserLostFoundAdapter.UserLostFoundViewAdapter> {

    private Context context;
    private ArrayList<UserLostFoundData> list;
    private String downloadUrl = "";

    public UserLostFoundAdapter(Context context, ArrayList<UserLostFoundData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserLostFoundViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_lostfound_item_layout, parent, false);
        return new UserLostFoundViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserLostFoundViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        UserLostFoundData currentItem = list.get(position);
        downloadUrl = currentItem.getImage();

        holder.lostFoundTitle.setText(currentItem.getTitle());
        holder.lostDate.setText(currentItem.getDate());
        holder.lostTime.setText(currentItem.getTime());

        try {
            if (currentItem.getImage() != null) {
                Picasso.get().load(currentItem.getImage()).fit().centerInside().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(holder.lostFoundImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class UserLostFoundViewAdapter extends RecyclerView.ViewHolder {

        private TextView lostFoundTitle, lostDate, lostTime;
        private ImageView lostFoundImage;

        public UserLostFoundViewAdapter(@NonNull View itemView) {
            super(itemView);
            lostFoundTitle = itemView.findViewById(R.id.userLostFoundTitle);
            lostFoundImage = itemView.findViewById(R.id.userLostFoundImage);
            lostDate = itemView.findViewById(R.id.lostDate);
            lostTime = itemView.findViewById(R.id.lostTime);

        }
    }

}
