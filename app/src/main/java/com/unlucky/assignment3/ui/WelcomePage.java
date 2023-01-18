package com.unlucky.assignment3.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.data.Shoe;
import com.unlucky.assignment3.user.buyer.BuyerMain;
import com.unlucky.assignment3.user.seller.SellerMain;

import java.util.ArrayList;
import java.util.Map;

public class WelcomePage extends AppCompatActivity {

    Button signInButton,buyerButton,sellerButton;
    TextView signUpButton;
    EditText emailEditText, passwordEditText;
    boolean isBuyer = true;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    int clicked;
    boolean allowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_page);
        buyerButton = findViewById(R.id.buyer_button);
        sellerButton = findViewById(R.id.seller_button);
        signUpButton = findViewById(R.id.signUpWelcomeButton);
        signInButton = findViewById(R.id.signInButton);
        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);

        clicked = 0;

        if (isBuyer) {
            activateBuyerButton();
        }

        buyerButton.setOnClickListener(v -> {
            if (!isBuyer) {
                activateBuyerButton();
                isBuyer = true;
            }
        });
        sellerButton.setOnClickListener(v -> {
            if (isBuyer) {
                activateSellerButton();
                isBuyer = false;
            }
        });

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 1); // 1 is requestCode
            return;

        }

        signInButton.setOnClickListener(view -> logIn());
    }

    public void logIn() {
        boolean checked = true;
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

//        if (email.length() == 0) {
//            emailEditText.setError("Email is required");
//            checked = false;
//        } else if (!isValidEmail(email)) {
//            emailEditText.setError("Email is not valid");
//            checked = false;
//        }
//
//        if (password.length() == 0) {
//            passwordEditText.setError("Password is required");
//            checked = false;
//        }

        if (checked) {
            logInWithAuth(email, password);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void activateBuyerButton() {
        sellerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_background));
        sellerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_text));

        buyerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.yellow));
        buyerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.white));
    }

    public void activateSellerButton() {
        buyerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_background));
        buyerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.unactivated_text));

        sellerButton
                .setBackgroundColor(ContextCompat
                        .getColor(this,
                                R.color.yellow));
        sellerButton
                .setTextColor(ContextCompat
                        .getColor(this,
                                R.color.white));
    }

    public void signUp_action(View view) {
        signUpButton.setOnClickListener(v -> {
            Intent i = new Intent(this, SignUpPage.class);
            startActivity(i);
        });
    }

    private void logInWithAuth(String email, String password) {
//        auth.signInWithEmailAndPassword(email, password)
        clicked++;

        if (clicked == 1) {
            auth.signInWithEmailAndPassword("abc@gmail.com", "123456")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                clicked = 0;
                                Toast.makeText(WelcomePage.this, "Login successful!",
                                        Toast.LENGTH_SHORT).show();
                                currentUser = auth.getCurrentUser();

                                switchUserType();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchUserType() {
        Intent i;
        if (isBuyer) {
            i = new Intent(WelcomePage.this, BuyerMain.class);
            startActivity(i);
        } else {
//            i = new Intent(WelcomePage.this, SellerMain.class);
//            searchIntent.putExtra("shoe_list", shoeList);
//            startActivity(i);

            db.collection("shoes")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isComplete()) {
                                ArrayList<Shoe> shoeList = new ArrayList<>();

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

                                Intent searchIntent = new Intent(WelcomePage.this, SellerMain.class);

                                searchIntent.putExtra("shoe_list", shoeList);
                                startActivity(searchIntent);
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WelcomePage.this, "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
//                allowed = true;
            } else {
                Toast.makeText(WelcomePage.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                // permission granted do something
            }
        }
    }
}