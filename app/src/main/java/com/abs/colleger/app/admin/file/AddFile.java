package com.abs.colleger.app.admin.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.abs.colleger.app.R;


public class AddFile extends AppCompatActivity {

    private final int REQ = 1;
    LinearLayout addFileQuestionPaper, pdfViewerLayout;
    private AutoCompleteTextView fileCategory;
    private MaterialButton selectFile;
    private EditText pdfTitleEditText;
    private TextView fileNameTextView;
    private String pdfName,pdfTitleEdit;
    private Uri pdfData;
    private Button uploadFileBtn;
    private DatabaseReference reference, dbRef;
    String downloadUrl = "";
    private ImageView fileImageView;
    private MaterialCardView nextPage;
    private MaterialCardView previousPage;
    private TextView pageNumber;
    private String selectedCategory="";
    PdfRenderer renderer;
    int total_pages = 0;
    int current_page = 0;
    MaterialToolbar toolbar;
    Bitmap bitmap;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);

        toolbar=findViewById(R.id.addFilesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child("pdf");

        pdfViewerLayout=findViewById(R.id.pdfViewerLayout);
        addFileQuestionPaper=findViewById(R.id.addFileQuestionPaper);
        selectFile = findViewById(R.id.selectFile);
        fileCategory = findViewById(R.id.fileCategory);
        uploadFileBtn = findViewById(R.id.fileUploadBtn);
        fileImageView = findViewById(R.id.pdfImageView);
        nextPage = findViewById(R.id.nextPage);
        previousPage = findViewById(R.id.previousPage);
        pageNumber = findViewById(R.id.pageNumber);
        fileNameTextView = findViewById(R.id.fileName);
        pdfTitleEditText = findViewById(R.id.pdfTitle);

        progressBar = new ProgressDialog(this);

        String[] categories = new String[]{"Previous Question Paper", "Notice","Performa", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categories);
        fileCategory.setAdapter(categoryAdapter);

        fileCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = categoryAdapter.getItem(i);
                Snackbar.make(addFileQuestionPaper, selectedCategory, Snackbar.LENGTH_SHORT).show();
            }
        });

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileExplorer();
            }
        });

        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_page > 0) {
                    current_page--;
                    _display(current_page);
                }
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_page < (total_pages - 1)) {
                    current_page++;
                    _display(current_page);
                }
            }
        });
        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfTitleEdit = pdfTitleEditText.getText().toString();
                if (selectedCategory.isBlank()) {
                    Snackbar.make(addFileQuestionPaper, "Please provide file category", Snackbar.LENGTH_SHORT).show();
                }else if (pdfTitleEdit.isBlank()) {
                    pdfTitleEditText.setError("Empty");
                    pdfTitleEditText.requestFocus();
                    Snackbar.make(addFileQuestionPaper, "Please enter the PDF title", Snackbar.LENGTH_SHORT).show();
                }else if (pdfData==null) {
                    Snackbar.make(selectFile, "Please select PDF", Snackbar.LENGTH_SHORT).show();
                } else{
                    progressBar.setMessage("Uploading File");
                    progressBar.show();
                    uploadPdf();
                }
            }
        });

    }

    private void openFileExplorer() {
        pdfViewerLayout.setVisibility(View.VISIBLE);
        Intent pickFile = new Intent(Intent.ACTION_GET_CONTENT);
        pickFile.addCategory(Intent.CATEGORY_OPENABLE);
        pickFile.setType("application/pdf");
        startActivityForResult(Intent.createChooser(pickFile, "Select PDF file"), REQ);
    }

    private void uploadPdf() {
        progressBar.setTitle("Please wait");
        progressBar.setMessage("Uploading File");
        progressBar.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference sRef = storageReference.child("pdf/"+ pdfName + "_" +System.currentTimeMillis()+".pdf");
        sRef.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        downloadUrl=String.valueOf(uri);
                        uploadData(downloadUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.dismiss();
                        Snackbar.make(addFileQuestionPaper, "Something went wrong!", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String d) {

        dbRef=reference.child(selectedCategory);
        final String uniqueKey = dbRef.push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",pdfTitleEdit);
        data.put("pdfUrl", downloadUrl);
        data.put("key",uniqueKey);
        data.put("category",selectedCategory);

        dbRef.child(uniqueKey).setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.dismiss();

                        fileImageView.setImageBitmap(null);
                        pdfTitleEditText.setText(null);
                        fileCategory.setText(null);
                        fileNameTextView.setText(null);

                        pdfData=null;
                        selectedCategory="";
                        pdfName=null;
                        total_pages=0;
                        current_page=0;
                        pageNumber.setText(current_page + "/" + total_pages);
                        downloadUrl="";

                        pdfViewerLayout.setVisibility(View.GONE);
                        Snackbar.make(addFileQuestionPaper, "Uploaded successfully!", Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.dismiss();
                        Snackbar.make(addFileQuestionPaper, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                    }
                });

    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {

            pdfData = data.getData();

            if (pdfData.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = AddFile.this.getContentResolver().query(pdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (pdfData.toString().startsWith("file://")) {
                pdfName = new File(pdfData.toString()).getName();
            }
            fileNameTextView.setText(pdfName);

        }

        if (data != null) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(pdfData, "r");
                renderer = new PdfRenderer(parcelFileDescriptor);
                total_pages = renderer.getPageCount();
                _display(current_page);
                fileImageView.setBackgroundColor(Color.WHITE);
            } catch (FileNotFoundException fnfe) {
                throw new RuntimeException(fnfe);
            } catch (IOException e) {
            }
        }
    }


    private void _display(int current_page) {
        if(renderer != null){
            PdfRenderer.Page page=renderer.openPage(current_page);
            bitmap =Bitmap.createBitmap(page.getWidth(),page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            fileImageView.setImageBitmap(bitmap);
            page.close();
            pageNumber.setText(current_page + 1 + "/" + total_pages);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] categories = new String[]{"Question Papers", "Notices","Timetables", "Others"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categories);
        fileCategory.setAdapter(categoryAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}