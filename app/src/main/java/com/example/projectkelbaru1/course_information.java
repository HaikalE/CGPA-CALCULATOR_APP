package com.example.projectkelbaru1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projectkelbaru1.databinding.ActivityMainBinding;

public class course_information extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Spinner semesterSpinner;
    private EditText courseCodeEditText;
    private EditText courseNameEditText;
    private EditText creditHoursEditText;

    private DatabaseHelper databaseHelper;
    private NavController navController;
    private ActivityMainBinding binding;

    public course_information() {
        // Required empty public constructor
    }

    public static course_information newInstance(String param1, String param2) {
        course_information fragment = new course_information();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_information, container, false);

        semesterSpinner = view.findViewById(R.id.semesterSpinner);
        courseCodeEditText = view.findViewById(R.id.courseCodeEditText);
        courseNameEditText = view.findViewById(R.id.courseNameEditText);
        creditHoursEditText = view.findViewById(R.id.creditHoursEditText);

        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getSemesterOptions());

        // Set the dropdown layout style
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the spinner
        semesterSpinner.setAdapter(spinnerAdapter);
        TextView deleteCourseTextView = view.findViewById(R.id.delete_course_textview);
        deleteCourseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for the "Delete course" TextView
                // Perform your delete course logic here
                // For example:
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(R.id.delete_course);
                Toast.makeText(getActivity(), "Delete course clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int semester = Integer.parseInt(semesterSpinner.getSelectedItem().toString());
                String courseCode = courseCodeEditText.getText().toString();
                String courseName = courseNameEditText.getText().toString();
                int creditHours = Integer.parseInt(creditHoursEditText.getText().toString());
                Course course = databaseHelper.getCourseDetailsByCode(courseCode);
                if (course!= null){
                    Toast.makeText(getActivity(), "Update data successfully", Toast.LENGTH_SHORT).show();
                    databaseHelper.updateCourse(courseCode, courseName, creditHours, semester);
                }
                else {
                    Toast.makeText(getActivity(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    databaseHelper.insertCourse(courseCode, courseName, creditHours, semester);
                }

                // Clear the form fields
                courseCodeEditText.setText("");
                courseNameEditText.setText("");
                creditHoursEditText.setText("");
            }
        });

        return view;
    }

    // Helper method to get the semester options
    private String[] getSemesterOptions() {
        return new String[]{"1", "2", "3",
                "4", "5", "6", "7", "8"};
    }
}
