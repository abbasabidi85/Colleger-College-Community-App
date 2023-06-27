package com.abs.colleger.app.student.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abs.colleger.app.student.career.UserCareer;
import com.abs.colleger.app.student.club.UserClub;
import com.abs.colleger.app.student.file.UserFiles;
import com.abs.colleger.app.student.timetable.UserLecture;
import com.abs.colleger.app.student.timetable.UserLectureAdapter;
import com.abs.colleger.app.student.timetable.UserTimetableFragment;
import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.abs.colleger.app.R;
import com.abs.colleger.app.student.contact.UserContact;
import com.abs.colleger.app.student.lostfound.UserLostFound;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class HomeFragment extends Fragment {

    LinearLayout thisWeek, nextWeek;
    private final static String TAG = "HomeFragment";
    View view;
    private RecyclerView getLecture, getNextWeekLecture;
    private List<UserLecture> lectureList;
    private DatabaseReference dbRef,userRef, reference;
    private UserLectureAdapter userLectureAdapter;

    private String phone;
    private String course;
    private String semester;
    private String section;
    private String currentDay;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser=mAuth.getCurrentUser();
    TextView currentDayOfWeek;

    ImageSlider mainSlider;
    TextView todayLectures;
    MaterialCardView contactsCardView, lostFoundCardView, clubsCardView, previousQuestionCardView, careerServicesCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        reference= FirebaseDatabase.getInstance().getReference().child("Timetable");

        contactsCardView=view.findViewById(R.id.userContactsCardView);
        clubsCardView=view.findViewById(R.id.userClubsCardView);
        lostFoundCardView=view.findViewById(R.id.userLostFoundCardView);
        previousQuestionCardView=view.findViewById(R.id.userQuestionsCardView);
        careerServicesCardView=view.findViewById(R.id.userCareerCardView);

        getLecture=view.findViewById(R.id.home_recyclerView);
        todayLectures=view.findViewById(R.id.todayLectures);
        nextWeek=view.findViewById(R.id.nextWeek);
        getNextWeekLecture=view.findViewById(R.id.home_nextWeekRecyclerView);
        thisWeek=view.findViewById(R.id.thisWeek);

       int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active, set text color to white
            todayLectures.setTextColor(Color.parseColor("#C0C1C2"));

            contactsCardView.setCardBackgroundColor(Color.parseColor("#AD2C6D"));
            clubsCardView.setCardBackgroundColor(Color.parseColor("#4B3BA1"));
            lostFoundCardView.setCardBackgroundColor(Color.parseColor("#8A4AA3"));
            previousQuestionCardView.setCardBackgroundColor(Color.parseColor("#0E8F45"));
            careerServicesCardView.setCardBackgroundColor(Color.parseColor("#A67A0F"));
        } else {
            // Light mode is active, set text color to blue
            todayLectures.setTextColor(Color.parseColor("#1f5eaa"));

            contactsCardView.setCardBackgroundColor(Color.parseColor("#F7C8DB"));
            clubsCardView.setCardBackgroundColor(Color.parseColor("#D3C8F7"));
            lostFoundCardView.setCardBackgroundColor(Color.parseColor("#EEC3F7"));
            previousQuestionCardView.setCardBackgroundColor(Color.parseColor("#C6F7DE"));
            careerServicesCardView.setCardBackgroundColor(Color.parseColor("#F9E8C0"));
        }

        currentDayOfWeek=view.findViewById(R.id.currentDay);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        currentDay = dateFormat.format(calendar.getTime());
        if (currentDay.equals("Saturday") || currentDay.equals("Sunday")){
            thisWeek.setVisibility(View.GONE);
            nextWeek.setVisibility(View.VISIBLE);
            getNextWeekLecture();
        }else {
            currentDayOfWeek.setText(currentDay);
            getLecture();
        }


        //imageSlider
        /*mainSlider=view.findViewById(R.id.image_slider);
        final List<SlideModel> slideImages = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren())
                        {
                            slideImages.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));
                        }
                        mainSlider.setImageList(slideImages,ScaleTypes.FIT);
                        mainSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Toast.makeText(getContext(), "Slider Clicked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void doubleClick(int i) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/


        contactsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserContact.class));
            }
        });


        lostFoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserLostFound.class));
            }
        });

        clubsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserClub.class));
            }
        });


        careerServicesCardView=view.findViewById(R.id.userCareerCardView);
        careerServicesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserCareer.class));
            }
        });

        previousQuestionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserFiles.class));
            }
        });
        return view;
    }

    private void getNextWeekLecture() {
        if (currentUser!=null) {
            phone = getUserPhoneNumber();

            userRef=FirebaseDatabase.getInstance().getReference().child("Users");
            Query query = userRef.orderByChild("id").equalTo(phone);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        course = snapshot.child(phone).child("course").getValue(String.class);
                        semester = snapshot.child(phone).child("semester").getValue(String.class);
                        section = snapshot.child(phone).child("section").getValue(String.class);
                        reference=FirebaseDatabase.getInstance().getReference().child("Timetable").child("Monday");
                        dbRef=reference.child(course).child(semester).child(section);
                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                lectureList = new ArrayList<>();
                                if(!dataSnapshot.exists()){
                                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                                }else{
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        UserLecture data = snapshot.getValue(UserLecture.class);
                                        lectureList.add(data);
                                    }
                                    getNextWeekLecture.setHasFixedSize(true);
                                    getNextWeekLecture.setLayoutManager((new LinearLayoutManager(getContext())));
                                    userLectureAdapter=new UserLectureAdapter(getContext(),lectureList);
                                    getNextWeekLecture.setAdapter(userLectureAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Record not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            mAuth.signOut();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLecture() {
        if (currentUser!=null) {
            phone = getUserPhoneNumber();

            userRef=FirebaseDatabase.getInstance().getReference().child("Users");
            Query query = userRef.orderByChild("id").equalTo(phone);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        course = snapshot.child(phone).child("course").getValue(String.class);
                        semester = snapshot.child(phone).child("semester").getValue(String.class);
                        section = snapshot.child(phone).child("section").getValue(String.class);
                        reference=FirebaseDatabase.getInstance().getReference().child("Timetable").child(currentDay);
                        dbRef=reference.child(course).child(semester).child(section);
                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                lectureList = new ArrayList<>();
                                if(!dataSnapshot.exists()){
                                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                                }else{
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        UserLecture data = snapshot.getValue(UserLecture.class);
                                        lectureList.add(data);
                                    }
                                    getLecture.setHasFixedSize(true);
                                    getLecture.setLayoutManager((new LinearLayoutManager(getContext())));
                                    userLectureAdapter=new UserLectureAdapter(getContext(),lectureList);
                                    getLecture.setAdapter(userLectureAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Record not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            mAuth.signOut();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }
    private String getUserPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        String phoneNumberWithCountryCode = currentUser.getPhoneNumber();
        String phoneNumberWithoutCountryCode = null;
        String defaultCountryIso = telephonyManager.getNetworkCountryIso(); // replace with your default country code
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberWithCountryCode, defaultCountryIso);
            phoneNumberWithoutCountryCode = String.valueOf(phoneNumber.getNationalNumber());
        } catch (NumberParseException e) {
            Log.e(TAG, "Error parsing phone number: " + e.getMessage());
        }
        return phoneNumberWithoutCountryCode;
    }
}