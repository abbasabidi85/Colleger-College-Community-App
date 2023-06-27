package com.abs.colleger.app.admin.lostfound;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.abs.colleger.app.R;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.LostFoundViewAdapter> {
    private final static String TAG = "LostFoundAdapterClass";
    private Context context;
    private ArrayList<LostFoundData> list;
    private String downloadUrlNewsFeed="";
    private String category;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser=mAuth.getCurrentUser();

    public LostFoundAdapter(Context context, ArrayList<LostFoundData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LostFoundAdapter.LostFoundViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.lost_found_item_layout, parent, false);
        return new LostFoundAdapter.LostFoundViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LostFoundAdapter.LostFoundViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        LostFoundData currentItem = list.get(position);

        holder.lostFoundDescription.setText(currentItem.getTitle());
        try {
            if(currentItem.getImage() != null){
                Picasso.get().load(currentItem.getImage()).resize(700,900).centerInside().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(holder.lostFoundImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.lostFoundDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category= currentItem.category;
                new MaterialAlertDialogBuilder(context)
                        .setMessage(
                                "Are you sure want to delete this post?")
                        .setCancelable(true)
                        .setPositiveButton(
                                "OK",
                                (dialogInterface, i) -> {
                                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Lost & Found").child(category);

                                    downloadUrlNewsFeed= currentItem.image;
                                    if(downloadUrlNewsFeed.isEmpty()){

                                        reference.child(currentItem.getKey()).removeValue()
                                                .addOnCompleteListener(task -> Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();                            }
                                                });
                                        notifyItemRemoved(position);

                                    }else {

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
                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();                            }
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

    public class LostFoundViewAdapter extends RecyclerView.ViewHolder{

        private Button lostFoundDeleteBtn;
        private TextView lostFoundDescription;
        private ImageView lostFoundImage;
        public LostFoundViewAdapter(@NonNull View itemView) {
            super(itemView);
            lostFoundDeleteBtn= itemView.findViewById(R.id.lostFoundDeleteBtn);
            lostFoundDescription= itemView.findViewById(R.id.lostFoundDescription);
            lostFoundImage=itemView.findViewById(R.id.lostFoundImage);
        }
    }
    private String getUserPhoneNumber() {
        String phoneNumberWithoutCountryCode = null;
        if (currentUser!=null){
            TelephonyManager telephonyManager = (TelephonyManager) context .getSystemService(context.TELEPHONY_SERVICE);
            String phoneNumberWithCountryCode = currentUser.getPhoneNumber();
            String defaultCountryIso = telephonyManager.getNetworkCountryIso(); // replace with your default country code
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberWithCountryCode, defaultCountryIso);
                phoneNumberWithoutCountryCode = String.valueOf(phoneNumber.getNationalNumber());
            } catch (NumberParseException e) {
                Log.e(TAG, "Error parsing phone number: " + e.getMessage());
            }

        }else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return phoneNumberWithoutCountryCode;
    }

}