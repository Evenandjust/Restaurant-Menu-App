package com.franks.restaurantmenu;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// Add dish to Firebase database
public class AddDish extends AppCompatActivity {

    private ImageButton dishImage;
    private static final int GALLREQUEST = 1;
    private EditText dishName, dishDetails, dishPrice;
    private Uri uri = null;
    private StorageReference storageReference = null;
    private DatabaseReference myDBRef;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        dishName = (EditText) findViewById(R.id.dishName);
        dishDetails = (EditText) findViewById(R.id.dishDetails);
        dishPrice = (EditText) findViewById(R.id.dishPrice);

        storageReference = FirebaseStorage.getInstance().getReference();
        myDBRef = FirebaseDatabase.getInstance().getReference("Dish");

    }

    // Select an image to upload when imageButton clicked
    public void imageButtonClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLREQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLREQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            dishImage = (ImageButton) findViewById(R.id.dishImageButton);
            dishImage.setImageURI(uri);
        }
    }

    // Upload the typed in texts, such as dishName, dishDetails, dishPrice, to the database
    public void addDishToMenu(View view){
        final String dish_name = dishName.getText().toString().trim();
        final String dish_details = dishDetails.getText().toString().trim();
        final String dish_price = dishPrice.getText().toString().trim();

        if(!TextUtils.isEmpty(dish_name) && !TextUtils.isEmpty(dish_details) && !TextUtils.isEmpty(dish_price)){
            StorageReference filePath = storageReference.child(uri.getLastPathSegment());
            // To check whether it's added successfully
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(AddDish.this, "Image Uploaded!", Toast.LENGTH_LONG).show();
                    
                    // Construct nodes in database
                    final DatabaseReference newPost = myDBRef.push();  // post a new item
                    newPost.child("dishName").setValue(dish_name);
                    newPost.child("dishDetails").setValue(dish_details);
                    newPost.child("dishPrice").setValue(dish_price);
                    newPost.child("dishImage").setValue(downloadUrl.toString());
                    // Redirect to the main screen
                    startActivity(new Intent(AddDish.this, MainActivity.class));
                }
            });
        }
    }
}
