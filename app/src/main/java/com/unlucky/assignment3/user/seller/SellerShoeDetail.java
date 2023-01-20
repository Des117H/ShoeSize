package com.unlucky.assignment3.user.seller;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.unlucky.assignment3.R;
import com.unlucky.assignment3.user.buyer.BuyerMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerShoeDetail extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    TextView name, mainPrice, style, colorway, releaseDay, description;
    ImageView shoeImage;
    Map<String, Object> shoeData = new HashMap<>();
    FloatingActionButton deleteShoeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_shoe_detail);

        currentUser = auth.getCurrentUser();

        Button back = findViewById(R.id.back);

        name = findViewById(R.id.name);
        mainPrice = findViewById(R.id.mainPrice);
        style = findViewById(R.id.style);
        colorway = findViewById(R.id.colorway);

        releaseDay = findViewById(R.id.releaseDay);
        description = findViewById(R.id.description);
        shoeImage = findViewById(R.id.shoeImage);

        deleteShoeButton = findViewById(R.id.deleteButton);

        String shoeName = getIntent().getExtras().getString("shoe_name");

        AlertDialog alertDialog = new AlertDialog.Builder(SellerShoeDetail.this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to delete this shoe?");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            db.collection("users")
                    .document(currentUser.getEmail())
                    .collection("shoeSell")
                    .document(shoeName)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            db.collection("shoes")
                                    .document(shoeName)
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(SellerShoeDetail.this, "Deleted",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent y = new Intent(SellerShoeDetail.this,
                                                    SellerMain.class);
                                            startActivity(y);
                                            finish();
                                        }
                                    });
                        }
                    });
            });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.dismiss();
        });

        deleteShoeButton.setOnClickListener(view -> {
            alertDialog.show();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db.collection("shoes")
                .whereEqualTo("name", shoeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                shoeData.putAll(document.getData());

                                setDataToView();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void setDataToView() {
        String shoePrice = "$" + shoeData.get("price").toString();

        name.setText((CharSequence) shoeData.get("name"));
        mainPrice.setText(shoePrice);
        style.setText((CharSequence) shoeData.get("style"));
        colorway.setText((CharSequence) shoeData.get("colorway"));

        releaseDay.setText((CharSequence) shoeData.get("releaseDate"));
        description.setText((CharSequence) shoeData.get("description"));

        Glide.with(SellerShoeDetail.this)
                .load(shoeData.get("pictureLink"))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Toast.makeText(SellerShoeDetail.this, "Error loading image",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .into(shoeImage);
    }
}