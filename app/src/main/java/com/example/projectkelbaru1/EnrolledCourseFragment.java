package com.example.projectkelbaru1;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnrolledCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnrolledCourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View bottomNavigationView;
    private Spinner spinnerCourses;
    private DatabaseHelper databaseHelper;
    private int sid;
    private boolean isCheckboxAdded=false;
    private TableLayout tableLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isSpinnerAdded=false;
    private CourseDatabaseHelper courseDatabaseHelper;
    private List<CourseData> courseList;
    private int semesterType;
    public EnrolledCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnrolledCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnrolledCourseFragment newInstance(String param1, String param2) {
        EnrolledCourseFragment fragment = new EnrolledCourseFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enrolled_course, container, false);
        // Inflate the layout for this fragment
        spinnerCourses = view.findViewById(R.id.spinnerEnrollCourses);
        databaseHelper = new DatabaseHelper(requireContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sid = sharedPreferences.getInt("sid", 0);
        courseDatabaseHelper = new CourseDatabaseHelper(requireContext());
        List<String> courseList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            courseList.add(String.valueOf(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courseList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(spinnerAdapter);
        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                semesterType = Integer.parseInt(selectedItem);
                List<CourseData> courseList = courseDatabaseHelper.selectBySemesterAndSid(semesterType, sid);

                displayEnrolledCourses(courseList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when nothing is selected in the spinner
                // Implement your logic here
            }
        });
        tableLayout = view.findViewById(R.id.tableLayout);
        TextView deleteCourseTextView = view.findViewById(R.id.delete_course_textview);
        deleteCourseTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isCheckboxAdded) {
                    removeCheckboxColumn();
                } else {
                    addCheckboxColumn();
                }
                //isCheckboxAdded = !isCheckboxAdded;
            }
        });
        Button addRowButton = view.findViewById(R.id.addRowButton);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckboxAdded){
                    deleteSelectedCourses(); // Call the deleteSelectedCourses method
                }
                else {
                    updateGrades();
                }

                // Perform any other actions you want to take when the button is clicked
                // ...
            }
        });
        return view;
    }
    private void updateGrades() {
        TableLayout tableLayout = requireView().findViewById(R.id.tableLayout);

        int gradeColumnIndex = 3; // Index of the grade column in the table

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            sid = sharedPreferences.getInt("sid", 0);
            // Get the spinner from the appropriate column
            Spinner spinner = (Spinner) tableRow.getChildAt(gradeColumnIndex);

            // Get the selected grade from the spinner
            int selectedGrade = spinner.getSelectedItemPosition();

            // Get the course code from the first column
            TextView courseCodeTextView = (TextView) tableRow.getChildAt(0);
            String courseCode = courseCodeTextView.getText().toString();

            // Update the grade in the database using sid and course code
            courseDatabaseHelper.updateGrade(sid, courseCode, selectedGrade);
        }
    }

    private void displayEnrolledCourses(List<CourseData> enrolledCourses) {
        TableLayout tableLayout = requireView().findViewById(R.id.tableLayout);
        tableLayout.removeAllViews(); // Clear existing table rows

        // Add header row with column labels
        TableRow headerRow = new TableRow(requireContext());

        TextView textViewHeaderCourseCode = new TextView(requireContext());
        textViewHeaderCourseCode.setText("Course Code");
        textViewHeaderCourseCode.setPadding(8, 8, 8, 8);

        TextView textViewHeaderCourseName = new TextView(requireContext());
        textViewHeaderCourseName.setText("Course Name");
        textViewHeaderCourseName.setPadding(8, 8, 8, 8);

        TextView textViewHeaderCourseCredit = new TextView(requireContext());
        textViewHeaderCourseCredit.setText("Credit");
        textViewHeaderCourseCredit.setPadding(8, 8, 8, 8);



        headerRow.addView(textViewHeaderCourseCode);
        headerRow.addView(textViewHeaderCourseName);
        headerRow.addView(textViewHeaderCourseCredit);

        tableLayout.addView(headerRow);
        String[] spinnerValues = {"NULL","A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "E", "F"};
        // Add individual course rows
        for (CourseData course : enrolledCourses) {
            String courseCode = course.getCourseCode();
            Course courses = databaseHelper.getCourseDetailsByCode(courseCode);
            String courseName = courses.getName(); // Replace with your logic to fetch the course name
            int courseCredit = courses.getCredit(); // Replace with your logic to fetch the course credit
            int grade = course.getGradeCode();

            TableRow tableRow = new TableRow(requireContext());

            TextView textViewCourseCode = new TextView(requireContext());
            textViewCourseCode.setText(courseCode);
            textViewCourseCode.setPadding(17, 17, 17, 17);
            textViewCourseCode.setBackgroundResource(R.drawable.cell_border);

            TextView textViewCourseName = new TextView(requireContext());
            textViewCourseName.setText(courseName);
            textViewCourseName.setPadding(17, 17, 17, 17);
            textViewCourseName.setBackgroundResource(R.drawable.cell_border);

            TextView textViewCourseCredit = new TextView(requireContext());
            textViewCourseCredit.setText(String.valueOf(courseCredit));
            textViewCourseCredit.setPadding(17, 17, 17, 17);
            textViewCourseCredit.setBackgroundResource(R.drawable.cell_border);




            Spinner spinner = new Spinner(requireContext());

            spinner.setPadding(12, 12, 12, 12);
            spinner.setBackgroundResource(R.drawable.cell_border);

            // Create an array adapter with spinnerValues
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerValues);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinner.setSelection(grade);//index 0 of spinneradapter
            tableRow.addView(textViewCourseCode);
            tableRow.addView(textViewCourseName);
            tableRow.addView(textViewCourseCredit);
            tableRow.addView(spinner);
            tableLayout.addView(tableRow);
        }
        TextView textViewHeaderSpinners = new TextView(requireContext());
        textViewHeaderSpinners.setText("Grade");
        textViewHeaderSpinners.setPadding(8, 8, 8, 8);
        TableRow headerRows = (TableRow) tableLayout.getChildAt(0);
        headerRows.addView(textViewHeaderSpinners, 3); // Add after the "Credit" column
    }
    private void addCheckboxColumn() {
        // Get the index of the "Course Code" column
        int courseCodeColumnIndex = 0;

        // Add the "checkbox" column header
        TextView textViewHeaderCheckbox = new TextView(requireContext());
        textViewHeaderCheckbox.setText("Checkbox");
        textViewHeaderCheckbox.setPadding(8, 8, 8, 8);

        TableRow headerRow = (TableRow) tableLayout.getChildAt(0);
        headerRow.addView(textViewHeaderCheckbox, courseCodeColumnIndex + 4); // Add after the "Course Code" column

        // Add the checkboxes to each row
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);

            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setPadding(8, 8, 8, 8);
            checkBox.setBackgroundResource(R.drawable.cell_border);
            tableRow.addView(checkBox, courseCodeColumnIndex + 4); // Add after the "Course Code" column
        }
        isCheckboxAdded = true;
    }
    private void deleteSelectedCourses() {
        if (!isCheckboxAdded) {
            return;
        }

        TableLayout tableLayout = requireView().findViewById(R.id.tableLayout);
        List<CourseData> courseList = courseDatabaseHelper.selectBySemesterAndSid(semesterType, sid);

        int checkboxColumnIndex = 4; // Index of the checkbox column in the table

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);

            CheckBox checkBox = (CheckBox) tableRow.getChildAt(checkboxColumnIndex);

            if (checkBox.isChecked()) {
                // Get the course code from the appropriate column
                TextView courseCodeTextView = (TextView) tableRow.getChildAt(0);
                String courseCode = courseCodeTextView.getText().toString();

                // Check if the courseCode is a valid string
                try {
                    // Delete the course from the database using sid and course code
                    courseDatabaseHelper.deleteBySidAndCourseCode(sid, courseCode);

                    // Remove the row from the table layout
                    tableLayout.removeView(tableRow);
                    i--; // Adjust the loop counter since the table row is removed
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Handle the case where the courseCode is not a valid string
                }
            }
        }

        removeCheckboxColumn();
    }




    private void removeCheckboxColumn() {
        TableLayout tableLayout = requireView().findViewById(R.id.tableLayout);

        // Ensure the table layout has at least one child
        if (tableLayout.getChildCount() > 0) {
            // Remove the "Checkbox" header from the header row
            TableRow headerRow = (TableRow) tableLayout.getChildAt(0);
            // Ensure the header row has at least four child views
            if (headerRow.getChildCount() > 3) {
                headerRow.removeViewAt(4); // Remove the view at index 3 (assuming "Checkbox" column is at index 3)
            }

            // Remove the checkboxes from each row
            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                // Ensure each table row has at least four child views
                if (tableRow.getChildCount() > 3) {
                    tableRow.removeViewAt(4); // Remove the view at index 3 (assuming "Checkbox" column is at index 3)
                }
            }
        }

        isCheckboxAdded = false; // Update the flag
    }

















}