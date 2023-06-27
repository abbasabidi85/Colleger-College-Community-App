package com.abs.colleger.app.student.newsfeed;

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

public class UserNewsfeedAdapter extends RecyclerView.Adapter<UserNewsfeedAdapter.UserNewsfeedViewAdapter> {

    private Context context;
    private ArrayList<UserEventData> list;
    private String downloadUrl = "";

    public UserNewsfeedAdapter(Context context, ArrayList<UserEventData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserNewsfeedViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_newsfeed_item_layout, parent, false);
        return new UserNewsfeedViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNewsfeedViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        UserEventData currentItem = list.get(position);
        downloadUrl = currentItem.getImage();

        holder.newsfeedTitle.setText(currentItem.getTitle());
        holder.date.setText(currentItem.getDate());
        holder.time.setText(currentItem.getTime());

        try {
            if (currentItem.getImage() != null) {
                Picasso.get().load(currentItem.getImage()).fit().centerInside().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(holder.newsfeedImage);
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

    public class UserNewsfeedViewAdapter extends RecyclerView.ViewHolder {

        private TextView newsfeedTitle, date, time;
        private ImageView newsfeedImage;

        public UserNewsfeedViewAdapter(@NonNull View itemView) {
            super(itemView);
            newsfeedTitle = itemView.findViewById(R.id.userNewsfeedTitle);
            newsfeedImage = itemView.findViewById(R.id.userNewsfeedImage);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }

}
