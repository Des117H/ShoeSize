package com.unlucky.assignment3.user.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unlucky.assignment3.R;

public class SellerRemoveShoe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_remove_shoe);

        Button list = findViewById(R.id.list);
        Button add = findViewById(R.id.add);
        Button back = findViewById(R.id.back);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRemoveShoe.this, SellerList.class);
                intent.putExtra("activity","list");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRemoveShoe.this, SellerMain.class);
                intent.putExtra("activity","back");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRemoveShoe.this, SellerAddShoe.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}