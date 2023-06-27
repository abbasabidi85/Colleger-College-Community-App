package com.abs.colleger.app.admin.career;

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

public class PlacementAdapter extends RecyclerView.Adapter<PlacementAdapter.PlacementViewAdapter>{

    private List<PlacementData> placementDataList;
    private Context context;
    private String category;


    public PlacementAdapter(List<PlacementData> placementDataList, Context context, String category) {
        this.placementDataList = placementDataList;
        this.context=context;
        this.category=category;
    }

    @NonNull
    @Override
    public PlacementAdapter.PlacementViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.placement_item_layout, parent, false);
        return new PlacementViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacementAdapter.PlacementViewAdapter holder, int position) {

        PlacementData item = placementDataList.get(position);
        holder.jobTitle.setText(item.getJobTitle());
        holder.jobDescription.setText(item.getJobDescription());
        holder.jobCompanyName.setText(item.getJobCompanyName());

        try {
            Picasso.get().load(item.getImage()).fit().centerCrop().into(holder.companyLogoImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateExistedPlacement.class);
                intent.putExtra("jobTitle", item.getJobTitle());
                intent.putExtra("jobDescription", item.getJobDescription());
                intent.putExtra("jobCompanyName", item.getJobCompanyName());
                intent.putExtra("category", item.getCategory());
                //intent.putExtra("linkedinId", item.getLinkedinId());
                intent.putExtra("image",item.getImage());
                //intent.putExtra("facebookId", item.getFacebookId());
                //intent.putExtra("instagramId", item.getInstagramId());
                intent.putExtra("key", item.getKey());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return placementDataList.size();
    }

    public class PlacementViewAdapter extends RecyclerView.ViewHolder {

        private TextView jobTitle, jobDescription, jobCompanyName;
        private Button update;
        private ImageView companyLogoImageView;

        public PlacementViewAdapter(@NonNull View itemView) {
            super(itemView);
            jobTitle=itemView.findViewById(R.id.jobTitle);
            jobDescription=itemView.findViewById(R.id.jobDescription);
            jobCompanyName=itemView.findViewById(R.id.companyName);
            update=itemView.findViewById(R.id.updateButtonPlacement);
            companyLogoImageView=itemView.findViewById(R.id.placementCompanyIcon);
        }
    }
}
