package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String categoryName;
    private Button addNewProductBtton;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private ImageView InputProductImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        categoryName = getIntent().getExtras().get("category").toString();
        addNewProductBtton = (Button) findViewById(R.id.Add_new_product);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
//        Toast.makeText(getApplicationContext(), categoryName, Toast.LENGTH_SHORT).show();

    }
}
