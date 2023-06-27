package com.abs.colleger.app.admin.club;

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

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewAdapter>{

    private List<ClubData> clubDataList;
    private Context context;
    private String category;


    public ClubAdapter(List<ClubData> clubDataList, Context context, String category) {
        this.clubDataList = clubDataList;
        this.context=context;
        this.category=category;
    }

    @NonNull
    @Override
    public ClubAdapter.ClubViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_item_layout, parent, false);
        return new ClubAdapter.ClubViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ClubViewAdapter holder, int position) {

        ClubData item = clubDataList.get(position);
        holder.clubName.setText(item.getClubName());
        holder.clubDescription.setText(item.getClubDescription());

        try {
            Picasso.get().load(item.getImage()).fit().centerCrop().into(holder.clubLogoImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateExistedClub.class);
                intent.putExtra("clubName", item.getClubName());
                intent.putExtra("clubDescription", item.getClubDescription());
                intent.putExtra("image",item.getImage());
                intent.putExtra("category",item.getCategory());
                //intent.putExtra("facebookId", item.getFacebookId());
                //intent.putExtra("instagramId", item.getInstagramId());
                intent.putExtra("key", item.getKey());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return clubDataList.size();
    }

    public class ClubViewAdapter extends RecyclerView.ViewHolder {

        private TextView clubName, clubDescription;
        private Button update;
        private ImageView clubLogoImageView;

        public ClubViewAdapter(@NonNull View itemView) {
            super(itemView);
            clubName=itemView.findViewById(R.id.clubName);
            clubDescription=itemView.findViewById(R.id.clubDescription);
            update=itemView.findViewById(R.id.updateButtonClub);
            clubLogoImageView=itemView.findViewById(R.id.clubIcon);
        }
    }
}
