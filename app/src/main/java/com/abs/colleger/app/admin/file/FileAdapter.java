package com.abs.colleger.app.admin.file;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

import com.abs.colleger.app.R;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder>{

    private Context context;
    private List<FileData> list;

    public FileAdapter(Context context, List<FileData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_item_layout, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FileData currentItem = list.get(position);
        holder.fileNameTv.setText(list.get(position).getpdfTitle());

        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.getPdfUrl());

        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String fileSize= humanReadableByteCountSI(storageMetadata.getSizeBytes());
                holder.fileSizeTv.setText(fileSize);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        holder.fileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentItem.getPdfUrl()));
                context.startActivity(intent);
            }
        });

        holder.filedeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(context)
                        .setMessage(
                                "Are you sure want to delete this file?")
                        .setCancelable(true)
                        .setPositiveButton(
                                "OK",
                                (dialogInterface, i) -> {
                                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("pdf");
                                    DatabaseReference dbRef=reference.child(currentItem.getCategory());
                                    StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.getPdfUrl());

                                    fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    dbRef.child(currentItem.getKey()).removeValue()
                                            .addOnCompleteListener(task -> Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();                            }
                                            });
                                    notifyItemRemoved(position);
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

    public class FileViewHolder extends RecyclerView.ViewHolder {

        private TextView fileNameTv, fileSizeTv;
        private MaterialCardView filedeleteBtn, fileCardView;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            fileCardView=itemView.findViewById(R.id.fileCardView);
            filedeleteBtn=itemView.findViewById(R.id.deleteFileButton);
            fileNameTv=itemView.findViewById(R.id.fileNameTv);
            fileSizeTv=itemView.findViewById(R.id.fileSizeTv);
        }
    }
    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
}
