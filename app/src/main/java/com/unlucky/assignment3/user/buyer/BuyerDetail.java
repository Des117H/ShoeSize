package com.unlucky.assignment3.user.buyer;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;

import java.util.HashMap;
import java.util.Map;

public class BuyerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_detail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button back = findViewById(R.id.back);

        TextView name = findViewById(R.id.name);
        TextView mainPrice = findViewById(R.id.mainPrice);
        TextView style = findViewById(R.id.style);
        TextView colorway = findViewById(R.id.colorway);

        TextView releaseDay = findViewById(R.id.releaseDay);
        TextView description = findViewById(R.id.description);
        ImageView shoeImage = findViewById(R.id.shoeImage);

        FloatingActionButton cart = findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();

        String shoeName = bundle.getString("shoe_name");

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("shoe_to_cart", shoeName);
                setResult(1, intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Map<String, Object> shoeData = new HashMap<>();

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

                                String shoePrice = "$" + shoeData.get("price").toString();

                                name.setText((CharSequence) shoeData.get("name"));
                                mainPrice.setText(shoePrice);
                                style.setText((CharSequence) shoeData.get("style"));
                                colorway.setText((CharSequence) shoeData.get("colorway"));

                                releaseDay.setText((CharSequence) shoeData.get("releaseDate"));
                                description.setText((CharSequence) shoeData.get("description"));

                                Glide.with(BuyerDetail.this)
                                        .load(shoeData.get("pictureLink"))
                                        .into(shoeImage);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}