package com.unlucky.assignment3.ui.fragment.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BuyerSignInViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public BuyerSignInViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
