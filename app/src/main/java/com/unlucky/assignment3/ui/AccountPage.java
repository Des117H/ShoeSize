package com.unlucky.assignment3.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unlucky.assignment3.R;

import java.util.Map;

public class AccountPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    TextView nameBig, nameSmall, emailTV, phoneTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        currentUser = auth.getCurrentUser();

        Button back = findViewById(R.id.back);
        Button logOut = findViewById(R.id.logOut);

        nameBig = findViewById(R.id.nameBig);
        nameSmall = findViewById(R.id.nameSmall);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.Phone);

        db.collection("users")
                .document(currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> temp = document.getData();

                            nameBig.setText(temp.get("name").toString());
                            nameSmall.setText(temp.get("name").toString());
                            phoneTV.setText(temp.get("phone").toString());
                            emailTV.setText(temp.get("email").toString());
                        }
                    }
                });


        back.setOnClickListener(view -> finish());

        logOut.setOnClickListener(view -> {
            auth.signOut();
            if (auth.getCurrentUser() == null) {
                Toast.makeText(AccountPage.this, "Signed out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountPage.this,WelcomePage.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}