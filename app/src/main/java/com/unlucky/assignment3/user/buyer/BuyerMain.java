package com.unlucky.assignment3.user.buyer;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.utilities.DownloadImageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuyerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button search = findViewById(R.id.searchMain);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(BuyerMain.this, BuyerSearch.class);
                startActivity(searchIntent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.shoeItemRecyclerView);
        List<String> shoeNameList = new ArrayList<>();

        Task<QuerySnapshot> task = db.collection("shoes")
                .orderBy("releaseDate", Query.Direction.DESCENDING)
                .limit(5).get();

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                shoeNameList.add((String) document.get("name"));
                System.out.println(shoeNameList.get(shoeNameList.size() - 1));
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }

        for (String shoe: shoeNameList) {
            System.out.println("list: " + shoe);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShoeRecyclerViewAdapter shoeAdapter = new ShoeRecyclerViewAdapter(this, shoeNameList);
        recyclerView.setAdapter(shoeAdapter);
    }
}