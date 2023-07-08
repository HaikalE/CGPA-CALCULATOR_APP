package com.example.projectkelbaru1.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.projectkelbaru1.R;
import com.example.projectkelbaru1.databinding.FragmentHomeBinding;

import java.lang.reflect.Array;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView listView;
    private NavController navController;
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = binding.listView;
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, homeViewModel.getList().getValue());
        listView.setAdapter(adapter);

        // Set item click listener
        // Set item click listener
        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                // Handle the click event here
                if (selectedItem.equals("Storing the information of registered courses for a particular semester")){
                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.course_registered);
                }
                else if (selectedItem.equals("Storing the information of courses")){
                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.course_information);
                }
                else if (selectedItem.equals("Storing the grade and its point")){
                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.course_grade);
                }
            }
        });



        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable back button callback
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                // For example, you can navigate back or perform any other action
                // Here, we navigate back to the previous fragment or finish the activity

                // If there is a previous destination, navigate back to it

                    // If there is no previous destination, you can either navigate to a different fragment or activity
                    // or perform any other action
                requireActivity().finish();

            }
        };
        // Add the callback to the activity's OnBackPressedDispatcher
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
