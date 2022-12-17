package com.unlucky.assignment3.user.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unlucky.assignment3.R;

public class SellerAddShoe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_shoe);

        Button list = findViewById(R.id.list);
        Button remove = findViewById(R.id.add_remove);
        Button back = findViewById(R.id.back);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerAddShoe.this, SellerList.class);
                intent.putExtra("activity","list");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerAddShoe.this, SellerMain.class);
                intent.putExtra("activity","back");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerAddShoe.this, SellerRemoveShoe.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}