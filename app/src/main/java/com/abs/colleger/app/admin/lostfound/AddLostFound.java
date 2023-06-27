package com.abs.colleger.app.admin.lostfound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.Calendar;

import com.abs.colleger.app.R;

public class AddLostFound extends AppCompatActivity {

    LinearLayout addLostFound;
    private final int REQ = 1;
    private MaterialButton lostFoundAddImage;
    private Bitmap bitmap;
    private ImageView lostFoundImageView;
    private EditText lostFoundTitle;
    private AutoCompleteTextView addLostFoundCat;
    private TextView fileName;
    private Button lostFoundUploadBtns;
    private DatabaseReference reference, dbRef;
    private StorageReference storageReference;
    String downloadUrl="";
    MaterialCardView imageLostCardView;
    String title, image, key, date, time, selectedLostFoundCategory, category;
    ProgressDialog progressBar;
    String imageName;
    MaterialToolbar toolbar;
    ArrayAdapter<String> categoryLostFoundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_found);

        toolbar=findViewById(R.id.lostFoundToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Lost & Found");
        storageReference = FirebaseStorage.getInstance().getReference();

        lostFoundImageView=findViewById(R.id.lostFoundImageView);
        lostFoundTitle=findViewById(R.id.lostFoundTitle);
        lostFoundUploadBtns=findViewById(R.id.lostFoundUploadBtn);
        progressBar=new ProgressDialog(this);
        imageLostCardView=findViewById(R.id.imageLostFoundCardView);
        addLostFound=findViewById(R.id.addLostFound);
        lostFoundAddImage=findViewById(R.id.addLostImage);
        fileName=findViewById(R.id.lostFoundImageName);

        addLostFoundCat=findViewById(R.id.addLostFoundCat);
        String[] categoryLostFound = getResources().getStringArray(R.array.LostFound);
        categoryLostFoundAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categoryLostFound);
        addLostFoundCat.setAdapter(categoryLostFoundAdapter);
        addLostFoundCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLostFoundCategory=categoryLostFoundAdapter.getItem(i);
                Snackbar.make(addLostFound, selectedLostFoundCategory, Snackbar.LENGTH_SHORT).show();
            }
        });

        lostFoundAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        lostFoundUploadBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }

    private void checkValidation(){
        category=selectedLostFoundCategory;
        title=lostFoundTitle.getText().toString();

        if(title.isBlank()){
            lostFoundTitle.setError("Empty");
            lostFoundTitle.requestFocus();
        } else if (category==null) {
            Snackbar.make(addLostFound, "Please Provide Category", Snackbar.LENGTH_SHORT).show();
        } else if(bitmap == null){
            uploadData();
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        progressBar.setMessage("Uploading Image");
        progressBar.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimage = baos.toByteArray();

        final StorageReference filePath;
        filePath=storageReference.child("Lost & Found").child(finalimage+"jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);

        uploadTask.addOnCompleteListener(AddLostFound.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Snackbar.make(addLostFound, "Something Went Wrong! Try Again.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void uploadData() {
        dbRef=reference.child("Lost & Found");
        final String uniqueKey = dbRef.push().getKey();

        title = lostFoundTitle.getText().toString();
        image = downloadUrl;
        key = uniqueKey;

        Calendar retrieveDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        date = currentDate.format(retrieveDate.getTime());

        Calendar retrieveTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        time = currentTime.format(retrieveTime.getTime());

        LostFoundData lostFoundData = new LostFoundData(title, image, date, time, key, category);

        reference.child(category).child(key).setValue(lostFoundData).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                progressBar.dismiss();
                imageLostCardView.setVisibility(View.GONE);
                lostFoundImageView.setVisibility(View.GONE);
                Snackbar.make(addLostFound, "Lost & Found Post Uploaded Successfully", Snackbar.LENGTH_SHORT).show();
                lostFoundTitle.setText(null);
                fileName.setText(null);
                addLostFoundCat.setText(null);
                lostFoundImageView.setImageBitmap(null);
                downloadUrl="";
                bitmap=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Snackbar.make(addLostFound, "Something Went Wrong! Try Again.", Snackbar.LENGTH_SHORT).show();
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
                    cursor = AddLostFound.this.getContentResolver().query(uri, null, null, null, null);
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
            imageLostCardView.setVisibility(View.VISIBLE);
            lostFoundImageView.setVisibility(View.VISIBLE);
            lostFoundImageView.setImageBitmap(bitmap);
        }
    }
}