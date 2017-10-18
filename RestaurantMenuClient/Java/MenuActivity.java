package com.franks.restaurantmenuclient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


// Digital menu -> show a recycler view of available dishes
public class MenuActivity extends AppCompatActivity {

    private RecyclerView myDishList;
    private DatabaseReference myDBRef;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener myAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        myDishList = (RecyclerView) findViewById(R.id.dishList);
        myDishList.setHasFixedSize(true);
        myDishList.setLayoutManager(new LinearLayoutManager(this));

        myDBRef = FirebaseDatabase.getInstance().getReference().child("Dish");

        myAuth = FirebaseAuth.getInstance();
        myAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MenuActivity.this, MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(myAuthListener);

        FirebaseRecyclerAdapter<Dish, DishViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Dish, DishViewHolder>(
                Dish.class, R.layout.singlemenuitem, DishViewHolder.class, myDBRef
        ) {
            @Override
            protected void populateViewHolder(DishViewHolder viewHolder, Dish model, int position) {
                viewHolder.dishName.setText(model.getDishName());
                viewHolder.dishDetails.setText(model.getDishDetails());
                viewHolder.dishPrice.setText(model.getDishPrice());
                Picasso.with(getApplicationContext()).load(model.getDishImage()).fit().into(viewHolder.dishImage);

                // Add onclick listener to the list item, when one is clicked, direct user to SingleDishActivity
                final String dishKey = getRef(position).getKey().toString();
                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleDish = new Intent(MenuActivity.this, SingleDishActivity.class);
                        singleDish.putExtra("DishId", dishKey);
                        startActivity(singleDish);
                    }
                });
            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        myDishList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder{
        View myView;
        TextView dishName;
        TextView dishDetails;
        TextView dishPrice;
        ImageView dishImage;

        public DishViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            dishName = (TextView) itemView.findViewById(R.id.dishName);
            dishDetails = (TextView) itemView.findViewById(R.id.dishDetails);
            dishPrice = (TextView) itemView.findViewById(R.id.dishPrice);
            dishImage = (ImageView) itemView.findViewById(R.id.dishImage);
        }
    }

}
