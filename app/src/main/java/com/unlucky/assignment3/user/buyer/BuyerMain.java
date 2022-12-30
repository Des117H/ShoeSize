package com.unlucky.assignment3.user.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;
import com.unlucky.assignment3.ui.Account;
import com.unlucky.assignment3.utilities.adapter.ShoeRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerMain extends AppCompatActivity {
    private RecyclerView newShoeRV, bestSellShoeRV;
    private List<Shoe> newShoeList, bestSellShoeList;
    private ShoeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button search = findViewById(R.id.searchMain);
       // Button cart = findViewById(R.id.toCartBtn);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("shoes")
                        .orderBy("name", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isComplete()) {
                                    ArrayList<Shoe> shoeList = new ArrayList<Shoe>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> temp = document.getData();
                                        Shoe shoeData = new Shoe((String) temp.get("name"),
                                                (String) temp.get("style"), (String) temp.get("colorway"),
                                                (String) temp.get("releaseDate"), (String) temp.get("description"),
                                                (Double) temp.get("price"));
                                        shoeList.add(shoeData);
                                    }

                                    Intent searchIntent = new Intent(BuyerMain.this, BuyerSearch.class);

                                    searchIntent.putExtra("shoe_list", shoeList);
                                    startActivity(searchIntent);
                                }
                            }
                        });
            }
        });

//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent searchIntent = new Intent(BuyerMain.this, BuyerShoppingCart.class);
//                startActivity(searchIntent);
//            }
//        });

        newShoeList = new ArrayList<>();
        bestSellShoeList = new ArrayList<>();
        newShoeRV = findViewById(R.id.newShoeRV);
        bestSellShoeRV = findViewById(R.id.bestSellShoeRV);

        db.collection("shoes")
                .orderBy("releaseDate", Query.Direction.DESCENDING)
                .limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> temp = document.getData();
                                Shoe shoeData = new Shoe((String) temp.get("name"),
                                        (String) temp.get("style"), (String) temp.get("colorway"),
                                        (String) temp.get("releaseDate"), (String) temp.get("description"),
                                        (Double) temp.get("price"));
                                newShoeList.add(shoeData);
                            }
                            LinearLayoutManager layoutManager =
                                    new LinearLayoutManager(BuyerMain.this,
                                            LinearLayoutManager.HORIZONTAL, false);

                            newShoeRV.setLayoutManager(layoutManager);
                            adapter = new ShoeRecyclerViewAdapter(BuyerMain.this, newShoeList);
                            newShoeRV.setAdapter(adapter);
                        }
                    }
                });

        db.collection("shoes")
                .orderBy("name", Query.Direction.ASCENDING)
                .limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> temp = document.getData();
                                Shoe shoeData = new Shoe((String) temp.get("name"),
                                        (String) temp.get("style"), (String) temp.get("colorway"),
                                        (String) temp.get("releaseDate"), (String) temp.get("description"),
                                        (Double) temp.get("price"));
                                bestSellShoeList.add(shoeData);
                            }
                            LinearLayoutManager layoutManager =
                                    new LinearLayoutManager(BuyerMain.this,
                                            LinearLayoutManager.HORIZONTAL, false);

                            bestSellShoeRV.setLayoutManager(layoutManager);
                            adapter = new ShoeRecyclerViewAdapter(BuyerMain.this, bestSellShoeList);
                            bestSellShoeRV.setAdapter(adapter);
                        }
                    }
                });

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(BuyerMain.this,"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_cart:
                        Intent i = new Intent(BuyerMain.this, BuyerShoppingCart.class);
                        startActivity(i);
                        break;
                    case R.id.action_account:
                        Intent x = new Intent(BuyerMain.this, Account.class);
                        startActivity(x);
                        break;
                }
                return true;
            }
        });
    }


}