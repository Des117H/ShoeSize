package com.unlucky.assignment3.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;
import com.unlucky.assignment3.utilities.DownloadImageTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    String name;
    String mainPrice;
    String style;
    String colorway;
    String releaseDay;
    String description;
    String shoeImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> shoeData = new HashMap<>();

        db.collection("shoes")
                //.whereEqualTo("name", "Air Jordan 1 Mid Light Smoke Grey")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                shoeData.putAll(document.getData());
                                name = (String)shoeData.get("name");
                                mainPrice = "$" + shoeData.get("price");
                                style = (String) shoeData.get("style");
                                colorway = (String) shoeData.get("colorway");
                                releaseDay = (String) shoeData.get("releaseDate");
                                description = (String) shoeData.get("description");
                                shoeImage = (String) shoeData.get("pictureLink");

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

//    private boolean checkAllFields() {
//        boolean checked = true;
//        if (name.getText().length() == 0) {
//            name.setError("Full name is required");
//            checked = false;
//        }
//
//        if (style.getText().length() == 0) {
//            style.setError("Full name is required");
//            checked = false;
//        }
//        if (colorway.getText().length() == 0) {
//            colorway.setError("Full name is required");
//            checked = false;
//        }
//        if (releaseDate.getText().length() == 0) {
//            releaseDate.setError("Full name is required");
//            checked = false;
//        }
//        if (description.getText().length() == 0) {
//            description.setError("Full name is required");
//            checked = false;
//        }
//        if (brand.getText().length() == 0) {
//            brand.setError("Full name is required");
//            checked = false;
//        }
//        if (price.getText().length() == 0) {
//            price.setError("Full name is required");
//            checked = false;
//        }

        // after all validation return true.
       // return checked;
}

