package com.example.ezbilllite;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
//    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.userName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
//        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.loginButton);
        mLoginBtn = findViewById(R.id.loginRegisterButton);

//        fAuth = FirebaseAuth.getInstance();

//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        mRegisterBtn.setOnClickListener((v) -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        });
//            @Override
//            public void onClick(View v){
//            String email = mEmail.getText().toString().trim();
//            String password = mPassword.getText().toString().trim();
//
//            if (TextUtils.isEmpty(email)) {
//                mEmail.setError("Email is Required.");
//                return;
//            }
//
//            if (TextUtils.isEmpty(password)) {
//                mPassword.setError("Password is Required.");
//                return;
//            }
//
//            if (password.length() < 6) {
//                mPassword.setError("Password must be >= 6 characters");
//                return;
//            }
//
//            progressBar.setVisibility(View.VISIBLE);
//
//
//            //register user in Firebase
//            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//                } else {
//                    Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//
////                    }
//            });
////            }
//
//        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }

        });
    }
}