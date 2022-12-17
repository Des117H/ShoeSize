package com.unlucky.assignment3.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    String test;
    EditText name;
    EditText style;
    EditText colorway;
    EditText releaseDate;
    EditText description;
    EditText brand;
    EditText price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i  = new Intent (this, WelcomePage.class);
        startActivity(i);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.name);
        style = findViewById(R.id.style);
        colorway = findViewById(R.id.colorway);
        releaseDate = findViewById(R.id.releaseDate);
        description = findViewById(R.id.description);
        brand = findViewById(R.id.brand);
        price = findViewById(R.id.price);

        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (checkAllFields()) {
//                    Shoe shoe = new Shoe(name.getText().toString(), style.getText().toString(),
//                            colorway.getText().toString(), releaseDate.getText().toString(),
//                            description.getText().toString(), brand.getText().toString(),
//                            Integer.parseInt(price.getText().toString()));
//
//                    Map<String, Object> shoeDB = new HashMap<>();
//                    shoeDB.put("name", shoe.getName());
//                    shoeDB.put("style", shoe.getStyle());
//                    shoeDB.put("colorway", shoe.getColorway());
//                    shoeDB.put("pictureLink", shoe.getPictureLink());
//                    shoeDB.put("releaseDate", shoe.getReleaseDate());
//                    shoeDB.put("description", shoe.getDescription());
//                    shoeDB.put("price", shoe.getPrice());
//
//                    // Add a new document with a generated ID
//                    db.collection("shoes").document(shoe.name).set(shoeDB);
//                }
            }
        });



    }

    private boolean checkAllFields() {
        boolean checked = true;
        if (name.getText().length() == 0) {
            name.setError("Full name is required");
            checked = false;
        }

        if (style.getText().length() == 0) {
            style.setError("Full name is required");
            checked = false;
        }
        if (colorway.getText().length() == 0) {
            colorway.setError("Full name is required");
            checked = false;
        }
        if (releaseDate.getText().length() == 0) {
            releaseDate.setError("Full name is required");
            checked = false;
        }
        if (description.getText().length() == 0) {
            description.setError("Full name is required");
            checked = false;
        }
        if (brand.getText().length() == 0) {
            brand.setError("Full name is required");
            checked = false;
        }
        if (price.getText().length() == 0) {
            price.setError("Full name is required");
            checked = false;
        }

        // after all validation return true.
        return checked;
    }
}
