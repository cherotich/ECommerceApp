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
import com.rey.material.widget.CheckBox;
//import com.rey.material.widget.EditText;
import android.widget.EditText;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private Button LoginBtn;
    private EditText InputPhoneNumber,InputPassword;
    private ProgressDialog LoadingBar;
    private String ParentDbName="Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginBtn=(Button) findViewById(R.id.login_btn);
        InputPhoneNumber=(EditText) findViewById(R.id.login_phone_no_input);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        LoadingBar= new ProgressDialog(LoginActivity.this );
        chkBoxRememberMe=(CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();
             if(TextUtils.isEmpty(phone)){

                Toast.makeText(getApplicationContext() , "Please enter your phone number", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(password)){

                Toast.makeText(getApplicationContext() , "Please enter your password", Toast.LENGTH_SHORT).show();
            }
            else
             {
                 LoadingBar.setTitle("Login account");
                 LoadingBar.setMessage("Please wait while we are checking the credentials");
                 LoadingBar.setCanceledOnTouchOutside(false);
                 LoadingBar.show();
                AllowAccessToAccount(phone,password);
             }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if (chkBoxRememberMe.isChecked()){

            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if (dataSnapshot.child(ParentDbName).child(phone).exists()){
Users usersData= dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);
if (usersData.getPhone().equals(phone)){
    if (usersData.getPassword().equals(password)){
        Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
    LoadingBar.dismiss();
        Intent intent= new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
    }
    else{
        LoadingBar.dismiss();
        Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
    }
}

}
else
{
    Toast.makeText(getApplicationContext(), "Account with this" + phone +"do not exist", Toast.LENGTH_SHORT).show();
LoadingBar.dismiss();
    Intent intent= new Intent(LoginActivity.this,LoginActivity.class);
    startActivity(intent);
}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
