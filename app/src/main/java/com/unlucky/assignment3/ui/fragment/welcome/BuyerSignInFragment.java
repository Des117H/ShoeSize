package com.unlucky.assignment3.ui.fragment.welcome;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.unlucky.assignment3.R;

import java.util.ArrayList;
import java.util.List;

public class BuyerSignInFragment extends Fragment {
    public interface OnDataPass {
        public void onDataPass(String data);
    }
    EditText usernameEditText, passwordEditText;
    public BuyerSignInFragment() {
        super(R.layout.fragment_buyer_sign_in);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_buyer_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEditText = view.findViewById(R.id.UsernameEditText);
        passwordEditText = view.findViewById(R.id.PasswordEditText);
    }

    public List<String> getSignInInfo() {
        List<String> data = new ArrayList<>();
        data.add(usernameEditText.getText().toString());
        data.add(passwordEditText.getText().toString());

        return data;
    }
}