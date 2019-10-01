package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerceapp.Models.Users;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button JoinNowBtn,LoginBtn;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JoinNowBtn=(Button) findViewById(R.id.main_join_now_btn);
        LoginBtn=(Button) findViewById(R.id.main_login_btn);
        LoadingBar= new ProgressDialog(MainActivity.this );
        Paper.init(this);
      LoginBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent= new Intent(MainActivity.this,LoginActivity.class);
              startActivity(intent);
          }
      });

      JoinNowBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent= new Intent(MainActivity.this,RegisterActivity.class);
              startActivity(intent);
          }
      });

      String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
      String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);
      if (UserPhoneKey!="" && UserPasswordKey!=""){
          if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
          {
              AllowAccess(UserPhoneKey, UserPasswordKey);

              LoadingBar.setTitle("Already logged in");
              LoadingBar.setMessage("Please wait..");
              LoadingBar.setCanceledOnTouchOutside(false);
              LoadingBar.show();
          }
      }
    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){
                    Users usersData= dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                            Intent intent= new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Account with this" + phone +"do not exist", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                    Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
