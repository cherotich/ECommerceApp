package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Models.adminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderlist;
    private DatabaseReference ordersref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersref = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderlist = findViewById(R.id.order_cart_list);
        orderlist.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<adminOrders> options =
                new FirebaseRecyclerOptions.Builder<adminOrders>()
                        .setQuery(ordersref, adminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<adminOrders, adminOrdersHolderView> adapter
                = new FirebaseRecyclerAdapter<adminOrders, adminOrdersHolderView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull adminOrdersHolderView holder, final int i, @NonNull final adminOrders model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userDateTime.setText("Order at:  " + model.getDate() + "  " + model.getTime());
                holder.userPhoneNumber.setText("Phone Number: " + model.getPhone());
                holder.UserShipppingAddress.setText("ShippingAddress: " + model.getCity() + ",  " + model.getAddress());
                holder.userTotalPrice.setText("Total amount = " + model.getTotalAmount());

                holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = getRef(i).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public adminOrdersHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);

                return new adminOrdersHolderView(view);
            }
        };
        orderlist.setAdapter(adapter);
        adapter.startListening();
    }

    public static class adminOrdersHolderView extends RecyclerView.ViewHolder {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, UserShipppingAddress;
        public Button showOrdersBtn;


        public adminOrdersHolderView(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            UserShipppingAddress = itemView.findViewById(R.id.order_address_city);
            showOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);

        }
    }
}
