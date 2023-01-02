package com.unlucky.assignment3.user.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;
import com.unlucky.assignment3.utilities.adapter.ShoeSearchRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerShoppingCart extends AppCompatActivity {
    private RecyclerView cartRV;
    private List<Shoe> shoeList;
    private ShoeSearchRecyclerViewAdapter adapter;
    ArrayList<String> cart = new ArrayList<>();
    TextView selected, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_shopping_cart);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button back = findViewById(R.id.back);
        selected = findViewById(R.id.selected);
        total = findViewById(R.id.total);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getStringArrayList("cart") != null) {
            cart = bundle.getStringArrayList("cart");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shoeList = new ArrayList<>();
        cartRV = findViewById(R.id.cart);

        db.collection("shoes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            double price = 0.0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> temp = document.getData();


                                if (cart.contains(temp.get("name"))) {
                                    Shoe shoeData = new Shoe((String) temp.get("name"),
                                            (String) temp.get("style"), (String) temp.get("colorway"),
                                            (String) temp.get("releaseDate"), (String) temp.get("description"),
                                            (Double) temp.get("price"));
                                    shoeList.add(shoeData);
                                    price += shoeData.getPrice();
                                }
                            }

                            if (!shoeList.isEmpty()) {
                                LinearLayoutManager layoutManager =
                                        new LinearLayoutManager(BuyerShoppingCart.this,
                                                LinearLayoutManager.VERTICAL, false);

                                cartRV.setLayoutManager(layoutManager);
                                adapter = new ShoeSearchRecyclerViewAdapter(BuyerShoppingCart.this, shoeList);
                                cartRV.setAdapter(adapter);

                                selected.setText("Selected Item: " + cart.size());
                                total.setText("Total: " + price);

                            }
                        }
                    }
                });
    }
}