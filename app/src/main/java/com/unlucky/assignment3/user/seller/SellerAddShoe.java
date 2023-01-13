package com.unlucky.assignment3.user.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;

import java.util.Calendar;

public class SellerAddShoe extends AppCompatActivity {
    EditText name, price, style, colorway, releaseDay, description, imageURL;
    Button back;
    FloatingActionButton addShoe;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;

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
                                releaseDay.setText(year + "/" +
                                        (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
//
//        name.setText("Air Jordan 18 Retro Yellow Ochre");
//        price.setText("1");
//        style.setText("1");
//        colorway.setText("1");
//
//        description.setText("1");
//        imageURL.setText("1");
    }

    public void checkShoe() {
        String nameStr = name.getText().toString();
        String priceStr = price.getText().toString();
        String styleStr = style.getText().toString();
        String colorwayStr = colorway.getText().toString();
        String releaseDayStr = releaseDay.getText().toString();
        String descriptionStr = description.getText().toString();
        String imageURLStr = imageURL.getText().toString();
        db.collection("users")
                .document(currentUser.getEmail())
                .collection("shoeSell")
                .whereEqualTo("name", name.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {

                            if (task.getResult().size() == 0) {
                                if (isValid()) {
                                    Shoe shoe = new Shoe(nameStr, styleStr, colorwayStr,
                                            releaseDayStr, descriptionStr,
                                            Double.parseDouble(priceStr), imageURLStr);
                                    Intent intent = new Intent();
                                    intent.putExtra("shoe_to_add", shoe);
                                    setResult(1, intent);
                                    finish();
                                }
                            }
                            else {
                                name.setError("This shoe is already exist!!!");
                            }
                        }
                    }
                });
    }

    public boolean isValid() {
        String nameStr = name.getText().toString();
        String priceStr = price.getText().toString();
        String styleStr = style.getText().toString();
        String colorwayStr = colorway.getText().toString();
        String releaseDayStr = releaseDay.getText().toString();
        String descriptionStr = description.getText().toString();
        String imageURLStr = imageURL.getText().toString();

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
}