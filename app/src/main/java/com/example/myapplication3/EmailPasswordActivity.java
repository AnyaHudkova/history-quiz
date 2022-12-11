package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText ETemail;
    private EditText ETpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        myAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(EmailPasswordActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        };
        ETemail = findViewById(R.id.et_email);
        ETpassword = findViewById(R.id.et_password);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);

        FirebaseUser user = myAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(EmailPasswordActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sign_in) {
            signIn(ETemail.getText().toString(), ETpassword.getText().toString());
        } else if (view.getId() == R.id.btn_registration) {
            registration(ETemail.getText().toString(), ETpassword.getText().toString());
        }
    }

    public void signIn(String eMail, String password) {
        myAuth.signInWithEmailAndPassword(eMail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EmailPasswordActivity.this, "authorization successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmailPasswordActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EmailPasswordActivity.this, "authorization not successful", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void registration(String eMail, String password) {
        myAuth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EmailPasswordActivity.this, "registration successful", Toast.LENGTH_SHORT).show();
                    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    Map m=new HashMap();
                    m.put("value",0);
                    myRef.child(user.getUid()).child("POINTS").setValue(m);
                } else {
                    Toast.makeText(EmailPasswordActivity.this, "registration not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
