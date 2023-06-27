package com.abs.colleger.app.admin.club;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.abs.colleger.app.R;

public class UpdateExistedClub extends AppCompatActivity {

    ProgressDialog progressBar;
    private Bitmap profileBitmap =null;
    private static final int REQ = 1;
    LinearLayout updateClubLayout;
    MaterialCardView updateClubLogoBtn;
    FloatingActionButton fabUpdateClubImage;
    private ImageView updateClubImage;
    private EditText updateClubName, updateClubDescription, updateClubFacebook, updateClubInstagram;
    private Button updateClubBtn, deleteClubBtn;
    private String clubName, clubDescription, clubFacebookId,clubInstagramId, image, category;
    private String downloadUrl, uniqueKey;
    private StorageReference storageReference;
    private DatabaseReference reference;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_existed_club);

        toolbar=findViewById(R.id.updateClubToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clubName = getIntent().getStringExtra("clubName");
        clubDescription = getIntent().getStringExtra("clubDescription");
        image = getIntent().getStringExtra("image");
        category=getIntent().getStringExtra("category");
        uniqueKey = getIntent().getStringExtra("key");

        updateClubLayout=findViewById(R.id.updateClubLayout);
        updateClubImage = findViewById(R.id.updateClubImage);
        updateClubLogoBtn=findViewById(R.id.updateClubLogoBtn);
        fabUpdateClubImage=findViewById(R.id.fabUpdateClubImage);
        updateClubName=findViewById(R.id.updateClubName);
        updateClubDescription=findViewById(R.id.updateClubDescription);
        updateClubFacebook=findViewById(R.id.updateClubFacebook);
        updateClubInstagram=findViewById(R.id.updateClubInstagram);
        updateClubBtn=findViewById(R.id.updateClubBtn);
        deleteClubBtn=findViewById(R.id.deleteClubBtn);

        try {
            Picasso.get().load(image).fit().centerCrop().into(updateClubImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateClubName.setText(clubName);
        updateClubDescription.setText(clubDescription);

        updateClubLogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        fabUpdateClubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        updateClubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        deleteClubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(UpdateExistedClub.this)
                        .setMessage("Are you sure want to delete this " + category +"?")
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialogInterface, i)->
                        {
                            deleteData();
                        }).setNegativeButton("CANCEL", (DialogInterface.OnClickListener)(dialogInterface, i) -> dialogInterface.cancel()).show();
            }

        });
        progressBar= new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Club & Society");
        storageReference = FirebaseStorage.getInstance().getReference();

    }


    private void checkValidation() {
        clubName=updateClubName.getText().toString();
        clubDescription=updateClubDescription.getText().toString();

        if(clubName.isBlank()){
            updateClubName.setError("Empty");
            updateClubName.requestFocus();
        } else if (clubDescription.isBlank()) {
            updateClubDescription.setError("Empty");
            updateClubDescription.requestFocus();
        } else if (profileBitmap == null) {
            updateData(image);
        } else {
            uploadImage();
        }
    }

    private void deleteData() {

        if(image.isEmpty()){
            reference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Snackbar.make(updateClubLayout, "Deleted Successfully.", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateExistedClub.this, Club.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateClubLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
        }else {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateClubLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
            reference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Snackbar.make(updateClubLayout, "Deleted Successfully", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateExistedClub.this, Club.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateClubLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateData(String s) {

        HashMap hashMap=new HashMap();

        hashMap.put("clubName", clubName);
        hashMap.put("clubDescription", clubDescription);
        hashMap.put("image", s);

        String uniqueKey = getIntent().getStringExtra("key");
        String category= getIntent().getStringExtra("category");

        reference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Snackbar.make(updateClubLayout, "Updated successfully.", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateExistedClub.this, Club.class);
                intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(updateClubLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    private void uploadImage() {
        progressBar.setTitle("Please wait");
        progressBar.setMessage("Uploading Image");
        progressBar.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimage = baos.toByteArray();

        final StorageReference filePath;
        filePath=storageReference.child("Club & Society").child(finalimage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateExistedClub.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    updateData(downloadUrl);
                                }
                            });
                        }
                    });
                } else {
                    progressBar.dismiss();
                    Snackbar.make(updateClubLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallery() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
            Uri profileImageData=data.getData();
            try {
                profileBitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),profileImageData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            updateClubImage.setImageBitmap(profileBitmap);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}