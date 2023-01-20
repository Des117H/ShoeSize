package com.unlucky.assignment3.user.buyer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;
import com.unlucky.assignment3.user.seller.SellerMain;
import com.unlucky.assignment3.user.seller.SellerShoeDetail;
import com.unlucky.assignment3.utilities.adapter.ShoeSearchListAdapter;

import java.util.ArrayList;
import java.util.Map;

public class BuyerSearch extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ListView searchListView;
    private ArrayList<Shoe> shoeList = new ArrayList<>();
    private ShoeSearchListAdapter adapter;
    private SearchView shoeSearchView;
    private ArrayList<String> shoeNameList = new ArrayList<>();

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        String shoeToCart =
                                String.valueOf(result
                                        .getData()
                                        .getExtras()
                                        .getString("shoe_to_cart"));
                        if (!shoeNameList.contains(shoeToCart)) {
                            shoeNameList.add(shoeToCart);
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_search);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button back = findViewById(R.id.back);
        searchListView = findViewById(R.id.search_list_view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("shoe_list_to_cart", shoeNameList);
                setResult(2, intent);
                finish();
            }
        });


        db.collection("shoes")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            ArrayList<Shoe> shoeList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> temp = document.getData();
                                Shoe shoeData = new Shoe((String) temp.get("name"),
                                        (String) temp.get("style"), (String) temp.get("colorway"),
                                        (String) temp.get("releaseDate"), (String) temp.get("description"),
                                        Double.parseDouble(temp.get("price").toString()), (String) temp.get("pictureLink"));

                                shoeList.add(shoeData);
                            }

                            adapter = new ShoeSearchListAdapter(BuyerSearch.this, shoeList);

                            // Binds the Adapter to the ListView
                            searchListView.setAdapter(adapter);

                            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Shoe selectedItem = (Shoe) parent.getItemAtPosition(position);
//                    shoeNameList.add(selectedItem.name);
                                    Intent toShoeDetail = new Intent(BuyerSearch.this, BuyerDetail.class);
                                    toShoeDetail.putExtra("shoe_name",selectedItem.name);
                                    activityResultLaunch.launch(toShoeDetail);
                                }
                            });

                            // Locate the EditText in listview_main.xml
                            shoeSearchView = findViewById(R.id.shoeSearchView);
                            shoeSearchView.setOnQueryTextListener(BuyerSearch.this);
                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shoe_list_menu, menu);
  
        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
  
        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query than filter the adapter
                // using the filter method with the query as its argument
                if (shoeList.contains(query)) {
                    adapter.filter(query);
                } else {
                    // Search query not found in List View
                    Toast.makeText(BuyerSearch.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }
  
            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}