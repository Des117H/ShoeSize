package com.unlucky.assignment3.user.seller;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;
import com.unlucky.assignment3.ui.WelcomePage;
import com.unlucky.assignment3.user.buyer.BuyerDetail;
import com.unlucky.assignment3.user.buyer.BuyerSearch;
import com.unlucky.assignment3.utilities.adapter.ShoeSearchListAdapter;
import com.unlucky.assignment3.utilities.adapter.ShoeSellerRVAdapter;

import java.util.ArrayList;
import java.util.Map;

public class SellerMain extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ListView searchListView;
    private SearchView searchBox;
    FirebaseFirestore db;
    private ArrayList<Shoe> shoeList = new ArrayList<>();
    private ArrayList<Shoe> newAddedShoeList = new ArrayList<>();
    private ShoeSearchListAdapter adapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;

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
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);
        db = FirebaseFirestore.getInstance();

        searchBox = findViewById(R.id.shoeSellSearchView);
        searchListView = findViewById(R.id.sell_search_list_view);

        Bundle bundle = getIntent().getExtras();

        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            System.out.println("email: " + currentUser.getEmail());
        }

        if (bundle != null) {
            shoeList = (ArrayList<Shoe>) bundle.getSerializable("shoe_list");

            if (shoeList!= null && !shoeList.isEmpty()) {
                // Pass results to ListViewAdapter Class
                adapter = new ShoeSearchListAdapter(this, shoeList);

//                add all
//                db.collection("shoes")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isComplete()) {
//                                    ArrayList<Shoe> shoeList = new ArrayList<>();
//
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Map<String, Object> temp = document.getData();
//
//                                        db.collection("users")
//                                                .document(currentUser.getEmail())
//                                                .collection("shoeSell").add(temp).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                                                        Toast.makeText(SellerMain.this, "added", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                    }
//                                }
//                            }
//                        });

                // Binds the Adapter to the ListView
                searchListView.setAdapter(adapter);

                searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Shoe selectedItem = (Shoe) parent.getItemAtPosition(position);

                        Intent toShoeDetail = new Intent(SellerMain.this, BuyerDetail.class);
                        toShoeDetail.putExtra("shoe_name",selectedItem.name);
                        activityResultLaunch.launch(toShoeDetail);
                    }
                });

                // Locate the EditText in listview_main.xml
                searchBox = findViewById(R.id.shoeSellSearchView);
                searchBox.setOnQueryTextListener(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shoe_list_menu, menu);

        // Initialise menu item search bar with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        // attach setOnQueryTextListener to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query than filter the adapter using the filter
                // method with the query as its argument
                if (shoeList.contains(query)) {
                    adapter.filter(query);
                } else {
                    // Search query not found in List View
                    Toast.makeText(SellerMain.this, "Not found", Toast.LENGTH_LONG).show();
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