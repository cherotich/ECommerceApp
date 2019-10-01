package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountBtn;
    private EditText InputName, InputPhoneNo,InputPassword;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CreateAccountBtn=(Button) findViewById(R.id.register_btn);
        InputName=(EditText) findViewById(R.id.register_username_input);
        InputPhoneNo=(EditText) findViewById(R.id.register_phone_no_input);
        InputPassword=(EditText) findViewById(R.id.register_password_input);
        LoadingBar= new ProgressDialog(RegisterActivity.this );

        CreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();

            }
        });
    }

    private void CreateAccount() {
        String name=InputName.getText().toString();
        String phone=InputPhoneNo.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){

            Toast.makeText(getApplicationContext() , "Please enter your name", Toast.LENGTH_LONG).show();
        }
       else if(TextUtils.isEmpty(phone)){

            Toast.makeText(getApplicationContext() , "Please enter your phone number", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(getApplicationContext() , "Please enter your password", Toast.LENGTH_LONG).show();
        }
        else
        {
            LoadingBar.setTitle("Create account");
            LoadingBar.setMessage("Please wait while we are checking the credentials");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            ValidatePhoneNo(name,phone,password);
        }
    }

    private void ValidatePhoneNo(final String name, final String phone, final String password) {
       final DatabaseReference RootRef;
       RootRef= FirebaseDatabase.getInstance().getReference();
       RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

if(dataSnapshot.child("Users").child(phone).exists()){
    Toast.makeText(getApplicationContext(), "This"+ phone +"already exist", Toast.LENGTH_SHORT).show();
    LoadingBar.dismiss();
    Toast.makeText(getApplicationContext(), "Please try again using another phone number", Toast.LENGTH_SHORT).show();
    Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
    startActivity(intent);

}
else
{
    HashMap<String,Object> userdataMap= new HashMap<>();
    userdataMap.put("name",name);
    userdataMap.put("phone",phone);
    userdataMap.put("password",password);
    RootRef.child("Users").child(phone).updateChildren(userdataMap)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Your account was created successfully", Toast.LENGTH_SHORT).show();
                        LoadingBar.dismiss();
                        Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        LoadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Network error please try again later", Toast.LENGTH_SHORT).show();

                    }
                }
            });

}
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }
}
