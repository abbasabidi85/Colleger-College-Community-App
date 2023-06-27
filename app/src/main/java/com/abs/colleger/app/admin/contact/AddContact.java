package com.abs.colleger.app.admin.contact;

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

public class AddContact extends AppCompatActivity {

    LinearLayout updateContactLayout;

    MaterialCardView addContactImageBtn;
    private ImageView addContactImage;
    private Bitmap profileBitmap =null;
    FloatingActionButton fabAddImage;
    private EditText addContactName, addContactPhone, addContactEmail;
    private AutoCompleteTextView addContactDesignationCat;
    private AutoCompleteTextView addContactDepartmentCat;
    private AutoCompleteTextView addContactSchoolCat;
    private Button addContactBtn;
    private final int REQ = 1;
    private String name, email, phone, school, department, designation, downloadUrl ="";
    private String selectedCategorySchl;
    private String selectedCategoryDesg;
    private String selectedCategoryDept;
    private ProgressDialog progressBar;
    StorageReference storageReference;
    DatabaseReference reference, dbRef;
    MaterialToolbar toolbar;
    ArrayAdapter<String> categorySchoolAdapter,schoolOfChemical,schoolOfPharma,schoolOfUnani,schoolOfNursing,schoolOfEngineering,schoolOfManagement,schoolOfHumanities,schoolOfInterdisciplinary,designations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        toolbar=findViewById(R.id.addContactToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateContactLayout=findViewById(R.id.updateContact);
        addContactImageBtn=findViewById(R.id.addContactImageBtn);
        fabAddImage=findViewById(R.id.fabAddImage);
        addContactImage=findViewById(R.id.addContactImage);
        addContactName=findViewById(R.id.addContactName);
        addContactPhone=findViewById(R.id.addContactPhone);
        addContactEmail=findViewById(R.id.addContactEmail);
        addContactSchoolCat=findViewById(R.id.addContactSchoolCat);
        addContactDepartmentCat=findViewById(R.id.addContactDepartmentCat);
        addContactDesignationCat=findViewById(R.id.addContactDesignationCat);


        addContactBtn=findViewById(R.id.addContactBtn);
        progressBar= new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] categorySchool = getResources().getStringArray(R.array.Schools);
        categorySchoolAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categorySchool);
        addContactSchoolCat.setAdapter(categorySchoolAdapter);

        addContactSchoolCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                addContactDepartmentCat=findViewById(R.id.addContactDepartmentCat);

                selectedCategorySchl=categorySchoolAdapter.getItem(i);
                Snackbar.make(updateContactLayout, selectedCategorySchl, Snackbar.LENGTH_SHORT).show();


                    switch (selectedCategorySchl){
                        case "School of Chemical & Life Sciences":
                            String[] categoryChemical = getResources().getStringArray(R.array.SchoolOfChemical);
                            schoolOfChemical = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryChemical );
                            addContactDepartmentCat.setAdapter(schoolOfChemical);

                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);

                                    selectedCategoryDept=schoolOfChemical.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation );

                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Pharmaceutical Education & Research":
                            String[] categoryPharma = getResources().getStringArray(R.array.SchoolOfPharmaceutical);
                            schoolOfPharma = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryPharma);
                            addContactDepartmentCat.setAdapter(schoolOfPharma);

                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfPharma.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation);
                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Unani Medical Education & Research":
                            String[] categoryUnani = getResources().getStringArray(R.array.SchoolOfUnani);
                            schoolOfUnani = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryUnani );
                            addContactDepartmentCat.setAdapter(schoolOfUnani);

                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfUnani.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation );
                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Nursing Sciences & Allied Health":
                            String[] categoryNursing = getResources().getStringArray(R.array.SchoolOfNursing);
                            schoolOfNursing = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryNursing );
                            addContactDepartmentCat.setAdapter(schoolOfNursing);


                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfNursing.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation);
                                    addContactDesignationCat.setAdapter(designations);


                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Engineering Sciences & Technology":
                            String[] categoryEngineering = getResources().getStringArray(R.array.SchoolOfEngineering);
                            schoolOfEngineering = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryEngineering);
                            addContactDepartmentCat.setAdapter(schoolOfEngineering);


                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfEngineering.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation);
                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Management & Business Studies":
                            String[] categoryManagement = getResources().getStringArray(R.array.SchoolOfManagement);
                            schoolOfManagement = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryManagement);
                            addContactDepartmentCat.setAdapter(schoolOfManagement);


                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfManagement.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation);
                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Humanities & Social Sciences":
                            String[] categoryHumanities = getResources().getStringArray(R.array.SchoolOfHumanities);
                            schoolOfHumanities = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryHumanities);
                            addContactDepartmentCat.setAdapter(schoolOfHumanities);


                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfHumanities.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation);
                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        case "School of Interdisciplinary Sciences & Technology":
                            String[] categoryInterdis = getResources().getStringArray(R.array.SchoolOfInterdisciplinary);
                            schoolOfInterdisciplinary = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryInterdis);
                            addContactDepartmentCat.setAdapter(schoolOfInterdisciplinary);


                            addContactDepartmentCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    addContactDesignationCat=findViewById(R.id.addContactDesignationCat);
                                    selectedCategoryDept=schoolOfInterdisciplinary.getItem(i);
                                    Snackbar.make(updateContactLayout, selectedCategoryDept, Snackbar.LENGTH_SHORT).show();

                                    String[] categoryDesignation = getResources().getStringArray(R.array.Designations);
                                    designations = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryDesignation );
                                    addContactDesignationCat.setAdapter(designations);

                                    addContactDesignationCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            selectedCategoryDesg=designations.getItem(i);
                                            Snackbar.make(updateContactLayout, selectedCategoryDesg, Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            break;
                        default:break;
                    }
            }
        });


        addContactImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }

    private void checkValidation() {
        name=addContactName.getText().toString();
        phone=addContactPhone.getText().toString();
        email=addContactEmail.getText().toString();
        school=selectedCategorySchl;
        department=selectedCategoryDept;
        designation=selectedCategoryDesg;

        if(name.isBlank()){
            addContactName.setError("Empty");
            addContactName.requestFocus();
        } else if (phone.isBlank()) {
            addContactPhone.setError("Empty");
            addContactPhone.requestFocus();
        } else if (email.isBlank()) {
            addContactEmail.setError("Empty");
            addContactEmail.requestFocus();
        } else if (school==null) {
            Snackbar.make(updateContactLayout, "Please Provide School", Snackbar.LENGTH_SHORT).show();
        } else if (department==null) {
            Snackbar.make(updateContactLayout, "Please Provide Department", Snackbar.LENGTH_SHORT).show();
        } else if (designation==null) {
            Snackbar.make(updateContactLayout, "Please Provide Designation", Snackbar.LENGTH_SHORT).show();
        } else if (profileBitmap == null) {
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
        filePath=storageReference.child("Contacts").child(finalimage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(AddContact.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    downloadUrl=url.toString();
                                    insertData();
                                }
                            });
                        }
                    });
                } else {
                    progressBar.dismiss();
                    Snackbar.make(updateContactLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertData() {
        dbRef=reference.child(selectedCategoryDept);
        final String uniqueKey = dbRef.push().getKey();

        String image = downloadUrl;
        String key = uniqueKey;

        ContactData contactData = new ContactData(name, email, phone, downloadUrl,school, department, designation, key);

        dbRef.child(key).setValue(contactData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.dismiss();
                Snackbar.make(updateContactLayout, "Added Successfully", Snackbar.LENGTH_SHORT).show();
                addContactName.setText(null);
                addContactImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile));
                addContactPhone.setText(null);
                addContactEmail.setText(null);
                addContactSchoolCat.setText(null);
                addContactDepartmentCat.setText(null);
                addContactDesignationCat.setText(null);
                selectedCategorySchl=null;
                selectedCategoryDept=null;
                selectedCategoryDesg=null;
                profileBitmap=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Snackbar.make(updateContactLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
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
            addContactImage.setImageBitmap(profileBitmap);
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