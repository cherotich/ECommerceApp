

package com.example.ecommerceapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Models.Cart;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.example.ecommerceapp.ui.product.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextprocessbtn;
    private TextView txtTotalAmount, txtMsg1;
    public int overalTotalPrice = 0;
    private String ProductId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextprocessbtn = (Button) findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtMsg1 = (TextView) findViewById(R.id.msg1);
        ProductId = getIntent().getStringExtra("pid");


        nextprocessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtTotalAmount.setText("Total price = "+ String.valueOf(overalTotalPrice));
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total_price", String.valueOf(overalTotalPrice));
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUsers.getPhone()).child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {
//                cartViewHolder.
                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText(model.getQuantity());
//                holder.txtProductQuantity.setNumber(model.getQuantity());

                holder.txtProductPrice.setText("Ksh" + model.getPrice());

                int oneTypeProductTotalPrice = (Integer.valueOf(model.getPrice())) * (Integer.valueOf(model.getQuantity()));
                overalTotalPrice = overalTotalPrice + oneTypeProductTotalPrice;
                txtTotalAmount.setText("Total price = Ksh " + String.valueOf(overalTotalPrice));


//holder.txtProductQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//    @Override
//    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//
//        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
//        final HashMap<String, Object> cartMap = new HashMap<>();
//
//
//        cartMap.put("pname",holder.txtProductName.getText().toString());
//        cartMap.put("price",holder.txtProductPrice.getText().toString());
//        cartMap.put("quantity",String.valueOf(newValue));
//
//                                             cartListRef.child("User View")
//                                             .child(Prevalent.currentOnlineUsers.getPhone())
//                                             .child("Products")
//                                             .child(ProductId)
//                                             .updateChildren(cartMap)
//                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                 @Override
//                                                 public void onComplete(@NonNull Task<Void> task) {
//
//                                                     if (task.isSuccessful()) {
//                                                         cartListRef.child("Admin View")
//                                                                 .child(Prevalent.currentOnlineUsers.getPhone())
//                                                                 .child("Products")
//                                                                 .child(ProductId)
//                                                                 .updateChildren(cartMap)
//                                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                     @Override
//                                                                     public void onComplete(@NonNull Task<Void> task) {
//
//                                                                         holder.txtProductQuantity.setNumber(model.getQuantity());
//
//                                                                             Toast.makeText(CartActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
//////                                                                             txtTotalAmount.setText("Total price = Ksh " + String.valueOf(overalTotalPrice));
////                                                                             Intent intent = new Intent(CartActivity.this, CartActivity.class);
////                                                                             startActivity(intent);
//
//
//
//
//                                                                     }
//                                                                 });
////                                                         Toast.makeText(CartActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
////                                                         txtTotalAmount.setText("Total price = Ksh " + String.valueOf(overalTotalPrice));
////                                                         Intent intent = new Intent(CartActivity.this, CartActivity.class);
////                                                         startActivity(intent);
//
//
//                                                     }
//
//                                                 }
//                                             });
//
//
//
//
//
//    }
//});


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {

                                        "Edit",
                                        "Remove"

                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);


                        builder.setTitle("Cart options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }
                                if (which == 1) {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUsers.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()


                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                                        txtTotalAmount.setText("Total price = Ksh " + String.valueOf(overalTotalPrice));
                                                        Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                        startActivity(intent);


                                                    }

                                                }
                                            });
                                    cartListRef.child("Admin View")
                                            .child(Prevalent.currentOnlineUsers.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                        startActivity(intent);


                                                    }

                                                }
                                            });
//                                     Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class );
//                                     intent.putExtra("pid", model.getPid());
//                                     startActivity(intent);

                                }
                            }
                        });

                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void checkOrderState() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUsers.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        txtTotalAmount.setText("Dear " + userName + "\n Your order of total amount = ksh " + overalTotalPrice + " has been shipped successfully");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.GONE);
                        nextprocessbtn.setVisibility(View.GONE);


                    } else if (shippingState.equals("not shipped")) {

                        txtTotalAmount.setText("Shipping state = Not shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.GONE);
                        nextprocessbtn.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
