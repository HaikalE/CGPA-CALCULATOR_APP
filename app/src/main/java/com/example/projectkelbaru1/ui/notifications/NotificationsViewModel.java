package com.example.projectkelbaru1.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final List<Integer> semesterValues;
    private int selectedSemester;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        semesterValues = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            semesterValues.add(i);
        }
    }
    public List<Integer> getSemesterValues() {
        return semesterValues;
    }
    public void setSelectedSemester(int semester) {
        selectedSemester = semester;
    }
    public int getSelectedSemester() {
        return selectedSemester;
    }
    public LiveData<String> getText() {
        return mText;
    }
}