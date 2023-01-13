package com.unlucky.assignment3.user.buyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.unlucky.assignment3.R;
import com.unlucky.assignment3.ui.AccountPage;
import com.unlucky.assignment3.ui.WelcomePage;

import java.util.ArrayList;

public class BuyerPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView price;
    Button placeOrder;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_payment);

        Spinner spinner = findViewById(R.id.paymenMethod);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.payment_method, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button back = findViewById(R.id.back);

        back.setOnClickListener(view -> finish());

        Intent i = getIntent();

        price = findViewById(R.id.price);
        placeOrder = findViewById(R.id.placeOrder);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        double priceNumber = (double) i.getExtras().get("price");
        String total = "Total :$" + priceNumber;
        price.setText(total);

        AlertDialog alertDialog = new AlertDialog.Builder(BuyerPayment.this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to place this order?");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            Toast.makeText(BuyerPayment.this, "Ordered", Toast.LENGTH_SHORT).show();

//            Intent y = new Intent(BuyerPayment.this,BuyerMain.class);
//            startActivity(y);
            Intent intent = new Intent(BuyerPayment.this, BuyerSearch.class);
            setResult(RESULT_OK, intent);
            finish();
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.dismiss();
        });

        placeOrder.setOnClickListener(view -> {
            alertDialog.show();
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}