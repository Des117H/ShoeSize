package com.unlucky.assignment3.ui.fragment.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unlucky.assignment3.R;

import com.unlucky.assignment3.databinding.FragmentSellerSignInBinding;

public class SellerSignInFragment extends Fragment {

    private FragmentSellerSignInBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SellerSignInViewModel SellerSignInViewModel =
                new ViewModelProvider(this).get(SellerSignInViewModel.class);
        binding = FragmentSellerSignInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        SellerSignInViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}