package com.example.projectkelbaru1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projectkelbaru1.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private SharedPreferences sharedPreferences;
    private NavController navController;
    private ActivityMainBinding binding;
    private UserDatabaseHelper userDbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(requireContext());
        userDbHelper = new UserDatabaseHelper(requireContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                // For example, you can navigate back or perform any other action
                // Here, we navigate back to the previous fragment or activity

                // If there is a previous destination, navigate back to it
                if (navController != null && navController.getPreviousBackStackEntry() != null) {
                    navController.popBackStack();
                    requireActivity().finish();
                } else {
                    // If there is no previous destination, you can either navigate to a different fragment or activity
                    // or perform any other action
                    requireActivity().onBackPressed();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        TextView textViewRegisterAccount = view.findViewById(R.id.textViewRegisterAccount);
        navController = NavHostFragment.findNavController(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                int sid = loginUser(email, password);
                if (sid != -1) {
                    // Login successful, display the sid value
                    //Toast.makeText(requireContext(), "Login successful! SID: " + sid, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("sid", sid);
                    editor.apply();
                    // Start the home page activity or any other activity
                    if (navController != null) {
                        navController.navigate(R.id.navigation_home);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
                        //

                        // Finish the login activity to prevent going back to it
                    }// Finish the login activity to prevent going back to it
                } else {
                    // Login failed, display an error message
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the register page activity
                navController.navigate(R.id.signup_fragment);
            }
        });
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        return view;
    }

    private int loginUser(String email, String password) {
        // Query the database for a user with matching email and password
        // Return the sid (student ID) if the user exists, -1 otherwise
        return userDbHelper.isUserExists(email, password);
    }

}
