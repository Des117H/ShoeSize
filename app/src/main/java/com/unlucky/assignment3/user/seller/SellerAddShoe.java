package com.unlucky.assignment3.user.seller;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SellerAddShoe extends AppCompatActivity {
    EditText name, price, style, colorway, releaseDay, description, imageURL;
    Button back;
    FloatingActionButton addShoe;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    String nameStr, priceStr, styleStr, colorwayStr, releaseDayStr, descriptionStr,imageURLStr;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_shoe);

        currentUser = auth.getCurrentUser();

        back = findViewById(R.id.back);
        addShoe = findViewById(R.id.addShoeFloatButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(-1, intent);
                finish();
            }
        });

        addShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkShoe();
            }
        });

        name = findViewById(R.id.nameId);
        price = findViewById(R.id.priceId);
        style = findViewById(R.id.styleId);
        colorway = findViewById(R.id.colorwayId);

        releaseDay = findViewById(R.id.dayID);
        description = findViewById(R.id.descriptionId);
        imageURL = findViewById(R.id.imageID);

        releaseDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                @SuppressLint("SetTextI18n")
                DatePickerDialog datePickerDialog = new DatePickerDialog(SellerAddShoe.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                releaseDay.setText(year + "-" +
                                        (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
//
        name.setText("Nike Dunk High Panda 2021 W");
        price.setText("110");
        style.setText("DD1869-103");
        colorway.setText("Black/White-University Red");
        releaseDay.setText("2021-07-27");
        description.setText("The women's Nike Dunk High Panda (W) features white and black " +
                "leather upper with a matching sole. From there, hits of red on the woven tongue " +
                "label add some subtle flair.\nThe women's Nike Dunk High Black White (W) " +
                "released in July of 2021 and retailed for $110.");
        imageURL.setText("https://images.stockx.com/360/Nike-Dunk-High-Panda-2021-W/Images/Nike-Dunk-High-Panda-2021-W/Lv2/img01.jpg?fm=avif&amp;auto=compress&amp;w=576&amp;");
    }

    public void checkShoe() {
        getDataToString();

        if (isValid()) {
            db.collection("users")
                    .document(currentUser.getEmail())
                    .collection("shoeSell")
                    .whereEqualTo("name", name.getText().toString())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isComplete()) {
                            if (task.getResult().size() == 0) {
                                if (isValid()) {
                                    Shoe newShoe = new Shoe(nameStr, styleStr, colorwayStr,
                                            releaseDayStr, descriptionStr,
                                            Double.parseDouble(priceStr), imageURLStr);
                                    Map<String, Object> sData = newShoe.toMap();

                                    addShoeToDatabase(sData);
                                    addShoeToUserList(sData);

                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                }
                            }
                            else {
                                name.setError("This shoe is already exist!!!");
                            }
                        }
                    });
        }
    }

    public void getDataToString() {
        nameStr = name.getText().toString();
        priceStr = price.getText().toString();
        styleStr = style.getText().toString();
        colorwayStr = colorway.getText().toString();
        releaseDayStr = releaseDay.getText().toString();
        descriptionStr = description.getText().toString();
        imageURLStr = imageURL.getText().toString();
    }

    public boolean isValid() {
        boolean checked = true;
        if (nameStr.length() == 0) {
            name.setError("Name is required");
            checked = false;
        }

        if (priceStr.length() == 0) {
            price.setError("Price is required");
            checked = false;
        } else {
            try {
                Double priceDouble = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                price.setError("Price is not valid");
            }
        }

        if (styleStr.length() == 0) {
            style.setError("Style is required");
            checked = false;
        }

        if (colorwayStr.length() == 0) {
            colorway.setError("Colorway is required");
            checked = false;
        }

        if (releaseDayStr.length() == 0) {
            releaseDay.setError("Release day is required");
            checked = false;
        } else {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                df.parse(releaseDayStr);
            } catch (ParseException e) {
                e.printStackTrace();
                releaseDay.setError("Release date is not valid");
                checked = false;
            }
        }

        if (descriptionStr.length() == 0) {
            description.setError("Description is required");
            checked = false;
        }

        if (imageURLStr.length() == 0) {
            imageURL.setError("Image link is required");
            checked = false;
        }

        return checked;
    }

    public void addShoeToUserList(Map<String, Object> sData) {
        db.collection("users")
                .document(currentUser.getEmail())
                .collection("shoeSell")
                .document(nameStr)
                .set(sData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "shoe added to user");
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    public void addShoeToDatabase(Map<String, Object> sData) {
        db.collection("shoes")
                .whereEqualTo("name", nameStr)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        if (task.getResult().size() == 0) {
                            if (isValid()) {
                                db.collection("shoes")
                                        .document(nameStr)
                                        .set(sData)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "Shoe added to database");}
                                        });
                            }
                        }
                    }
                });
    }
}