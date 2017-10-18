package com.franks.restaurantmenuclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

// When user hit one dish in the menu, he/she will be direct to this screen to see details of this specific dish.
public class SingleDishActivity extends AppCompatActivity {

    private String singleDishKey = null;
    private TextView singleDishName, singleDishDetails, singleDishPrice;
    private ImageView singleDishImage;
    private Button orderButton;

    private DatabaseReference myDBRef;
    private FirebaseAuth myAuth;
    private FirebaseUser curUser;
    private DatabaseReference userData;
    private DatabaseReference myOrdersRef;

    private String dishName;
    private String dishDetails;
    private String dishPrice;
    private String dishImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dish);

        singleDishKey = getIntent().getExtras().getString("DishId");

        singleDishName = (TextView) findViewById(R.id.singleName);
        singleDishDetails = (TextView) findViewById(R.id.singleDetails);
        singleDishPrice = (TextView) findViewById(R.id.singlePrice);
        singleDishImage = (ImageView) findViewById(R.id.singleImage);

        myDBRef = FirebaseDatabase.getInstance().getReference().child("Dish");
        myAuth = FirebaseAuth.getInstance();

        curUser = myAuth.getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("Users").child(curUser.getUid());
        myOrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        myDBRef.child(singleDishKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dishName = dataSnapshot.child("dishName").getValue().toString();
                dishDetails = dataSnapshot.child("dishDetails").getValue().toString();
                dishPrice = dataSnapshot.child("dishPrice").getValue().toString();
                dishImage = dataSnapshot.child("dishImage").getValue().toString();

                singleDishName.setText(dishName);
                singleDishDetails.setText(dishDetails);
                singleDishPrice.setText(dishPrice);
                Picasso.with(SingleDishActivity.this).load(dishImage).into(singleDishImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Add current dish to order
    public void addItemClicked(View view){
        final DatabaseReference newOrder = myOrdersRef.push();
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newOrder.child("dishname").setValue(dishName);

                // After user order one dish item, he/she may need to add another item
                newOrder.child("username").setValue(dataSnapshot.child("Username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent backToMenu = new Intent(SingleDishActivity.this, MenuActivity.class);
                        startActivity(backToMenu);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
