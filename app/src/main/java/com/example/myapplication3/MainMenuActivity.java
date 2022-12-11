package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        EditText editText = (EditText) findViewById(R.id.main_img);

        myRef = FirebaseDatabase.getInstance().getReference();

        final Long[] l = new Long[1];

            myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        HashMap m3 = (HashMap) ds.getValue();
                        editText.setText(m3.get("value").toString());
                        l[0] = (Long) m3.get("value");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        System.out.println("=============");
        System.out.println(l[0]);

        Button buttonStart = (Button) findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainMenuActivity.this, GameLevels.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {

            }
        });

        Button buttonLeave = (Button) findViewById(R.id.buttonLeaveFor);
        buttonLeave.setOnClickListener(v -> {
            try {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainMenuActivity.this, EmailPasswordActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {

            }
        });

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "press again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}