package com.example.projectkelbaru1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class management_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_profile);
        setTitle("Management Profile");
        // Enable the back button in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Handle the click event of the back button in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  // Navigate back to the previous screen
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
