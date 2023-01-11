package com.unlucky.assignment3.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unlucky.assignment3.R;
import com.unlucky.assignment3.shoe.Shoe;
import com.unlucky.assignment3.user.buyer.BuyerMain;
import com.unlucky.assignment3.user.seller.SellerMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {

    Button buyerButton, sellerButton, signUpButton;
    EditText usernameEditText, passwordEditText, phoneEditText, emailEditText;
    boolean isBuyer = true;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        buyerButton = findViewById(R.id.buyer_button);
        sellerButton = findViewById(R.id.seller_button);
        signUpButton = findViewById(R.id.signUpButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

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

        signUpButton.setOnClickListener(v -> {
            if (isValid()) {
                signUp();
            }
        });
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValid() {
        boolean isValid = true;

        String userName = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if(userName.length() == 0) {
            usernameEditText.setError("Username is required");
            isValid = false;
        }

        if (password.length() == 0) {
            passwordEditText.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordEditText.setError("Password is too short");
            isValid = false;
        }

        if (phone.length() == 0) {
            phoneEditText.setError("Phone number is required");
            isValid = false;
        } else if (!isValidPhoneNumber(phone)) {
            phoneEditText.setError("Phone number is not valid");
            isValid = false;
        }

        if (email.length() == 0) {
            emailEditText.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailEditText.setError("Email is not valid");
            isValid = false;
        }

        return  isValid;
    }

    public static boolean isValidPhoneNumber(String str) {
        // 10 digit phone number
        String phoneRegex = "0[0-9]{9}";

        return (str.matches(phoneRegex));
    }

    public void signUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    switchUserType();
                } else {
                    emailEditText.setError("User already exist!!!");
                    Toast.makeText(SignUpPage.this, "User already exist!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void switchUserType() {
        currentUser = auth.getCurrentUser();
        Map<String, Object> user = new HashMap<>();

        String email = emailEditText.getText().toString();

        user.put("name", usernameEditText.getText().toString());
        user.put("password", passwordEditText.getText().toString());
        user.put("phone", phoneEditText.getText().toString());
        user.put("email", emailEditText.getText().toString());


        db.collection("users").document(email).set(user);
        Intent i;
        if (isBuyer) {
            i = new Intent(SignUpPage.this, BuyerMain.class);
            startActivity(i);
        } else {
            ArrayList<Shoe> shoeList = new ArrayList<>();

            Intent searchIntent =
                    new Intent(SignUpPage.this,
                            SellerMain.class);

            searchIntent.putExtra("shoe_list", shoeList);
            startActivity(searchIntent);
        }
        finish();
    }
}