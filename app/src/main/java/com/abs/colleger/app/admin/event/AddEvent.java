package com.abs.colleger.app.admin.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import com.abs.colleger.app.R;


public class AddEvent extends AppCompatActivity {

    LinearLayout addEvent;
    private final int REQ = 1;
    private MaterialButton addImage;
    private Bitmap bitmap;
    private ImageView eventImageView;
    private EditText eventTitle;
    private TextView fileName;
    private Button eventUploadBtns;
    private DatabaseReference reference, dbRef;
    private StorageReference storageReference;
    String downloadUrl="";
    ProgressDialog progressBar;
    String imageName;
    MaterialToolbar toolbar;

    MaterialCardView imageMaterialCardView;

    String title, image, key, date, time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar=findViewById(R.id.addEventsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=new ProgressDialog(this);


        imageMaterialCardView=findViewById(R.id.imageMaterialCardView);
        addEvent=findViewById(R.id.addEvent);
        addImage=findViewById(R.id.addImage);

        fileName=findViewById(R.id.imageName);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Events");
        storageReference = FirebaseStorage.getInstance().getReference();

        eventImageView=findViewById(R.id.eventImageView);


        eventTitle=findViewById(R.id.eventTitle);

        eventUploadBtns=findViewById(R.id.eventUploadBtn);
        eventUploadBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventTitle.getText().toString().isEmpty()){
                    eventTitle.setError("Empty");
                    eventTitle.requestFocus();
                    Snackbar.make(addEvent,"Please Provide Event Title",Snackbar.LENGTH_SHORT).show();
                } else if (bitmap==null) {
                    uploadData();
                } else {
                    uploadImage();
                }
            }
        });

    }

    private void uploadImage() {
        progressBar.setMessage("Uploading Image");
        progressBar.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimage = baos.toByteArray();

        final StorageReference filePath;
        filePath=storageReference.child("Events").child(finalimage +"jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);

        uploadTask.addOnCompleteListener(AddEvent.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                } else {
                    progressBar.dismiss();
                    Snackbar.make(addEvent, "Something Went Wrong! Try Again.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void uploadData() {
        dbRef=reference.child("Events");
        final String uniqueKey = dbRef.push().getKey();

        title = eventTitle.getText().toString();
        image = downloadUrl;
        key = uniqueKey;

        Calendar retrieveDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        date = currentDate.format(retrieveDate.getTime());

        Calendar retrieveTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        time = currentTime.format(retrieveTime.getTime());

        EventData eventData = new EventData(title, image, date, time, key);

        reference.child(key).setValue(eventData).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                progressBar.dismiss();
                imageMaterialCardView.setVisibility(View.GONE);
                eventImageView.setVisibility(View.GONE);
                Snackbar.make(addEvent, "Event Uploaded Successfully", Snackbar.LENGTH_SHORT).show();

                eventTitle.setText(null);
                fileName.setText(null);
                eventImageView.setImageBitmap(null);;
                downloadUrl="";
                bitmap=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Snackbar.make(addEvent, "Something Went Wrong! Try Again.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri =data.getData();

            if (uri.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = AddEvent.this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        imageName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (uri.toString().startsWith("file://")) {
                imageName = new File(uri.toString()).getName();
            }
            fileName.setText(imageName);

            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageMaterialCardView.setVisibility(View.VISIBLE);
            eventImageView.setVisibility(View.VISIBLE);
            eventImageView.setImageBitmap(bitmap);


        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}