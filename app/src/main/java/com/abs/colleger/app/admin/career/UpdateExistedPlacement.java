package com.abs.colleger.app.admin.career;

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

public class UpdateExistedPlacement extends AppCompatActivity {

    ProgressDialog progressBar;
    private Bitmap profileBitmap =null;
    private static final int REQ = 1;
    LinearLayout updateCareerLayout;
    MaterialCardView updateCareerLogoBtn;
    FloatingActionButton fabUpdateCareerImage;
    private ImageView updateCareerImage;
    private EditText updateCareerName, updateCareerDescription, updateCareerCompanyName, updateCareerLinkedin, updateCareerFacebook, updateCareerInstagram;
    private Button updateCareerBtn, deleteCareerBtn;
    private String careerTitle, careerDescription, careerCompanyName, careerFacebookId,careerInstagramId, image, category;
    private String downloadUrl, uniqueKey;
    private StorageReference storageReference;
    private DatabaseReference reference;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_existed_placement);

        toolbar=findViewById(R.id.updateCareerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        careerTitle = getIntent().getStringExtra("jobTitle");
        careerDescription = getIntent().getStringExtra("jobDescription");
        careerCompanyName =getIntent().getStringExtra("jobCompanyName");
        image = getIntent().getStringExtra("image");
        category=getIntent().getStringExtra("category");
        uniqueKey = getIntent().getStringExtra("key");

        updateCareerLayout=findViewById(R.id.updateCareerLayout);
        updateCareerImage = findViewById(R.id.updateCareerImage);
        updateCareerLogoBtn=findViewById(R.id.updateCareerLogoBtn);
        fabUpdateCareerImage=findViewById(R.id.fabUpdateCareerImage);
        updateCareerName=findViewById(R.id.updateCareerName);
        updateCareerDescription=findViewById(R.id.updateCareerDescription);
        updateCareerCompanyName=findViewById(R.id.updateCareerCompanyName);
        updateCareerLinkedin=findViewById(R.id.updateCareerLinkedin);
        updateCareerFacebook=findViewById(R.id.updateCareerFacebook);
        updateCareerInstagram=findViewById(R.id.updateCareerInstagram);
        updateCareerBtn=findViewById(R.id.updateCareerBtn);
        deleteCareerBtn=findViewById(R.id.deleteCareerBtn);

        try {
            Picasso.get().load(image).fit().centerCrop().into(updateCareerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateCareerName.setText(careerTitle);
        updateCareerDescription.setText(careerDescription);
        updateCareerCompanyName.setText(careerCompanyName);

        updateCareerLogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        fabUpdateCareerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        updateCareerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        deleteCareerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(UpdateExistedPlacement.this)
                        .setMessage("Are you sure want to delete this " + category +"?")
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialogInterface, i)->
                        {
                            deleteData();
                        }).setNegativeButton("CANCEL", (DialogInterface.OnClickListener)(dialogInterface, i) -> dialogInterface.cancel()).show();
            }

        });
        progressBar= new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Career");
        storageReference = FirebaseStorage.getInstance().getReference();

    }


    private void checkValidation() {
        careerTitle=updateCareerName.getText().toString();
        careerDescription=updateCareerDescription.getText().toString();
        careerCompanyName=updateCareerCompanyName.getText().toString();

        if(careerTitle.isBlank()){
            updateCareerName.setError("Empty");
            updateCareerName.requestFocus();
        } else if (careerDescription.isBlank()) {
            updateCareerDescription.setError("Empty");
            updateCareerDescription.requestFocus();
        } else if (careerCompanyName.isBlank()) {
            updateCareerCompanyName.setError("Empty");
            updateCareerCompanyName.requestFocus();
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
                    Snackbar.make(updateCareerLayout, "Deleted Successfully.", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateExistedPlacement.this, Career.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateCareerLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(updateCareerLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
            reference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Snackbar.make(updateCareerLayout, "Deleted Successfully.", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateExistedPlacement.this, Career.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateCareerLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateData(String s) {

        HashMap hashMap=new HashMap();

        hashMap.put("clubName", careerTitle);
        hashMap.put("clubDescription", careerDescription);
        hashMap.put("jobCompanyName", careerCompanyName);
        hashMap.put("image", s);

        String uniqueKey = getIntent().getStringExtra("key");
        String category= getIntent().getStringExtra("category");

        reference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Snackbar.make(updateCareerLayout, "Updated successfully.", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateExistedPlacement.this, Career.class);
                intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(updateCareerLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    private void uploadImage() {
        progressBar.setMessage("Uploading...");
        progressBar.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimage = baos.toByteArray();

        final StorageReference filePath;
        filePath=storageReference.child("Career").child(finalimage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateExistedPlacement.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

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
                    Snackbar.make(updateCareerLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
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
            updateCareerImage.setImageBitmap(profileBitmap);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

}