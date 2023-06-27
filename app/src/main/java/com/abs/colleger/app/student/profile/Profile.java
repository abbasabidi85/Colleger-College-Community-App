package com.abs.colleger.app.student.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abs.colleger.app.auth.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

import com.abs.colleger.app.R;
import com.abs.colleger.app.auth.signin.Login;

public class Profile extends Fragment {

    TelephonyManager telephonyManager;
    TextView userName, userEnrollment, userEmail, userPhone, userCourse, userSemester, userDot;
    Button logoutUser;
    String name, enrollment, phone, phoneQuery, email, course, semester, role;
    String phoneNumberWithoutCountryCode;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MaterialToolbar toolbar;
    MaterialCardView enrollmentID;
    ImageView dotProfile;

    private final static String TAG = "ProfileActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        SessionManager sessionManager = new SessionManager(getContext());
        dotProfile=view.findViewById(R.id.dotProfile);
        userDot=view.findViewById(R.id.dotText);
        enrollmentID=view.findViewById(R.id.enrollmentID);
        userName=view.findViewById(R.id.userName);
        userEnrollment=view.findViewById(R.id.userEnrollment);
        userEmail=view.findViewById(R.id.userEmail);
        userPhone=view.findViewById(R.id.userPhone);
        userCourse=view.findViewById(R.id.userCourse);
        userSemester=view.findViewById(R.id.userSemester);
        logoutUser=view.findViewById(R.id.userLogout);

        telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);

        if(mAuth!=null){
            FirebaseUser mAuthCurrentUser= mAuth.getCurrentUser();

            name=mAuthCurrentUser.getDisplayName();
            phone=mAuthCurrentUser.getPhoneNumber();
            email=mAuthCurrentUser.getEmail();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference().child("Users");

            String phoneNumberWithCountryCode = mAuthCurrentUser.getPhoneNumber();

            String defaultCountryIso = telephonyManager.getNetworkCountryIso(); // replace with your default country code
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberWithCountryCode, defaultCountryIso);
                phoneNumberWithoutCountryCode = String.valueOf(phoneNumber.getNationalNumber());
                Log.d(TAG, "Phone number without country code: " + phoneNumberWithoutCountryCode);
            } catch (NumberParseException e) {
                Log.e(TAG, "Error parsing phone number: " + e.getMessage());
            }

            Query query = userRef.orderByChild("id").equalTo(phoneNumberWithoutCountryCode);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        phoneQuery=phoneNumberWithoutCountryCode;
                        course=snapshot.child(phoneNumberWithoutCountryCode).child("course").getValue(String.class);
                        semester=snapshot.child(phoneNumberWithoutCountryCode).child("semester").getValue(String.class);
                        enrollment=snapshot.child(phoneNumberWithoutCountryCode).child("enrollment").getValue(String.class);
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                role = dataSnapshot.child(phoneQuery).child("role").getValue(String.class);
                                if (role != null) {
                                    if (role.equals("admin")) {
                                        // User is an admin
                                        // Do something
                                        dotProfile.setVisibility(View.VISIBLE);
                                        userSemester.setVisibility(View.VISIBLE);
                                        userSemester.setText("Admin");
                                        userName.setText(name);
                                        userEmail.setText(email);
                                        userPhone.setText(phone);

                                    } else if (role.equals("user")) {
                                        // User is a regular user
                                        // Do something else
                                        course=snapshot.child(phoneNumberWithoutCountryCode).child("course").getValue(String.class);
                                        semester=snapshot.child(phoneNumberWithoutCountryCode).child("semester").getValue(String.class);
                                        enrollment=snapshot.child(phoneNumberWithoutCountryCode).child("enrollment").getValue(String.class);
                                        userCourse.setVisibility(View.VISIBLE);
                                        userDot.setVisibility(View.VISIBLE);
                                        userSemester.setVisibility(View.VISIBLE);
                                        enrollmentID.setVisibility(View.VISIBLE);
                                        userName.setText(name);
                                        userEnrollment.setText(enrollment);
                                        userEmail.setText(email);
                                        userPhone.setText(phone);
                                        userCourse.setText(course);
                                        userSemester.setText(semester);
                                    } else {
                                        // Role is not recognized
                                        // Handle the error appropriately
                                        Toast.makeText(getContext(), "User role is undefined", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error appropriately
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Error fetching profile details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                }
            });

            logoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setIcon(R.drawable.ic_signout)
                            .setTitle("Logout")
                            .setMessage(
                                    "Are you sure want to log out?")
                            .setCancelable(true)
                            .setPositiveButton(
                                    "OK",
                                    (dialogInterface, i) -> {
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        mAuth.signOut();
                                        sessionManager.clearSession();
                                        Toast.makeText(getContext(), "You have logged out successfully", Toast.LENGTH_SHORT).show();
                                        Intent intentSignout =new Intent(getContext(), Login.class);
                                        intentSignout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intentSignout);
                                        getActivity().finish();
                                    }
                            )
                            .setNegativeButton(
                                    "CANCEL",
                                    (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.cancel()
                            ).show();
                }
            });
        }
        return view;
    }
}