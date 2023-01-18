package com.unlucky.assignment3.user.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.unlucky.assignment3.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyerPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback, OnMapsSdkInitializedCallback {
    TextView price, searchText;
    Button placeOrder,search;
    private GoogleMap mMap;
    CheckBox checkBox;
    EditText name, phoneNumber;

    Boolean isPermissionGranted = false;
    MapView mapView;

    Location gps_loc, network_loc, final_loc;
    double longitude, latitude;
    String userCountry, userAddress;
    LocationManager locationManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_payment);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        currentUser = auth.getCurrentUser();

        Spinner spinner = findViewById(R.id.paymenMethod);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        back.setOnClickListener(view -> finish());

        Intent i = getIntent();

        price = findViewById(R.id.price);
        searchText = findViewById(R.id.addressPayTV);
        placeOrder = findViewById(R.id.placeOrder);
        checkBox = findViewById(R.id.checkBox);
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);

        db.collection("users")
                .document(currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            name.setText((CharSequence) doc.get("name"));
                            phoneNumber.setText((CharSequence) doc.get("phone"));
                        }
                    }
                });

        double priceNumber = (double) i.getExtras().get("price");
        price.setText("Total :$" + priceNumber);

        getCurrentLocation();

        AlertDialog alertDialog = new AlertDialog.Builder(BuyerPayment.this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to place this order?");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            Toast.makeText(BuyerPayment.this, "Ordered", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(BuyerPayment.this, BuyerSearch.class);
            setResult(RESULT_OK, intent);
            finish();
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.dismiss();
        });

        placeOrder.setOnClickListener(view -> {
            if (checkBox.isChecked()) {
                if (isValid()) {
                    alertDialog.show();
                }
            } else {
                Toast.makeText(this, "You must agree with term of purchase before order",
                        Toast.LENGTH_SHORT).show();
            }
        });

        checkMyPermission();
        mapView = findViewById(R.id.mapView);

        if (isPermissionGranted) {
            mapView.getMapAsync(this);
            mapView.onCreate(savedInstanceState);
        }

        search.setOnClickListener(this::geoLocate);
    }

    private void geoLocate(View view) {
        String locationName = searchText.getText().toString();
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());

        try {
            List<Address>  addressList = geocoder.getFromLocationName(locationName,1);

            if(addressList.size()>0){
                Address address = addressList.get(0);
                gotoLocation(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));

                Toast.makeText(this,address.getLocality(),Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude,longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(BuyerPayment.this, "Location permission Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("packet", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressLint("SetTextI18n")
    public void getCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
        }

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getCountryName();
                userAddress = addresses.get(0).getAddressLine(0);
                userAddress = userAddress.replace("Vietnam", "Việt Nam");
                searchText.setText(userAddress);
            }
            else {
                userCountry = "Unknown";
                searchText.setText(userCountry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    public boolean isValid() {
        boolean checked = true;

        String nameStr = name.getText().toString();
        String phoneNumberStr = phoneNumber.getText().toString();

        if (nameStr.length() == 0) {
            name.setError("Full name is required");
            checked = false;
        } else if (!isValidName(nameStr)) {
            name.setError("Full name is not valid");
            checked = false;
        }

        if (phoneNumberStr.length() == 0) {
            phoneNumber.setError("Phone number is required");
            checked = false;
        } else if (!isValidPhoneNumber(phoneNumberStr)) {
            phoneNumber.setError("Phone number is not valid");
            checked = false;
        }

        return checked;
    }

    public static boolean isValidName(String fullName)
    {
        String nameRegex = "[A-Z][a-z]+(?: [A-Z][a-z]*)*";

        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String str)
    {
//      10 digit phone number
        String phoneRegex = "0[0-9]{9}";

        return (str.matches(phoneRegex));
    }
}