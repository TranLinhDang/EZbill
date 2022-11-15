package com.example.ezbilllite;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateAccount;
//    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.userName);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginButton);
        mCreateAccount = findViewById(R.id.createAccountButton);

//        fAuth = FirebaseAuth.getInstance();


        mLoginBtn.setOnClickListener((v) -> {
            startActivity(new Intent(getApplicationContext(), ActivityServices.class));

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




            //Signin user in Firebase
//            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//                } else {
//                    Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//
////                    }
//            });
////            }
//
//        });
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
//
//
        });
    }
}

