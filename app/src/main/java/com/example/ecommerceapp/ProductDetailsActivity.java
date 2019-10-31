package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerceapp.Models.Products;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName, productDescription, productPrice;
    public String ProductId = "", state = "normal";
    private StorageReference ProductImageRef;
    //    private DatabaseReference ProductsRef;
//    private Uri ImageUri;
    private String downloadImageUrl;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ProductId = getIntent().getStringExtra("pid");

        addToCartBtn = (Button) findViewById(R.id.pd_add_product_to_cart);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
//        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
//    ImageUri = Uri.parse("android.resource://com.example.ecommerceapp/" + R.id.product_image_details);
//        String downloadImageUrl = ImageUri.toString();


        getProductDetails(ProductId);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (state.equals("order placed") || state.equals("order shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase more products once the order is shipped or confirmed", Toast.LENGTH_LONG).show();

                } else {
                    addToCartList();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void addToCartList() {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("pid", ProductId);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");


        cartListRef.child("User View").child(Prevalent.currentOnlineUsers.getPhone())
                .child("Products").child(ProductId).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUsers.getPhone())
                                    .child("Products").child(ProductId).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(ProductDetailsActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                            intent.putExtra("pid", ProductId);
                                            startActivity(intent);
                                        }
                                    });
                        }

                    }
                });


    }


    private void getProductDetails(String productId) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(ProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkOrderState() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUsers.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String shippingState = dataSnapshot.child("state").getValue().toString();


                    if (shippingState.equals("shipped")) {
                        state = "order shipped";


                    } else if (shippingState.equals("not shipped")) {

                        state = "order placed";

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
