package com.abs.colleger.app.auth.otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.abs.colleger.app.R;
import com.abs.colleger.app.auth.signin.Login;

public class PhoneVerification extends AppCompatActivity {

    TextView loginTv;
    TextInputLayout otpCountryLayout, otpPhoneLayout;
    TextInputEditText editTextOtpCountry, editTextOtpPhone;
    Button sendOtpBtn;
    ProgressBar otpSendProgressBar;
    String otpCountryCode, country, otpPhoneNumber, email, name, enrollment;

    private final static String TAG = "PhoneVerification Activity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference().child("Users");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        CountryCodePicker countryCodePicker = findViewById(R.id.otpCountry);
        otpCountryCode = "+" + countryCodePicker.getSelectedCountryCode().trim();

        otpPhoneLayout=findViewById(R.id.otpPhoneLayout);

        editTextOtpPhone=findViewById(R.id.otpPhone);

        otpSendProgressBar=findViewById(R.id.otpSendProgressBar);

        editTextOtpPhone.addTextChangedListener(phoneTextWatcher);

        loginTv=findViewById(R.id.loginTv);
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneVerification.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        sendOtpBtn=findViewById(R.id.sendOtpButton);
        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()==true){
                    new MaterialAlertDialogBuilder(PhoneVerification.this)
                            .setTitle("Please check your details")
                            .setTitle(
                                    otpCountryCode+" "+otpPhoneNumber )
                            .setMessage("Have you entered the correct phone number?")
                            .setCancelable(true)
                            .setPositiveButton(
                                    "YES",
                                    (dialogInterface, i) -> {
                                        Query query = userRef.orderByChild("id").equalTo(otpPhoneNumber);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    String phone = snapshot.child(otpPhoneNumber).child("id").getValue(String.class);
                                                    enrollment = snapshot.child(otpPhoneNumber).child("enrollment").getValue(String.class);
                                                    email = snapshot.child(otpPhoneNumber).child("email").getValue(String.class);
                                                    name = snapshot.child(otpPhoneNumber).child("name").getValue(String.class);
                                                    if (otpPhoneNumber.equals(phone)) {
                                                        sendOtp();
                                                    }else{
                                                        Toast.makeText(PhoneVerification.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    // The phone does not exist in the database
                                                    // Show an error message or take other appropriate action
                                                    Toast.makeText(PhoneVerification.this, "Phone number not found in records", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(PhoneVerification.this, "Database error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                            )
                            .setNegativeButton(
                                    "EDIT",
                                    (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.cancel()
                            ).show();

                }
            }
        });


    }

    private void sendOtp() {
        otpSendProgressBar.setVisibility(View.VISIBLE);
        sendOtpBtn.setVisibility(View.GONE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneVerification.this, "Invalid OTP" , Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(PhoneVerification.this, "Maximum OTP quota exceeded, please try again after 24 hours.", Toast.LENGTH_SHORT).show();
                }else if (e instanceof FirebaseAuthUserCollisionException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(PhoneVerification.this, "User already registered", Toast.LENGTH_SHORT).show();
                }
                otpSendProgressBar.setVisibility(View.GONE);
                sendOtpBtn.setVisibility(View.VISIBLE);
                Toast.makeText(PhoneVerification.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                otpSendProgressBar.setVisibility(View.GONE);
                sendOtpBtn.setVisibility(View.VISIBLE);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Intent intent = new Intent(PhoneVerification.this, EnterOtp.class);
                intent.putExtra("countryCode", otpCountryCode);
                intent.putExtra("name", name);
                intent.putExtra("phone", otpPhoneNumber);
                intent.putExtra("verificationID", verificationId);
                intent.putExtra("email", email);
                intent.putExtra("enrollment",enrollment);

                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(otpCountryCode + otpPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            otpPhoneLayout.setError(null);
            otpPhoneLayout.setErrorEnabled(false);
            otpPhoneLayout.setCounterEnabled(true);

        }
    };

    private boolean validateData() {
        if(validatePhone())
            return true;
        else return false;
    }


    private boolean validatePhone() {
        otpPhoneNumber= Objects.requireNonNull(editTextOtpPhone.getText()).toString().trim();

        if (otpPhoneNumber.isEmpty()){
            otpPhoneLayout.setError("Phone no. should not be empty");
            return false;
        } else if (otpPhoneNumber.length() != 10) {
            otpPhoneLayout.setError("Phone no. must be 10 digits long");
            return false;
        } else {
            otpPhoneLayout.setError(null);
            return true;
        }
    }
}