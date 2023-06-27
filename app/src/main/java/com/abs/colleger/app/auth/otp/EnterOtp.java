package com.abs.colleger.app.auth.otp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.abs.colleger.app.R;
import com.abs.colleger.app.auth.register.Register;

public class EnterOtp extends AppCompatActivity {

    private final static String TAG = "EnterOtp Activity";
    TextView tvMobile, tvReOtp, tvInvalidOTP;

    TextInputEditText c1,c2,c3,c4,c5,c6;
    ProgressBar verifyOtpProgressBar;
    Button submitOtpBtn;
    String email, name, countryCode, verificationID, phone,enrollment, c,o,d,e,f,g;
    String code;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference().child("Users");


        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);
        c3=findViewById(R.id.c3);
        c4=findViewById(R.id.c4);
        c5=findViewById(R.id.c5);
        c6=findViewById(R.id.c6);

        editTextInput();

        name=getIntent().getStringExtra("name");
        countryCode = getIntent().getStringExtra("countryCode");
        verificationID = getIntent().getStringExtra("verificationID");
        phone = getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        enrollment=getIntent().getStringExtra("enrollment");

        tvMobile=findViewById(R.id.tvMobileNo);
        tvReOtp=findViewById(R.id.textViewReOTP);
        tvInvalidOTP=findViewById(R.id.tvInvalidOtp);

        tvMobile.setText(String.format(countryCode + "%s",phone));

        submitOtpBtn = findViewById(R.id.submitOtpButton);
        verifyOtpProgressBar=findViewById(R.id.submitOtpProgressBar);

        submitOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOtpBtn.setVisibility(View.GONE);
                verifyOtpProgressBar.setVisibility(View.VISIBLE);

                if (validateOtp()){
                    Intent intent = new Intent(EnterOtp.this, Register.class);
                    intent.putExtra("verificationID", verificationID);
                    intent.putExtra("code", code);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("enrollment",enrollment);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        tvReOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resend otp
            }
        });
    }

    private void editTextInput() {
        c1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        c2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        c3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        c4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        c5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    private boolean validateOtp() {
        c=c1.getText().toString().trim();
        o=c2.getText().toString().trim();
        d=c3.getText().toString().trim();
        e=c4.getText().toString().trim();
        f=c5.getText().toString().trim();
        g=c6.getText().toString().trim();

        if (c.isEmpty() || o.isEmpty() || d.isEmpty() || e.isEmpty() || f.isEmpty() || g.isEmpty()){
            tvInvalidOTP.setVisibility(View.VISIBLE);
            verifyOtpProgressBar.setVisibility(View.GONE);
            submitOtpBtn.setVisibility(View.VISIBLE);
            return false;
        } else {
            if (verificationID != null)
                code=c+o+d+e+f+g;
            return true;
        }
    }
}