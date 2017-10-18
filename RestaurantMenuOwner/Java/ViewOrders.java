package com.franks.restaurantmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewOrders extends AppCompatActivity {

    private RecyclerView myOrdersList;
    private DatabaseReference myDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        myOrdersList = (RecyclerView) findViewById(R.id.ordersList);
        myOrdersList.setHasFixedSize(true);
        myOrdersList.setLayoutManager(new LinearLayoutManager(this));
        myDBRef = FirebaseDatabase.getInstance().getReference().child("Orders");

    }

    @Override
    protected void onStart() {
        super.onStart();
        
        // Customize recycler adapter and populate recycler view of orders
        FirebaseRecyclerAdapter<Order, OrderViewHolder> fRAdapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(
                Order.class, R.layout.singleorder, OrderViewHolder.class, myDBRef
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Order model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDishname(model.getDishname());
            }
        };

        myOrdersList.setAdapter(fRAdapter);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        View orderView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderView = itemView;
        }

        public void setUsername(String username) {
            TextView usernameText = (TextView) itemView.findViewById(R.id.orderUsername);
            usernameText.setText(username);
        }

        public void setDishname(String dishname){
            TextView dishnameText = (TextView) itemView.findViewById(R.id.orderDishname);
            dishnameText.setText(dishname);
        }
    }
}
