package com.unlucky.assignment3.user.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unlucky.assignment3.R;

public class SellerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        Button add = findViewById(R.id.updateShoeButton);
        Button list = findViewById(R.id.viewListButton);
        Button remove = findViewById(R.id.removeShoeButton);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSellerList();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSellerRemoveShoe();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK){
                if(data.getExtras() != null){
                    System.out.println("Main");
                }
                else {
                    System.out.println("Lecturer");
                    openSellerList();
                }
            }
        }

        if (requestCode == 201) {
            if (resultCode == RESULT_OK){
                if(data.getExtras() != null){
                    System.out.println("Main");
                }
                else {
                    System.out.println("Courses");
                    openSellerRemoveShoe();
                }
            }
        }
    }
    public void openSellerList(){
        Intent i = new Intent(SellerMain.this, SellerList.class);
        startActivityForResult(i, 200);
    }
    public void openSellerRemoveShoe(){
        Intent i = new Intent(SellerMain.this, SellerRemoveShoe.class);
        startActivityForResult(i, 201);
    }
}