package com.franks.restaurantmenuclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 * @ Created By: Frank
 * @ Date: 10/17/2017
 * @ Function: Restaurant menu for clients use
 */

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private DatabaseReference myDBRef;
    private FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPw);
        myFirebaseAuth = FirebaseAuth.getInstance();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void signUpButtonClicked(View view){
        final String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        // Create user information
        if(!TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(passwordText)){
            myFirebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String userId = myFirebaseAuth.getCurrentUser().getUid();
                        DatabaseReference curUser = myDBRef.child(userId);
                        curUser.child("Username").setValue(emailText);

                        Toast.makeText(MainActivity.this, "Sign Up Successfully!", Toast.LENGTH_LONG).show();

                        // Direct user to another activity after signing up
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }
            });
        }
    }

    public void signInButtonClicked(View view){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

}
