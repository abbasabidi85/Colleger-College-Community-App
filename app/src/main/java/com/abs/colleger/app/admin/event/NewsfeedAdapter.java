package com.abs.colleger.app.admin.event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.abs.colleger.app.R;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.NewsfeedViewAdapter> {

    private Context context;
    private ArrayList<EventData> list;
    private String downloadUrlNewsFeed="";

    public NewsfeedAdapter(Context context, ArrayList<EventData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsfeedViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.newsfeed_item_layout, parent, false);
        return new NewsfeedViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsfeedViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        EventData currentItem = list.get(position);
        downloadUrlNewsFeed = currentItem.getImage();
        holder.newsfeedTitle.setText(currentItem.getTitle());
        try {
            if(currentItem.getImage() != null){
                Picasso.get().load(currentItem.getImage()).fit().centerInside().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(holder.newsfeedImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.newsfeedDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(context)
                        .setMessage(
                                "Are you sure want to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton(
                            "OK",
                        (dialogInterface, i) -> {
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Events");

                            downloadUrlNewsFeed= currentItem.image;

                                    if (downloadUrlNewsFeed.isEmpty()){
                                    reference.child(currentItem.getKey()).removeValue()
                                            .addOnCompleteListener(task -> Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();                            }
                                            });
                                    notifyItemRemoved(position);}

                                    else {
                                        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.image);
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        reference.child(currentItem.getKey()).removeValue()
                                                .addOnCompleteListener(task -> Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        notifyItemRemoved(position);
                                    }
                        }
                )
                        .setNegativeButton(
                            "CANCEL",
                        (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.cancel()
                ).show();
            }
        });
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

    public class NewsfeedViewAdapter extends RecyclerView.ViewHolder{

        private Button newsfeedDeleteBtn;
        private TextView newsfeedTitle;
        private ImageView newsfeedImage;
        public NewsfeedViewAdapter(@NonNull View itemView) {
            super(itemView);
            newsfeedDeleteBtn= itemView.findViewById(R.id.newsfeedDeleteBtn);
            newsfeedTitle= itemView.findViewById(R.id.newsfeedTitle);
            newsfeedImage=itemView.findViewById(R.id.newsfeedImage);
        }
    }

}
