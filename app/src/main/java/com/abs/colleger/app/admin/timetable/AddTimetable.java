package com.abs.colleger.app.admin.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.abs.colleger.app.R;
import com.abs.colleger.app.admin.contact.ContactData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTimetable extends AppCompatActivity {

    LinearLayout addTimetableLayout;
    TextInputEditText addLectureTime;
    MaterialToolbar toolbar;
    private EditText addTimetableSubject, addTimetableTeacher, addTimetableRoom;
    private AutoCompleteTextView addTimetableDaysCat;
    private AutoCompleteTextView addTimetableCourseCat;
    private AutoCompleteTextView addTimetableSemesterCat;
    private AutoCompleteTextView addTimetableSectionCat;
    private Button addLectureBtn;
    private String weekday, startTime, course, semester, section, subject, teacher, roomNumber;
    private String selectedCategoryWeekday;
    private String selectedCategoryCourse;
    private String selectedCategorySemester;
    private String selectedCategorySection;
    private ProgressDialog progressBar;
    StorageReference storageReference;
    DatabaseReference reference, dbRef;
    ArrayAdapter<String> categoryWeekdaysAdapter,categoryCoursesAdapter,categorySemestersAdapter,categorySectionsAdapter,schoolOfNursing,schoolOfEngineering,schoolOfManagement,schoolOfHumanities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

        toolbar=findViewById(R.id.addTimetableToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addTimetableLayout=findViewById(R.id.addTimetableLayout);

        addTimetableSubject=findViewById(R.id.addTimetableSubject);
        addTimetableTeacher=findViewById(R.id.addTimetableTeacher);
        addTimetableRoom=findViewById(R.id.addTimetableRoom);

        addTimetableDaysCat=findViewById(R.id.addTimetableDayCat);
        addTimetableCourseCat=findViewById(R.id.addTimetableCourseCat);
        addTimetableSemesterCat=findViewById(R.id.addTimetableSemesterCat);
        addTimetableSectionCat=findViewById(R.id.addTimetableSectionCat);

        addLectureBtn=findViewById(R.id.addTimetableLecture);

        reference = FirebaseDatabase.getInstance().getReference().child("Timetable");
        storageReference = FirebaseStorage.getInstance().getReference();

        addLectureTime=findViewById(R.id.addLectureTime);
        addLectureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        String[] categoryDay = getResources().getStringArray(R.array.Weekdays);
        categoryWeekdaysAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, categoryDay);
        addTimetableDaysCat.setAdapter(categoryWeekdaysAdapter);

        addTimetableDaysCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addTimetableCourseCat=findViewById(R.id.addTimetableCourseCat);

                selectedCategoryWeekday=categoryWeekdaysAdapter.getItem(i);
                Snackbar.make(addTimetableLayout, selectedCategoryWeekday, Snackbar.LENGTH_SHORT).show();

                switch (selectedCategoryWeekday){
                    case "Monday":
                    case "Tuesday":
                    case "Wednesday":
                    case "Thursday":
                    case "Friday":
                        String[] categoryCourses = getResources().getStringArray(R.array.Courses);
                        categoryCoursesAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categoryCourses );
                        addTimetableCourseCat.setAdapter(categoryCoursesAdapter);

                        addTimetableCourseCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                addTimetableSemesterCat=findViewById(R.id.addTimetableSemesterCat);

                                selectedCategoryCourse=categoryCoursesAdapter.getItem(i);
                                Snackbar.make(addTimetableLayout, selectedCategoryCourse, Snackbar.LENGTH_SHORT).show();

                                String[] categorySemester = getResources().getStringArray(R.array.Semesters);
                                categorySemestersAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categorySemester );

                                addTimetableSemesterCat.setAdapter(categorySemestersAdapter);

                                addTimetableSemesterCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        addTimetableSectionCat=findViewById(R.id.addTimetableSectionCat);

                                        selectedCategorySemester=categorySemestersAdapter.getItem(i);
                                        Snackbar.make(addTimetableLayout, selectedCategorySemester, Snackbar.LENGTH_SHORT).show();

                                        String[] categorySection = getResources().getStringArray(R.array.Sections);
                                        categorySectionsAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item,categorySection );

                                        addTimetableSectionCat.setAdapter(categorySectionsAdapter);

                                        addTimetableSectionCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                selectedCategorySection=categorySectionsAdapter.getItem(i);
                                                Snackbar.make(addTimetableLayout, selectedCategorySection, Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                            }
                        });
                        break;
                    default:break;
                }
            }
        });
        addLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        weekday=selectedCategoryWeekday;
        course=selectedCategoryCourse;
        semester=selectedCategorySemester;
        section=selectedCategorySection;
        startTime=addLectureTime.getText().toString();
        subject=addTimetableSubject.getText().toString().trim();
        teacher=addTimetableTeacher.getText().toString().trim();
        roomNumber=addTimetableRoom.getText().toString().trim();

        if(startTime.isBlank()){
            addLectureTime.setError("Empty");
            addLectureTime.requestFocus();
        } else if (subject.isBlank()) {
            addTimetableSubject.setError("Empty");
            addTimetableSubject.requestFocus();
        } else if (teacher.isBlank()) {
            addTimetableTeacher.setError("Empty");
            addTimetableTeacher.requestFocus();
        } else if (roomNumber.isBlank()) {
            addTimetableRoom.setError("Empty");
            addTimetableRoom.requestFocus();
        } else if (weekday==null) {
            Snackbar.make(addTimetableLayout, "Please Provide Weekday", Snackbar.LENGTH_SHORT).show();
        } else if (course==null) {
            Snackbar.make(addTimetableLayout, "Please Provide Course", Snackbar.LENGTH_SHORT).show();
        } else if (semester==null) {
            Snackbar.make(addTimetableLayout, "Please Provide Semester", Snackbar.LENGTH_SHORT).show();
        } else if (section == null) {
            Snackbar.make(addTimetableLayout, "Please Provide Section", Snackbar.LENGTH_SHORT).show();
        } else {
            insertData();
        }
    }

    private void insertData() {
        dbRef=reference.child(selectedCategoryWeekday).child(selectedCategoryCourse).child(selectedCategorySemester).child(selectedCategorySection);
        final String uniqueKey = dbRef.push().getKey();

        String key = uniqueKey;

        Lecture lectureData = new Lecture(startTime, subject, teacher, roomNumber, key);

        dbRef.child(key).setValue(lectureData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Snackbar.make(addTimetableLayout, "Lecture Added Successfully", Snackbar.LENGTH_SHORT).show();
                addTimetableDaysCat.setText(null);
                addLectureTime.setText(null);
                addTimetableCourseCat.setText(null);
                addTimetableSemesterCat.setText(null);
                addTimetableSectionCat.setText(null);
                addTimetableSubject.setText(null);
                addTimetableTeacher.setText(null);
                addTimetableRoom.setText(null);
                selectedCategoryWeekday=null;
                selectedCategoryCourse=null;
                selectedCategorySemester=null;
                selectedCategorySection=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Snackbar.make(addTimetableLayout, "Something Went Wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTimetable.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                int hour = selectedHour % 12;
                if (hour == 0)
                    hour = 12;
                addLectureTime.setText(String.format(Locale.getDefault(),"%02d:%02d %s", hour, selectedMinute,
                        selectedHour < 12 ? "AM" : "PM"));
            }
        }, hour, minute, false);
        timePickerDialog.setTitle("Select Lecture Start Time");
        timePickerDialog.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}