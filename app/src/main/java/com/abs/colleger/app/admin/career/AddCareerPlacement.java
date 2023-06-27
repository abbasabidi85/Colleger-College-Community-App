package com.abs.colleger.app.admin.career;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.abs.colleger.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.abs.colleger.app.R;

public class AddCareerPlacement extends AppCompatActivity {

    LinearLayout addCareerLayout;

    MaterialCardView addCompanyLogoBtn;
    private ImageView addCompanyImage;
    private Bitmap profileBitmap =null;
    FloatingActionButton fabAddCompanyImage;
    private EditText addJobTitle, addJobDescription, addJobCompanyName, addLinkedin, addFacebook, addInstagram;
    private Button addPlacementBtn;
    private AutoCompleteTextView addCareerCat;
    private final int REQ = 1;
    private String jobTitle, jobDescription, jobCompanyName, linkedinId, facebookId, instagramId, downloadUrl ="", category;
    private ProgressDialog progressBar;
    private String selectedCategoryCareer;
    StorageReference storageReference;
    DatabaseReference reference, dbRef;
    MaterialToolbar toolbar;
    ArrayAdapter<String> categoryCareerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_career_placement);

        toolbar=findViewById(R.id.addCareerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCareerLayout=findViewById(R.id.addCareerLayout);
        addCompanyLogoBtn=findViewById(R.id.addCompanyLogoBtn);
        fabAddCompanyImage=findViewById(R.id.fabAddCompanyImage);
        addCompanyImage=findViewById(R.id.addCompanyImage);
        addJobTitle=findViewById(R.id.addCareerTitle);
        addJobDescription=findViewById(R.id.addCareerDescription);
        addJobCompanyName=findViewById(R.id.addCareerCompanyName);
        addLinkedin=findViewById(R.id.addLinkedin);
        addFacebook=findViewById(R.id.addFacebook);
        addInstagram=findViewById(R.id.addInstagram);
        addCareerCat=findViewById(R.id.addCareerCat);

        addPlacementBtn=findViewById(R.id.addPlacementBtn);
        progressBar= new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Career");
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] categoryCareer = getResources().getStringArray(R.array.Career);
        categoryCareerAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categoryCareer);
        addCareerCat.setAdapter(categoryCareerAdapter);

        addCareerCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryCareer=categoryCareerAdapter.getItem(i);
                Snackbar.make(addCareerLayout, selectedCategoryCareer, Snackbar.LENGTH_SHORT).show();
            }
        });

        addCompanyLogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        fabAddCompanyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addPlacementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }

    private void checkValidation() {
        jobTitle=addJobTitle.getText().toString();
        jobDescription=addJobDescription.getText().toString();
        jobCompanyName=addJobCompanyName.getText().toString();
        //linkedinId=addLinkedin.getText().toString();
        //facebookId=addFacebook.getText().toString();
        //instagramId=addInstagram.getText().toString();
        category=selectedCategoryCareer;


        if(jobTitle.isBlank()){
            addJobTitle.setError("Empty");
            addJobTitle.requestFocus();
        } else if (jobDescription.isBlank()) {
            addJobDescription.setError("Empty");
            addJobDescription.requestFocus();
        } else if (jobCompanyName.isBlank()) {
            addJobCompanyName.setError("Empty");
            addJobCompanyName.requestFocus();
        } else if (category==null) {
            Snackbar.make(addCareerLayout, "Please Provide Category", Snackbar.LENGTH_SHORT).show();
        } else if (profileBitmap == null) {
            insertData();
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        progressBar.setMessage("Uploading " + category + " Info");
        progressBar.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimage = baos.toByteArray();

        final StorageReference filePath;
        filePath=storageReference.child("Career").child(finalimage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(AddCareerPlacement.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

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
                                    insertData();
                                }
                            });

                        }
                    });
                } else {
                    progressBar.dismiss();
                    Snackbar.make(addCareerLayout, "Something Went Wrong! Try Again.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertData() {
        dbRef=reference.child(selectedCategoryCareer);
        final String uniqueKey = dbRef.push().getKey();

        String image = downloadUrl;
        String key = uniqueKey;

        PlacementData contactData = new PlacementData(jobTitle, jobDescription, jobCompanyName,linkedinId, facebookId,instagramId, downloadUrl,category,key);

        dbRef.child(key).setValue(contactData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.dismiss();
                Snackbar.make(addCareerLayout, "Career info added successfully", Snackbar.LENGTH_SHORT).show();
                addJobTitle.setText(null);
                addCompanyImage.setImageDrawable(ContextCompat.getDrawable(AddCareerPlacement.this, R.drawable.job));
                addJobDescription.setText(null);
                addJobCompanyName.setText(null);
                addLinkedin.setText(null);
                addFacebook.setText(null);
                addInstagram.setText(null);
                addCareerCat.setText(null);
                profileBitmap=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Snackbar.make(addCareerLayout, "Something Went Wrong! Try Again.", Snackbar.LENGTH_SHORT).show();
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
            addCompanyImage.setImageBitmap(profileBitmap);
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