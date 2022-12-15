package com.unlucky.assignment3.ui.welcome.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;

public class SignInDataViewModel extends ViewModel {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public SignInDataViewModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SignInDataViewModel(String username, String password, @NonNull Closeable... closeables) {
        super(closeables);
        this.username = username;
        this.password = password;
    }
}
