package com.unlucky.assignment3.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.unlucky.assignment3.R;
import com.unlucky.assignment3.user.buyer.BuyerMain;
import com.unlucky.assignment3.user.seller.SellerMain;

public class SignUpPage extends AppCompatActivity {

    Button buyerButton, sellerButton, signUpButton;
    boolean isBuyer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        buyerButton = findViewById(R.id.buyer_button);
        sellerButton = findViewById(R.id.seller_button);
        signUpButton = findViewById(R.id.signUpButton);

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

        signUpButton.setOnClickListener(v -> {
            if (signUp()) {
                Intent i;
                if (isBuyer) {
                    i = new Intent(this, BuyerMain.class);
                } else {

                    i = new Intent(this, SellerMain.class);
                }
                startActivity(i);
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

    public boolean signUp() {
        boolean isValid = true;



        return  isValid;
    }
}