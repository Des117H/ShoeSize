package com.unlucky.assignment3.user.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unlucky.assignment3.R;

public class SellerList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);

        Button add = findViewById(R.id.add);
        Button remove = findViewById(R.id.remove);
        Button back = findViewById(R.id.back);


        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerList.this, SellerAddShoe.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerList.this, SellerRemoveShoe.class);
                intent.putExtra("activity","remove");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerList.this, SellerMain.class);
                intent.putExtra("activity","back");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}