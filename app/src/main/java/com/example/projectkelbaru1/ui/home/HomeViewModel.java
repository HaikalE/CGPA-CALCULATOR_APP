package com.example.projectkelbaru1.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mList;

    public HomeViewModel() {
        mList = new MutableLiveData<>();
        List<String> items = new ArrayList<>();
        items.add("Storing the information of registered courses for a particular semester");
        items.add("Storing the information of courses");
        items.add("Storing the grade and its point");
        /*
        List<String> filteredItems = new ArrayList<>();
        for (String item : items) {
            if (item.equals("Storing the information of registered courses for a particular semester")) {
                filteredItems.add(item);
            }
        }
        */
        mList.setValue(items);
    }

    public LiveData<List<String>> getList() {
        return mList;
    }
}
