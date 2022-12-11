package com.example.myapplication3.level_activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication3.Array;
import com.example.myapplication3.GameLevels;
import com.example.myapplication3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Level2 extends AppCompatActivity {

    Dialog dialog;
    Dialog dialogEnd;

    public int numLeft;
    public int numRight;
    Array array = new Array();
    Random random = new Random();
    public int count = 0;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);

        TextView textLevels = findViewById(R.id.textV_level);
        textLevels.setText(R.string.level2);

        final ImageView IMG_LEFT = findViewById(R.id.image_left);
        IMG_LEFT.setClipToOutline(true);
        final ImageView IMG_RIGHT = findViewById(R.id.image_right);
        IMG_RIGHT.setClipToOutline(true);


        final TextView TEXT_LEFT = findViewById(R.id.text_left);
        final TextView TEXT_RIGHT = findViewById(R.id.text_right);



        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.previewdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageView previewing = (ImageView) dialog.findViewById(R.id.preview_img);
        previewing.setImageResource(R.drawable.l2_choise);
        TextView textDescription = dialog.findViewById(R.id.text_description);
        textDescription.setText(R.string.level_two);

        TextView btnClose = (TextView) dialog.findViewById(R.id.btnclose);
        btnClose.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Level2.this, GameLevels.class);
                startActivity(intent);
                finish();
            } catch (Exception exception) {

            }
            dialog.dismiss();
        });
        Button btnContinue = (Button) dialog.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        //____________________________________
        //Level end
        dialogEnd = new Dialog(this);
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEnd.setContentView(R.layout.dialog_end);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEnd.setCancelable(false);

        TextView textDescriptionEnd = dialogEnd.findViewById(R.id.text_description_end);
        textDescriptionEnd.setText(R.string.leve_two_end);

        TextView btnClose2 = (TextView) dialogEnd.findViewById(R.id.btn_close_end);
        btnClose2.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Level2.this, GameLevels.class);
                startActivity(intent);
                finish();
            } catch (Exception exception) {

            }
            dialogEnd.dismiss();
        });
        Button btnContinue2 = (Button) dialogEnd.findViewById(R.id.btn_continue);
        btnContinue2.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Level2.this, Level2.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {

            }
            dialogEnd.dismiss();
        });

        Button btnBack = (Button) findViewById(R.id.button_back_2);
        btnBack.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Level2.this, GameLevels.class);
                startActivity(intent);
                finish();
            } catch (Exception exception) {

            }
        });

        //arr progress
        final int[] progress = {
                R.id.point1, R.id.point2, R.id.point3, R.id.point4, R.id.point5, R.id.point6, R.id.point7, R.id.point8,
                R.id.point9, R.id.point10, R.id.point11, R.id.point12, R.id.point13, R.id.point14, R.id.point15,
                R.id.point16, R.id.point17, R.id.point18, R.id.point19, R.id.point20
        };


        //animation for check
        final Animation a = AnimationUtils.loadAnimation(Level2.this, R.anim.alpha);


        //img gen
        numLeft = random.nextInt(10);// rand num
        IMG_LEFT.setImageResource(array.historicalFigureDTOS[numLeft].getImage());
        TEXT_LEFT.setText(array.historicalFigureDTOS[numLeft].getName());

        numRight = random.nextInt(10);// rand num

        while (numLeft == numRight) {
            numRight = random.nextInt(10);// rand num
        }
        IMG_RIGHT.setImageResource(array.historicalFigureDTOS[numRight].getImage());
        TEXT_RIGHT.setText(array.historicalFigureDTOS[numRight].getName());


        //image press
        IMG_LEFT.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                IMG_RIGHT.setEnabled(false);
                if (array.historicalFigureDTOS[numLeft].getYer() > array.historicalFigureDTOS[numRight].getYer()) {
                    IMG_LEFT.setImageResource(R.drawable.check_mark);
                } else {
                    IMG_LEFT.setImageResource(R.drawable.cross);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //finger out
                if (array.historicalFigureDTOS[numLeft].getYer() > array.historicalFigureDTOS[numRight].getYer()) {
                    if (count < 20) {
                        count++;
                    }
                    //all to gray color
                    for (int i = 0; i < 20; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    //to green color
                    for (int i = 0; i < count; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }


                } else {
                    if (count > 0) {
                        if (count == 1) {
                            count = 0;
                        } else {
                             count=count-2;
                        }
                    }
                    for (int i = 0; i < 19; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    //to green color
                    for (int i = 0; i < count; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }
                }
                //left from LEVEL
                if (count == 20) {
                    final int[] i = {20};
                    final Long[] points = new Long[1];
                    myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                HashMap m3 = (HashMap) ds.getValue();
                                points[0] = (Long) m3.get("value");
                                Map m = new HashMap();
                                if (points[0] != null) {
                                    m.put("value", points[0] + i[0]);
                                    myRef.child(user.getUid()).child("POINTS").updateChildren(m);
                                    i[0] = 0;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    dialogEnd.show();
                } else {
                    numLeft = random.nextInt(10);// rand num
                    IMG_LEFT.setImageResource(array.historicalFigureDTOS[numLeft].getImage());
                    IMG_LEFT.startAnimation(a);
                    TEXT_LEFT.setText(array.historicalFigureDTOS[numLeft].getName());

                    numRight = random.nextInt(10);// rand num

                    while (numLeft == numRight) {
                        numRight = random.nextInt(10);// rand num
                    }
                    IMG_RIGHT.setImageResource(array.historicalFigureDTOS[numRight].getImage());
                    IMG_RIGHT.startAnimation(a);
                    TEXT_RIGHT.setText(array.historicalFigureDTOS[numRight].getName());

                    IMG_RIGHT.setEnabled(true);
                }
            }
            return true;
        });


        //rigt img
        IMG_RIGHT.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                IMG_LEFT.setEnabled(false);
                if (array.historicalFigureDTOS[numLeft].getYer() < array.historicalFigureDTOS[numRight].getYer()) {
                    IMG_RIGHT.setImageResource(R.drawable.check_mark);
                } else {
                    IMG_RIGHT.setImageResource(R.drawable.cross);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //finger out
                if (array.historicalFigureDTOS[numLeft].getYer() < array.historicalFigureDTOS[numRight].getYer()) {
                    if (count < 20) {
                        count++;
                    }
                    //all to gray color
                    for (int i = 0; i < 20; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    //to green color
                    for (int i = 0; i < count; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }


                } else {
                    if (count > 0) {
                        if (count == 1) {
                            count = 0;
                        } else {
                            count=count-2;
                        }
                    }
                    for (int i = 0; i < 19; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    //to green color
                    for (int i = 0; i < count; i++) {
                        TextView tv = findViewById(progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }
                }
                //left from LEVEL
                if (count == 20) {
                    final int[] i = {20};
                    final Long[] points = new Long[1];
                    myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                HashMap m3 = (HashMap) ds.getValue();
                                points[0] = (Long) m3.get("value");
                                Map m = new HashMap();
                                if (points[0] != null) {
                                    m.put("value", points[0] + i[0]);
                                    myRef.child(user.getUid()).child("POINTS").updateChildren(m);
                                    i[0] = 0;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    dialogEnd.show();
                } else {
                    numLeft = random.nextInt(10);// rand num
                    IMG_LEFT.setImageResource(array.historicalFigureDTOS[numLeft].getImage());
                    IMG_LEFT.startAnimation(a);
                    TEXT_LEFT.setText(array.historicalFigureDTOS[numLeft].getName());

                    numRight = random.nextInt(10);// rand num

                    while (numLeft == numRight) {
                        numRight = random.nextInt(10);// rand num
                    }
                    IMG_RIGHT.setImageResource(array.historicalFigureDTOS[numRight].getImage());
                    IMG_RIGHT.startAnimation(a);
                    TEXT_RIGHT.setText(array.historicalFigureDTOS[numRight].getName());

                    IMG_LEFT.setEnabled(true);
                }
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(Level2.this, GameLevels.class);
            startActivity(intent);
            finish();
        } catch (Exception exception) {

        }
    }
}