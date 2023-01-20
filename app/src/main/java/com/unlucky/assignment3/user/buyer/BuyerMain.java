package com.unlucky.assignment3.user.buyer;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;
import com.unlucky.assignment3.data.Photo;
import com.unlucky.assignment3.ui.AccountPage;
import com.unlucky.assignment3.ui.WelcomePage;
import com.unlucky.assignment3.utilities.RecyclerItemClickListener;
import com.unlucky.assignment3.utilities.adapter.ShoeRecyclerViewAdapter;
import com.unlucky.assignment3.utilities.adapter.photoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class BuyerMain extends AppCompatActivity {
    private RecyclerView newShoeRV;
    private List<Shoe> newShoeList, shoeCart;
    private ShoeRecyclerViewAdapter adapter;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private photoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer mTimer;

    ArrayList<String> cart = new ArrayList<>();
    FirebaseFirestore db;

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
                        if (!cart.contains(shoeToCart)) {
                            cart.add(shoeToCart);
                        }
                    } else if (result.getResultCode() == 2) {
                        System.out.println("done search");
                        ArrayList<String> shoeToCart = result.
                                getData()
                                .getExtras()
                                .getStringArrayList("shoe_list_to_cart");
                        for (String shoeName: shoeToCart) {
                            if (!cart.contains(shoeName)) {
                                cart.add(shoeName);
                            }

                            for (String name: cart) {
                                System.out.println("a: " + name);
                            }
                        }
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 202) {
            if (resultCode == RESULT_OK){
                Intent x = new Intent(BuyerMain.this, WelcomePage.class);
                startActivity(x);
                finish();
            }
        }
        if (requestCode == 203) {
            if (resultCode == RESULT_OK){
                cart.clear();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);
        db = FirebaseFirestore.getInstance();

        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        Button search = findViewById(R.id.searchMain);
        
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
                                                (String) temp.get("style"),
                                                (String) temp.get("colorway"),
                                                (String) temp.get("releaseDate"),
                                                (String) temp.get("description"),
                                                Double.parseDouble(temp.get("price").toString()),
                                                (String) temp.get("pictureLink"));
                                        shoeList.add(shoeData);
                                    }

                                    Intent searchIntent = new Intent(BuyerMain.this, BuyerSearch.class);

                                    searchIntent.putExtra("shoe_list", shoeList);
                                    activityResultLaunch.launch(searchIntent);
                                }
                            }
                        });
            }
        });

        newShoeList = new ArrayList<>();
        shoeCart = new ArrayList<>();
        newShoeRV = findViewById(R.id.newShoeRV);

        db.collection("shoes")
                .orderBy("releaseDate", Query.Direction.DESCENDING)
                .limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> temp = document.getData();
                                Shoe shoeData = new Shoe((String) temp.get("name"),
                                        (String) temp.get("style"), (String) temp.get("colorway"),
                                        (String) temp.get("releaseDate"), (String) temp.get("description"),
                                        (Double) temp.get("price"), (String) temp.get("pictureLink"));
                                newShoeList.add(shoeData);
                            }
                            LayoutAnimationController ani = AnimationUtils.loadLayoutAnimation(BuyerMain.this,R.anim.layout_animation_down_to_up);
                            newShoeRV.setLayoutAnimation(ani);
                            newShoeRV.setHasFixedSize(true);
                            newShoeRV.setLayoutManager(new GridLayoutManager(BuyerMain.this,2));

                            adapter = new ShoeRecyclerViewAdapter(BuyerMain.this, newShoeList);

                            newShoeRV.setAdapter(adapter);

                            newShoeRV.addOnItemTouchListener(new RecyclerItemClickListener(BuyerMain.this, newShoeRV, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent toShoeDetail = new Intent(BuyerMain.this, BuyerDetail.class);
                                    toShoeDetail
                                            .putExtra("shoe_name", 
                                                    newShoeList.get(
                                                            newShoeRV
                                                                    .getChildAdapterPosition(view)
                                                    ).getName());
                                    activityResultLaunch.launch(toShoeDetail);
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {

                                }
                            }));
                        }
                    }
                });

        viewPager = findViewById(R.id.viewpaper);
        circleIndicator = findViewById(R.id.circle_indicator);

        mListPhoto =getListPhoto();

        photoAdapter = new photoAdapter(this,mListPhoto);
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoSlideImage();

        newShoeRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if(dy > 0){
            bottom_nav.setVisibility(View.INVISIBLE);
        }
        else {
            bottom_nav.setVisibility(View.VISIBLE);
        }
        super.onScrolled(recyclerView, dx, dy);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_cart:
                    Intent i = new Intent(BuyerMain.this, BuyerShoppingCart.class);
                    i.putStringArrayListExtra("cart", cart);
                    startActivityForResult(i,203);
                    break;

                case R.id.action_account:
                    Intent x = new Intent(BuyerMain.this, AccountPage.class);
                    startActivityForResult(x,202);
                    break;
            }
            return true;
        });
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.poster1));
        list.add(new Photo(R.drawable.poster2));
        list.add(new Photo(R.drawable.poster3));
        list.add(new Photo(R.drawable.poster4));

        return list;
    }

    private void autoSlideImage(){
        if(mListPhoto == null || mListPhoto.isEmpty() || viewPager == null){
            return;
        }

        if(mTimer == null){
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size()-1;
                        if (currentItem < totalItem){
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        }
                        else{
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },  500,2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}