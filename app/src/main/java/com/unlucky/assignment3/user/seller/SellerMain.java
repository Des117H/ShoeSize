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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateShoeButton();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK){
                if(data.getExtras() != null){
                    if(data.getExtras().get("activity").toString().equals("remove"))
                    {
                        System.out.println("list -> remove");
                        openSellerRemoveShoe();
                    }
                    if(data.getExtras().get("activity").toString().equals("back"))
                    {
                        System.out.println("List -> Seller main");
                    }
                }
                else {
                    System.out.println("list - > add");
                    openUpdateShoeButton();
                }
            }
        }

        if (requestCode == 201) {
            if (resultCode == RESULT_OK){
                if(data.getExtras() != null){
                    if(data.getExtras().get("activity").toString().equals("list"))
                    {
                        System.out.println("add -> list");
                        openSellerList();
                    }
                    if(data.getExtras().get("activity").toString().equals("back"))
                    {
                        System.out.println("add -> Seller main");
                    }
                }
                else {
                    System.out.println("add -> remove");
                    openSellerRemoveShoe();
                }
            }
        }
        if (requestCode == 202) {
            if (resultCode == RESULT_OK){
                if(data.getExtras() != null){
                    if(data.getExtras().get("activity").toString().equals("list"))
                    {
                        System.out.println("remove -> list");
                        openSellerList();
                    }
                    if(data.getExtras().get("activity").toString().equals("back"))
                    {
                        System.out.println("remove -> Seller main");
                    }
                }
                else {
                    System.out.println("remove -> add");
                    openUpdateShoeButton();
                }
            }
        }
    }
    public void openSellerList(){
        Intent i = new Intent(SellerMain.this, SellerList.class);
        startActivityForResult(i, 200);
    }

    public void openUpdateShoeButton(){
        Intent i = new Intent(SellerMain.this, SellerAddShoe.class);
        startActivityForResult(i, 201);
    }
    public void openSellerRemoveShoe(){
        Intent i = new Intent(SellerMain.this, SellerRemoveShoe.class);
        startActivityForResult(i, 202);
    }
}