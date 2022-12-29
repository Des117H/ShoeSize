package com.unlucky.assignment3.user.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;
import com.unlucky.assignment3.utilities.adapter.ShoeSearchListAdapter;
import com.unlucky.assignment3.utilities.adapter.ShoeSearchRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class BuyerSearch extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ListView searchListView;
    private ArrayList<Shoe> shoeList = new ArrayList<>();
    private ShoeSearchListAdapter adapter;
    private SearchView shoeSearchView;
    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_search);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Handler timeHandler = new Handler(Looper.getMainLooper());

        Button back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchListView = findViewById(R.id.search_list_view);

        Bundle bundle = getIntent().getExtras();

        shoeList = (ArrayList<Shoe>) bundle.getSerializable("shoe_list");

        if (shoeList!= null && !shoeList.isEmpty()) {
            // Pass results to ListViewAdapter Class
            adapter = new ShoeSearchListAdapter(this, shoeList);

            // Binds the Adapter to the ListView
            searchListView.setAdapter(adapter);

            // Locate the EditText in listview_main.xml
            shoeSearchView = findViewById(R.id.shoeSearchView);
            shoeSearchView.setOnQueryTextListener(this);
        }

//        db.collection("shoes")
//                .orderBy("name", Query.Direction.ASCENDING)
//                .limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isComplete()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String, Object> temp = document.getData();
//                                Shoe shoeData = new Shoe((String) temp.get("name"),
//                                        (String) temp.get("style"), (String) temp.get("colorway"),
//                                        (String) temp.get("releaseDate"), (String) temp.get("description"),
//                                        (Double) temp.get("price"));
//                                shoeList.add(shoeData);
//                            }
//
//                            LinearLayoutManager layoutManager =
//                                    new LinearLayoutManager(BuyerSearch.this,
//                                            LinearLayoutManager.VERTICAL, false);
//
//                            searchListView.setLayoutManager(layoutManager);
//                            adapter = new ShoeSearchRecyclerViewAdapter(BuyerSearch.this, shoeList);
//                            searchListView.setAdapter(adapter);
//                        }
//                    }
//                });

//        LinearLayoutManager layoutManager =
//                new LinearLayoutManager(BuyerSearch.this,
//                        LinearLayoutManager.VERTICAL, false);
//
//        searchListView.setLayoutManager(layoutManager);
//        adapter = new ShoeSearchRecyclerViewAdapter(BuyerSearch.this, shoeList);
//        searchListView.setAdapter(adapter);
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