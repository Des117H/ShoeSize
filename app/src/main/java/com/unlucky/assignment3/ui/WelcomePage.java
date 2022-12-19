package com.unlucky.assignment3.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unlucky.assignment3.R;

import com.unlucky.assignment3.user.buyer.BuyerDetail;
import com.unlucky.assignment3.user.buyer.BuyerMain;
import com.unlucky.assignment3.user.buyer.BuyerPayment;
import com.unlucky.assignment3.user.seller.SellerMain;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent i = new Intent(this, BuyerPayment.class);
//        startActivity(i);
        setContentView(R.layout.activity_welcome_page);
    }
}