package com.unlucky.assignment3.user.seller;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;
import com.unlucky.assignment3.ui.WelcomePage;
import com.unlucky.assignment3.utilities.adapter.SellerShoeSearchListAdapter;

import java.util.ArrayList;
import java.util.Map;

public class SellerMain extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ListView searchListView;
    private SearchView searchBox;
    FirebaseFirestore db;
    private ArrayList<Shoe> shoeList = null;
    private ArrayList<String> addList = new ArrayList<>();
    private ArrayList<String> deleteList = new ArrayList<>();
    private SellerShoeSearchListAdapter adapter;
    Button addShoeButton, logOutButton;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1){
                    Shoe shoeAdd = (Shoe) result.getData().getExtras().get("shoe_to_add");
                    if (!addList.contains(shoeAdd.getName())){
                        addList.add(shoeAdd.getName());
                        adapter.addShoe(shoeAdd);
                        shoeList.add(shoeAdd);
                        adapter.notifyDataSetChanged();
                    }
                } else if (result.getResultCode() == 2) {
                    String shoeDelete = String.valueOf(result
                            .getData()
                            .getExtras()
                            .getString("shoe_to_delete"));
                    if (!deleteList.contains(shoeDelete)){
                        adapter.removeShoe(shoeDelete);
                        deleteList.add(shoeDelete);
                        shoeList.removeIf(shoe -> shoe.getName().equals(shoeDelete));
                        adapter.notifyDataSetChanged();
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
        addShoeButton = findViewById(R.id.addShoeButton);
        logOutButton = findViewById(R.id.sellerLogOutButton);
        searchBox = findViewById(R.id.shoeSellSearchView);
        shoeList = new ArrayList<>();

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                if (auth.getCurrentUser() == null) {
                    Toast.makeText(SellerMain.this, "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SellerMain.this, WelcomePage.class);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            System.out.println("email: " + currentUser.getEmail());
        }

        db.collection("users")
                .document(currentUser.getEmail())
                .collection("shoeSell")
                .orderBy("name", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                            adapter = new SellerShoeSearchListAdapter(SellerMain.this, shoeList);

                            // Binds the Adapter to the ListView
                            searchListView.setAdapter(adapter);

                            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Shoe selectedItem = (Shoe) parent.getItemAtPosition(position);

                                    //Toast.makeText(SellerMain.this, "clicked", Toast.LENGTH_SHORT).show();

                                    Intent toShoeDetail = new Intent(SellerMain.this, SellerShoeDetail.class);
                                    toShoeDetail.putExtra("shoe_name",selectedItem.name);
                                    activityResultLaunch.launch(toShoeDetail);
                                }
                            });

                            // Locate the EditText in listview_main.xml
                            searchBox.setOnQueryTextListener(SellerMain.this);
                        }
                    }
                });

        searchBox.setOnClickListener(view -> adapter.notifyDataSetChanged());

        addShoeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerMain.this, SellerAddShoe.class);

                activityResultLaunch.launch(i);
            }
        });
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