package com.example.projectkelbaru1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectkelbaru1.R;
import com.example.projectkelbaru1.UserDatabaseHelper;

public class SignupFragment extends Fragment {

    private EditText editTextSID, editTextName, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private UserDatabaseHelper userDbHelper;
    private TextView textViewRegisterAccount;
    private NavController navController;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDbHelper = new UserDatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        editTextSID = view.findViewById(R.id.editTextSID);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        textViewRegisterAccount = view.findViewById(R.id.textViewLoginAccount);
        textViewRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the login activity
                navController.navigate(R.id.login_fragment);
            }
        });
        buttonRegister = view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values from the EditText fields
                int sid = Integer.parseInt(editTextSID.getText().toString());
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Add the user to the database
                boolean isUserAdded = userDbHelper.addUser(sid, name, email, password);
                if (isUserAdded) {
                    // User added successfully
                    Toast.makeText(requireContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                    // You can add further logic or navigate to another fragment/activity
                } else {
                    // Failed to add user
                    Toast.makeText(requireContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                    // Handle the failure scenario
                }
            }
        });
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        return view;
    }
}
