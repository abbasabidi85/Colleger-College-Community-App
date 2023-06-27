package com.abs.colleger.app.auth.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.abs.colleger.app.admin.AdminMainActivity;
import com.abs.colleger.app.R;
import com.abs.colleger.app.student.UserMainActivity;
import com.abs.colleger.app.auth.signin.Login;

public class Register extends AppCompatActivity {
    TextInputLayout countryLayout, phoneLayout, passwordLayout, confirmPasswordLayout, emailLayout, enrollmentLayout;
    TextInputEditText editTextName,editTextCountry, editTextPhone, editTextEmail, editTextEnrollment, editTextPassword, editTextConfirmPassword;
    Button registerUserBtn;

    TextView enrollmentTv, emailTv;

    String phone, email,name, enrollment,password,confirmPassword,role;
    private final static String TAG = "Register Activity";

    String otpCountryCode;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    ProgressBar registerProgressBar;
    String verificationID,code;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerProgressBar=findViewById(R.id.registerProgressBar);
        enrollmentTv=findViewById(R.id.enrollmentTv);
        emailTv=findViewById(R.id.emailTv);

       /* countryLayout=findViewById(R.id.registerCountryLayout);
        phoneLayout=findViewById(R.id.registerPhoneLayout);
        emailLayout=findViewById(R.id.registerEmailLayout);
        enrollmentLayout=findViewById(R.id.registerEnrollmentLayout);

        editTextCountry=findViewById(R.id.registerCountry);
        editTextPhone=findViewById(R.id.registerPhone);
        editTextEmail=findViewById(R.id.registerEmail);
        editTextEnrollment=findViewById(R.id.registerEnrollment);*/

        passwordLayout=findViewById(R.id.registerPasswordLayout);
        confirmPasswordLayout=findViewById(R.id.registerConfirmPasswordLayout);
        editTextPassword=findViewById(R.id.registerPassword);
        editTextConfirmPassword=findViewById(R.id.registerConfirmPassword);

        /*editTextCountry.addTextChangedListener(countryTextWatcher);

        editTextPhone.addTextChangedListener(phoneTextWatcher);

        editTextEmail.addTextChangedListener(emailTextWatcher);

        editTextEnrollment.addTextChangedListener(enrollmentTextWatcher);*/

        editTextPassword.addTextChangedListener(passwordTextWatcher);

        editTextConfirmPassword.addTextChangedListener(confirmPasswordTextWatcher);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference().child("Users");

        name=getIntent().getStringExtra("name");
        code = getIntent().getStringExtra("code");
        verificationID = getIntent().getStringExtra("verificationID");
        phone = getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        enrollment=getIntent().getStringExtra("enrollment");

        enrollmentTv.setText(enrollment);
        emailTv.setText(email);

        registerUserBtn=findViewById(R.id.registerBtn);

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerProgressBar.setVisibility(View.VISIBLE);
                registerUserBtn.setVisibility(View.GONE);
                if(validateData()){
                    //register
                    /*if enrollment & email exists in firebase db
                        Create a user
                            if the created user's has admin role in db
                              Signin user
                              Intent to admin main activity
                            else signin user
                              intent to user main activity
                     else toast "Record not found" */
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
                                    FirebaseUser mAuthCurrentUser =mAuth.getCurrentUser();
                                    mAuthCurrentUser.updatePhoneNumber(credential);
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();
                                    mAuthCurrentUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(task1 -> {

                                            });
                                    AuthCredential emailCredential = EmailAuthProvider.getCredential(email, password);
                                    mAuthCurrentUser.reauthenticate(emailCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                userRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        role = dataSnapshot.child(phone).child("role").getValue(String.class);
                                                        if (role != null) {
                                                            if (role.equals("admin")) {
                                                                // User is an admin
                                                                // Do something

                                                                if (!mAuthCurrentUser.isEmailVerified()) {
                                                                    mAuthCurrentUser.sendEmailVerification();
                                                                    Toast.makeText(Register.this, "Admin Registered Successfully", Toast.LENGTH_LONG).show();
                                                                    Toast.makeText(Register.this, "Verification email sent, please verify your email", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(Register.this, AdminMainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Intent intent = new Intent(Register.this, AdminMainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }

                                                            } else if (role.equals("user")) {
                                                                // User is a regular user
                                                                // Do something else
                                                                if (!mAuthCurrentUser.isEmailVerified()) {
                                                                    mAuthCurrentUser.sendEmailVerification();
                                                                    Toast.makeText(Register.this, "Student Registered Successfully", Toast.LENGTH_LONG).show();
                                                                    Toast.makeText(Register.this, "Verification email sent, please verify your email", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(Register.this, UserMainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Intent intent = new Intent(Register.this, UserMainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            } else {
                                                                // Role is not recognized
                                                                // Handle the error appropriately
                                                                mAuth.signOut();
                                                                Toast.makeText(Register.this, "Unable to login", Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(Register.this, Login.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        // Handle the error appropriately
                                                        Toast.makeText(Register.this, "Unable to create user", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Authentication Failed, Please try logging in again", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Register.this, Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    try {
                                        throw task.getException();
                                    }catch (FirebaseAuthUserCollisionException e){
                                        registerProgressBar.setVisibility(View.GONE);
                                        registerUserBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(Register.this, "User already registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, Login.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "Unable to create user", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                }else {
                    registerProgressBar.setVisibility(View.GONE);
                    registerUserBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }



    private boolean validateData() {
        if(validatePassword())
            return true;
        else return false;
    }


    /*private TextWatcher countryTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            countryLayout.setError(null);
            countryLayout.setErrorEnabled(false);

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            phoneLayout.setError(null);
            phoneLayout.setErrorEnabled(false);
            phoneLayout.setCounterEnabled(true);

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);

        }
    };

    private TextWatcher enrollmentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            enrollmentLayout.setError(null);
            enrollmentLayout.setErrorEnabled(false);
            enrollmentLayout.setCounterEnabled(true);
        }
    };*/

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            confirmPasswordLayout.setError(null);
            confirmPasswordLayout.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            passwordLayout.setError(null);
            passwordLayout.setErrorEnabled(false);

        }
    };

    private TextWatcher confirmPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            confirmPasswordLayout.setHelperText(null);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            passwordLayout.setError(null);
            passwordLayout.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            confirmPasswordLayout.setError(null);
            confirmPasswordLayout.setErrorEnabled(false);

        }
    };


    /*private boolean validateCountry() {
        otpCountryCode= Objects.requireNonNull(editTextCountry.getText()).toString().trim();

        if (otpCountryCode.isEmpty()){
            countryLayout.setError("Country code should not be empty");
            return false;
        } else {
            countryLayout.setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        phone= editTextPhone.getText().toString().trim();

        if (phone.isEmpty()){
            phoneLayout.setError("Phone no. should not be empty");
            return false;
        } else if (phone.length() != 10) {
            phoneLayout.setError("Phone no. must be 10 digits long");
            return false;
        } else {
            phoneLayout.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        email= editTextEmail.getText().toString().trim();
        if (email.isEmpty()){
            emailLayout.setError("Email should not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email address");
            return false;
        }else {
            emailLayout.setError(null);
            return true;
        }
    }

    private boolean validateEnrollment() {
        enrollment= editTextEnrollment.getText().toString().trim();

        if (enrollment.isEmpty()){
            enrollmentLayout.setError("Enrollment no. should not be empty");
            return false;
        } else if (enrollment.length()<10 || enrollment.length()>10) {
            enrollmentLayout.setError("Enrollment no. must be 10 digits long");
            return false;
        } else {
            enrollmentLayout.setError(null);
            return true;
        }
    }*/

    private boolean validatePassword() {
        password= editTextPassword.getText().toString().trim();
        confirmPassword= editTextConfirmPassword.getText().toString().trim();

        if (password.isEmpty()){
           passwordLayout.setError("Password should not be empty");
            registerProgressBar.setVisibility(View.GONE);
            registerUserBtn.setVisibility(View.VISIBLE);
           return false;
        }else if (password.length()<8){
            passwordLayout.setError("Password should be at least 8 characters long");
            registerProgressBar.setVisibility(View.GONE);
            registerUserBtn.setVisibility(View.VISIBLE);
            return false;
        }else if (!password.equals(confirmPassword) && !confirmPassword.equals(password)){
            passwordLayout.setError("Password doesn't match");
            confirmPasswordLayout.setError("Password doesn't match");
            registerProgressBar.setVisibility(View.GONE);
            registerUserBtn.setVisibility(View.VISIBLE);
            return false;
        }else {
            confirmPasswordLayout.setHelperText("Password matched :)");
            passwordLayout.setError(null);
            confirmPasswordLayout.setError(null);
            return true;
        }
    }
}