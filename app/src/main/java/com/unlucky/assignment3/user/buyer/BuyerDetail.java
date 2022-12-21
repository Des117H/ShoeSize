package com.unlucky.assignment3.user.buyer;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.utilities.DownloadImageTask;

import java.io.InputStream;
import java.net.URL;
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
        TextView retailPrice = findViewById(R.id.retailPrice);
        TextView releaseDay = findViewById(R.id.releaseDay);
        TextView description = findViewById(R.id.description);
        ImageView shoeImage = findViewById(R.id.shoeImage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Map<String, Object> shoeData = new HashMap<>();

        db.collection("shoes")
                .whereEqualTo("name", "Air Jordan 1 Mid Light Smoke Grey")
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
                                retailPrice.setText(shoePrice);
                                releaseDay.setText((CharSequence) shoeData.get("releaseDate"));
                                description.setText((CharSequence) shoeData.get("description"));
                                new DownloadImageTask(shoeImage)
                                        .execute((String) shoeData.get("pictureLink"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}