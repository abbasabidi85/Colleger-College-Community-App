package com.abs.colleger.app.admin.club;

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

public class AddClub extends AppCompatActivity {

    LinearLayout addClubLayout;

    MaterialCardView addClubLogoBtn;
    private ImageView addClubImage;
    private Bitmap profileBitmap =null;
    FloatingActionButton fabAddClubImage;
    private EditText addClubName, addClubDescription, addClubFacebook, addClubInstagram;
    private AutoCompleteTextView addClubCat;
    String selectedCategoryCS;
    private Button addClubBtn;
    private final int REQ = 1;
    private String clubName, clubDescription, clubFacebookId, clubInstagramId, downloadUrl ="", category;
    private ProgressDialog progressBar;
    StorageReference storageReference;
    DatabaseReference reference, dbRef;
    MaterialToolbar toolbar;
    ArrayAdapter<String> categoryClubAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);

        toolbar=findViewById(R.id.addClubToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addClubLayout=findViewById(R.id.addClubLayout);
        addClubLogoBtn=findViewById(R.id.addClubLogoBtn);
        fabAddClubImage=findViewById(R.id.fabAddClubImage);
        addClubImage=findViewById(R.id.addClubImage);
        addClubName=findViewById(R.id.addClubName);
        addClubDescription=findViewById(R.id.addClubDescription);
        addClubFacebook=findViewById(R.id.addClubFacebook);
        addClubInstagram=findViewById(R.id.addClubInstagram);
        addClubCat=findViewById(R.id.addClubCat);


        addClubBtn=findViewById(R.id.addClubBtn);
        progressBar= new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Club & Society");
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] categoryClub = getResources().getStringArray(R.array.Club);
        categoryClubAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categoryClub);
        addClubCat.setAdapter(categoryClubAdapter);

        addClubCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryCS=categoryClubAdapter.getItem(i);
                Snackbar.make(addClubLayout, selectedCategoryCS, Snackbar.LENGTH_SHORT).show();
            }
        });


        addClubLogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        fabAddClubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addClubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }

    private void checkValidation() {
        clubName=addClubName.getText().toString();
        clubDescription=addClubDescription.getText().toString();
        clubFacebookId=addClubFacebook.getText().toString();
        clubInstagramId=addClubInstagram.getText().toString();
        category=selectedCategoryCS;



        if(clubName.isBlank()){
            addClubName.setError("Empty");
            addClubName.requestFocus();
        } else if (clubDescription.isBlank()) {
            addClubDescription.setError("Empty");
            addClubDescription.requestFocus();
        }else if (category.isBlank()) {
            Snackbar.make(addClubLayout, "Please Provide Category", Snackbar.LENGTH_SHORT).show();
        }else if (profileBitmap == null) {
            insertData();
        } else {
            uploadImage();
        }
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
        uploadTask.addOnCompleteListener(AddClub.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

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
                    Snackbar.make(addClubLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertData() {
        dbRef=reference.child(category);
        final String uniqueKey = dbRef.push().getKey();

        String image = downloadUrl;
        String key = uniqueKey;
        String category = selectedCategoryCS;

        ClubData clubData = new ClubData(clubName, clubDescription, clubFacebookId,clubInstagramId, downloadUrl, category, key);

        dbRef.child(key).setValue(clubData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.dismiss();
                Snackbar.make(addClubLayout, "Added successfully", Snackbar.LENGTH_SHORT).show();
                addClubName.setText(null);
                addClubImage.setImageDrawable(ContextCompat.getDrawable(AddClub.this, R.drawable.job_on_success));
                addClubDescription.setText(null);
                addClubFacebook.setText(null);
                addClubInstagram.setText(null);
                addClubCat.setText(null);
                profileBitmap=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Snackbar.make(addClubLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
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
            addClubImage.setImageBitmap(profileBitmap);
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