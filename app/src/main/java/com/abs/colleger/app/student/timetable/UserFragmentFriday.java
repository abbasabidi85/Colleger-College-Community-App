package com.abs.colleger.app.student.timetable;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.colleger.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.List;

public class UserFragmentFriday extends Fragment {

    NestedScrollView lecture;
    LinearLayout lectureShimmer;
    private final static String TAG = "FridayFragment";
    View view;
    private RecyclerView fridayLecture;
    private List<UserLecture> lectureList;
    private DatabaseReference dbRef,userRef, reference;
    private UserLectureAdapter userLectureAdapter;

    private String phone;
    private String course;
    private String semester;
    private String section;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser=mAuth.getCurrentUser();

    public UserFragmentFriday() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_friday_user,container, false);
        fridayLecture=view.findViewById(R.id.user_fridayRecyclerView);
        lectureShimmer=view.findViewById(R.id.lectureShimmerFriday);
        lecture=view.findViewById(R.id.lectureFriday);
        reference= FirebaseDatabase.getInstance().getReference().child("Timetable");
        fridayLecture();
        return view;
    }

    private void fridayLecture() {
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
                        reference=FirebaseDatabase.getInstance().getReference().child("Timetable").child("Friday");
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
                                    fridayLecture.setHasFixedSize(true);
                                    fridayLecture.setLayoutManager((new LinearLayoutManager(getContext())));
                                    userLectureAdapter=new UserLectureAdapter(getContext(),lectureList);
                                    fridayLecture.setAdapter(userLectureAdapter);
                                    lectureShimmer.setVisibility(View.GONE);
                                    lecture.setVisibility(View.VISIBLE);
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
