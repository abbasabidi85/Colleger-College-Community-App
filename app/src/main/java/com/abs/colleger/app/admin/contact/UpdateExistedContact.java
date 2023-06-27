package com.abs.colleger.app.admin.contact;

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

public class UpdateExistedContact extends AppCompatActivity {

    ProgressDialog progressBar;
    private Bitmap profileBitmap =null;
    private static final int REQ = 1;
    LinearLayout updateExistedContact;
    MaterialCardView existedContactImageBtn;
    FloatingActionButton fabUpdateImage;
    private ImageView existedContactImage;
    private EditText existedContactName, existedContactEmail, existedContactPhone, existedContactDesignation;
    private Button existedContactUpdateBtn, existedContactDeleteBtn;
    private AutoCompleteTextView existedContactDesignationCat;
    private String selectedCategoryDesg;
    ArrayAdapter<String> designations;
    private String name, email, phone, image, designation;
    private String downloadUrl="", uniqueKey, category;
    private StorageReference storageReference;
    private DatabaseReference reference;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_existed_contact);

        toolbar=findViewById(R.id.updateContactToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        image = getIntent().getStringExtra("image");
        designation = getIntent().getStringExtra("designation");

        uniqueKey = getIntent().getStringExtra("key");
        category= getIntent().getStringExtra("category");

        updateExistedContact=findViewById(R.id.updateExistedContact);
        existedContactImage = findViewById(R.id.existedContactImage);
        existedContactImageBtn=findViewById(R.id.existedContactImageBtn);
        fabUpdateImage=findViewById(R.id.fabAddNewImage);
        existedContactName=findViewById(R.id.existedContactName);
        existedContactEmail=findViewById(R.id.existedContactEmail);
        existedContactPhone=findViewById(R.id.existedContactPhone);
        existedContactDesignation=findViewById(R.id.existedContactDesignationCat);
        existedContactUpdateBtn=findViewById(R.id.existedContactUpdateBtn);
        existedContactDeleteBtn=findViewById(R.id.existedContactDeleteBtn);

        try {
            Picasso.get().load(image).fit().centerCrop().into(existedContactImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        existedContactName.setText(name);
        existedContactEmail.setText(email);
        existedContactPhone.setText(phone);
        existedContactDesignation.setText(designation);

        existedContactDesignationCat=findViewById(R.id.existedContactDesignationCat);
        String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
        designations = new ArrayAdapter<>(this, R.layout.drop_down_item, categoryDesignation);
        existedContactDesignationCat.setAdapter(designations);

        existedContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryDesg=designations.getItem(i);
                existedContactDesignation.setText(selectedCategoryDesg);
                Snackbar.make(updateExistedContact, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
            }
        });

        existedContactImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        fabUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        existedContactUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        existedContactDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(UpdateExistedContact.this)
                        .setMessage("Are you sure want to delete this contact?")
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialogInterface, i)->
                        {
                            deleteData();
                        }).setNegativeButton("CANCEL", (DialogInterface.OnClickListener)(dialogInterface, i) -> dialogInterface.cancel()).show();
            }

        });
        progressBar= new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();

    }


    private void checkValidation() {
        name=existedContactName.getText().toString();
        phone=existedContactPhone.getText().toString();
        email=existedContactEmail.getText().toString();
        designation=existedContactDesignation.getText().toString();

        if(name.isBlank()){
            existedContactName.setError("Empty");
            existedContactName.requestFocus();
        } else if (phone.isBlank()) {
            existedContactPhone.setError("Empty");
            existedContactPhone.requestFocus();
        } else if (email.isBlank()) {
            existedContactEmail.setError("Empty");
            existedContactEmail.requestFocus();
        } else if (designation==null) {
            Snackbar.make(updateExistedContact, "Please Provide Designation", Snackbar.LENGTH_SHORT).show();
        } else if (profileBitmap == null) {
            updateData(image);
        } else {
            uploadImage();
        }
    }

    private void deleteData() {


        if(image.isEmpty()) {
            reference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Snackbar.make(updateExistedContact, "Deleted Successfully", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateExistedContact.this, Contact.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateExistedContact, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateExistedContact, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
            reference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Snackbar.make(updateExistedContact, "Deleted Successfully", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateExistedContact.this, Contact.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(updateExistedContact, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateData(String s) {

        HashMap hashMap=new HashMap();

        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("phone", phone);
        hashMap.put("designation", designation);
        hashMap.put("image", s);

        String uniqueKey = getIntent().getStringExtra("key");
        String category= getIntent().getStringExtra("category");

        reference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Snackbar.make(updateExistedContact, "Updated successfully.", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateExistedContact.this, Contact.class);
                intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(updateExistedContact, "Something went wrong. Try again!", Snackbar.LENGTH_SHORT).show();
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
        filePath=storageReference.child("Contacts").child(finalimage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateExistedContact.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

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
                    Snackbar.make(updateExistedContact, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
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
            existedContactImage.setImageBitmap(profileBitmap);
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