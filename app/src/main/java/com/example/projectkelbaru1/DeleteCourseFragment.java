package com.example.projectkelbaru1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteCourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private List<String> selectedCourses = new ArrayList<>();
    private Spinner spinnerCourses;

    private DatabaseHelper databaseHelper;
    private String mParam2;

    public DeleteCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteCourseFragment newInstance(String param1, String param2) {
        DeleteCourseFragment fragment = new DeleteCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private void populateTable(List<Course> courses) {
        TableLayout tableLayout = getView().findViewById(R.id.tableLayout);

        // Clear existing table rows
        tableLayout.removeAllViews();

        TableRow headerRow = new TableRow(requireContext());
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // Create header TextViews
        TextView headerCourseCodeTextView = new TextView(requireContext());
        headerCourseCodeTextView.setText("Course Code");
        headerCourseCodeTextView.setBackgroundResource(R.drawable.cell_border);
        headerCourseCodeTextView.setPadding(17, 17, 17, 17);
        headerRow.addView(headerCourseCodeTextView);

        TextView headerCourseNameTextView = new TextView(requireContext());
        headerCourseNameTextView.setText("Name");
        headerCourseNameTextView.setBackgroundResource(R.drawable.cell_border);
        headerCourseNameTextView.setPadding(17, 17, 17, 17);
        headerRow.addView(headerCourseNameTextView);

        TextView headerCreditHoursTextView = new TextView(requireContext());
        headerCreditHoursTextView.setText("Credit");
        headerCreditHoursTextView.setBackgroundResource(R.drawable.cell_border);
        headerCreditHoursTextView.setPadding(17, 17, 17, 17);
        headerRow.addView(headerCreditHoursTextView);

        TextView headerCheckboxTextView = new TextView(requireContext());
        headerCheckboxTextView.setText("Checkbox");
        headerCheckboxTextView.setBackgroundResource(R.drawable.cell_border);
        headerCheckboxTextView.setPadding(17, 17, 17, 17);
        headerRow.addView(headerCheckboxTextView);

        // Add the header row to the table layout
        tableLayout.addView(headerRow);

        // Create and add new table rows for each course
        for (Course course : courses) {
            TableRow tableRow = new TableRow(requireContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Create TextViews for each course attribute
            TextView courseCodeTextView = new TextView(requireContext());
            courseCodeTextView.setText(course.getCourseCode());
            courseCodeTextView.setBackgroundResource(R.drawable.cell_border);
            courseCodeTextView.setPadding(17, 17, 17, 17);
            tableRow.addView(courseCodeTextView);

            TextView courseNameTextView = new TextView(requireContext());
            courseNameTextView.setText(course.getName());
            courseNameTextView.setBackgroundResource(R.drawable.cell_border);
            courseNameTextView.setPadding(17, 17, 17, 17);
            tableRow.addView(courseNameTextView);

            TextView creditHoursTextView = new TextView(requireContext());
            creditHoursTextView.setText(String.valueOf(course.getCredit()));
            creditHoursTextView.setBackgroundResource(R.drawable.cell_border);
            creditHoursTextView.setPadding(17, 17, 17, 17);
            tableRow.addView(creditHoursTextView);

            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Add the course code to the list of selected courses
                        selectedCourses.add(course.getCourseCode());
                    } else {
                        // Remove the course code from the list of selected courses
                        selectedCourses.remove(course.getCourseCode());
                    }
                }
            });
            checkBox.setBackgroundResource(R.drawable.cell_border);
            checkBox.setPadding(17, 17, 17, 17);
            tableRow.addView(checkBox);

            // Add the table row to the table layout
            tableLayout.addView(tableRow);
        }
    }

    private void deleteSelectedCourses() {
        databaseHelper = new DatabaseHelper(getContext());

        for (String courseCode : selectedCourses) {
            databaseHelper.deleteCourseByCode(courseCode);
        }

        // Refresh the table view after deletion
        int semesterType = spinnerCourses.getSelectedItemPosition() + 1;
        List<Course> courses = databaseHelper.getCoursesBySemester(semesterType);
        populateTable(courses);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_course, container, false);

        spinnerCourses = view.findViewById(R.id.spinnerCourses);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, getSemesterOptions());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Button deleteButton = view.findViewById(R.id.addRowButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the selected courses
                deleteSelectedCourses();
            }
        });

        spinnerCourses.setAdapter(spinnerAdapter);
        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int semesterType = position + 1; // Add 1 to match the semester type
                databaseHelper = new DatabaseHelper(getContext());
                List<Course> courses = databaseHelper.getCoursesBySemester(semesterType);
                populateTable(courses);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle nothing selected if needed
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private String[] getSemesterOptions() {
        return new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    }

}