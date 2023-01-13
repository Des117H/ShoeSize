package com.unlucky.assignment3.user.buyer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;
import com.unlucky.assignment3.ui.WelcomePage;
import com.unlucky.assignment3.utilities.RecyclerItemClickListener;
import com.unlucky.assignment3.utilities.adapter.ShoeSearchRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerShoppingCart extends AppCompatActivity {
    private RecyclerView cartRV;
    private List<Shoe> shoeList;
    private ShoeSearchRecyclerViewAdapter adapter;
    private double price = 0.0;
    ArrayList<String> cart = new ArrayList<>();
    TextView selected, total;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202) {
            if (resultCode == RESULT_OK){
                cart.clear();
                getShoppingCart();
                Intent intent = new Intent(BuyerShoppingCart.this, BuyerMain.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_shopping_cart);


        Button back = findViewById(R.id.back);
        selected = findViewById(R.id.selected);
        total = findViewById(R.id.total);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getStringArrayList("cart") != null) {
            cart = bundle.getStringArrayList("cart");
        }

        back.setOnClickListener(view -> finish());

        shoeList = new ArrayList<>();
        cartRV = findViewById(R.id.cart);

        Button checkOut = findViewById(R.id.checkOut);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.size() != 0) {
                    Intent i = new Intent(BuyerShoppingCart.this,BuyerPayment.class);
                    i.putExtra("price", price);
                    startActivityForResult(i,202);
                } else {
                    Toast.makeText(BuyerShoppingCart.this, "There is no shoe in cart!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getShoppingCart();


    }

    public void getShoppingCart() {
        db.collection("shoes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> temp = document.getData();


                                if (cart.contains(temp.get("name"))) {
                                    Shoe shoeData = new Shoe((String) temp.get("name"),
                                            (String) temp.get("style"), (String) temp.get("colorway"),
                                            (String) temp.get("releaseDate"), (String) temp.get("description"),
                                            (Double) temp.get("price"), (String) temp.get("pictureLink"));
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

                                cartRV.addOnItemTouchListener(new RecyclerItemClickListener(BuyerShoppingCart.this, cartRV, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent toShoeDetail = new Intent(BuyerShoppingCart.this, BuyerDetail.class);
                                        toShoeDetail
                                                .putExtra("shoe_name",
                                                        shoeList.get(
                                                                cartRV
                                                                        .getChildAdapterPosition(view)
                                                        ).getName());
                                        activityResultLaunch.launch(toShoeDetail);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {

                                    }
                                }));

                                selected.setText("Selected Item: " + cart.size());
                                total.setText("Total: $" + price);

                            }
                        }
                    }
                });
    }

}