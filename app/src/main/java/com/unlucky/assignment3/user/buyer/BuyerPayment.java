package com.unlucky.assignment3.user.buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unlucky.assignment3.R;

public class BuyerPayment extends AppCompatActivity {

    TextView price;
     Button placeOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_payment);

        Button back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i = getIntent();

        price  =findViewById(R.id.price);
        placeOrder = findViewById(R.id.placeOrder);

        double priceNumber = (double) i.getExtras().get("price");
        String total = "Total :$" + priceNumber;
        price.setText(total);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y = new Intent(BuyerPayment.this,BuyerMain.class);
                startActivity(y);
                finish();
            }
        });

    }
}