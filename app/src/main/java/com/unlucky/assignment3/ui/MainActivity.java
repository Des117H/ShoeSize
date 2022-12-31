package com.unlucky.assignment3.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

    Button buyerButton = findViewById(R.id.buyer_button);
    Button sellerButton = findViewById(R.id.seller_button);
    boolean isBuyer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (isBuyer) {
            activateBuyerButton();
        }


        buyerButton.setOnClickListener(v -> {
            if (!isBuyer) {
                activateBuyerButton();
                isBuyer = true;
            }
        });

        sellerButton.setOnClickListener(v -> {
            if (isBuyer) {
                activateSellerButton();
                isBuyer = false;
            }
        });
    }
    public void activateBuyerButton() {
        sellerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_background));
        sellerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_text));

        buyerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.red));
        buyerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.white));
    }

    public void activateSellerButton() {
        buyerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_background));
        buyerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_text));

        sellerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.red));
        sellerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.white));
    }

}

