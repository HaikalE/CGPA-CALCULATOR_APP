package com.example.projectkelbaru1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projectkelbaru1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class course_registered extends Fragment {

    private TableLayout tableLayout;
    private Button addRowButton;
    private Spinner spinnerCourses;
    private CourseDatabaseHelper courseDatabaseHelper;
    private DatabaseHelper databaseHelper;
    private int sid;
    private BottomNavigationView bottomNavigationView;
    List<String> checkedCourseCodes, checkedNames, checkedCredits;
    private List<TableRow> checkedRows = new ArrayList<>();

    public course_registered() {
        // Required empty public constructor
    }

    public static course_registered newInstance() {
        return new course_registered();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_registered, container, false);
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sid = sharedPreferences.getInt("sid", 0);

        tableLayout = view.findViewById(R.id.tableLayout);
        spinnerCourses = view.findViewById(R.id.spinnerCourses);
        addRowButton = view.findViewById(R.id.addRowButton);
        Context context = requireContext();
        databaseHelper = new DatabaseHelper(context);
        TextView enrolledCourseTextView = view.findViewById(R.id.enrolled_course_textview);

        enrolledCourseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Event handling code goes here
                // This code will be executed when the text is clicked
                // You can perform any actions or start another activity, for example:
                Toast.makeText(getContext(), "Text clicked", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(R.id.course_enrol);
                // Start a new activity

            }
        });


        List<String> courseList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            courseList.add(String.valueOf(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courseList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(spinnerAdapter);

        courseDatabaseHelper = new CourseDatabaseHelper(context);


        checkedCourseCodes = new ArrayList<>();
        checkedNames = new ArrayList<>();
        checkedCredits = new ArrayList<>();
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int semesterType = Integer.parseInt(spinnerCourses.getSelectedItem().toString());

                List<TableRow> checkedRows = getCheckedRows();
                System.out.println("Checked Rows:");
                for (TableRow row : checkedRows) {
                    // Perform actions on each checked row
                    // For example, you can retrieve the TextViews within the row
                    TextView textViewCourseCode = (TextView) row.getChildAt(0);
                    TextView textViewName = (TextView) row.getChildAt(1);
                    TextView textViewCredit = (TextView) row.getChildAt(2);

                    // Retrieve the text values from the TextViews
                    String courseCode = textViewCourseCode.getText().toString();
                    String name = textViewName.getText().toString();
                    String credit = textViewCredit.getText().toString();
                    checkedCourseCodes.add(courseCode);
                    checkedNames.add(name);
                    checkedCredits.add(credit);
                    // Perform any further processing or actions with the checked row data
                    // ...
                }
                for (int i = 0; i < checkedCourseCodes.size(); i++) {
                    courseDatabaseHelper.insertCourse(sid,semesterType,checkedCourseCodes.get(i),null);
                }
                Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                int semesterType = Integer.parseInt(selectedItem) % 2;
                populateTable(semesterType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when no item is selected
            }
        });
        /*
        CheckBox checkBox = new CheckBox(requireContext());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TableRow row = (TableRow) buttonView.getParent();
                if (isChecked) {
                    checkedRows.add(row);
                } else {
                    checkedRows.remove(row);
                }
            }
        });
        */
        return view;
    }
    private List<TableRow> getCheckedRows() {
        List<TableRow> checkedRows = new ArrayList<>();

        int rowCount = tableLayout.getChildCount();
        for (int i = 1; i < rowCount; i++) { // Start from index 1 to skip the header row
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            CheckBox checkBox = (CheckBox) row.getChildAt(row.getChildCount() - 1);
            if (checkBox.isEnabled() && checkBox.isChecked()) { // Check if checkbox is enabled and checked
                checkBox.setEnabled(false); // Disable the checkbox
                checkedRows.add(row);
            }
        }

        return checkedRows;
    }






    private void populateTable(int semesterType) {
        tableLayout.removeAllViews();
        //

        // Add header row
        TableRow headerRow = new TableRow(requireContext());
        TextView headerCourseCode = new TextView(requireContext());
        headerCourseCode.setText("Course Code");
        headerCourseCode.setPadding(8, 8, 8, 8);
        //headerCourseCode.setTextStyleBold();
        headerRow.addView(headerCourseCode);

        TextView headerName = new TextView(requireContext());
        headerName.setText("Name");
        headerName.setPadding(8, 8, 8, 8);
        //headerName.setTextStyleBold();
        headerRow.addView(headerName);

        TextView headerCredit = new TextView(requireContext());
        headerCredit.setText("Credit");
        headerCredit.setPadding(8, 8, 8, 8);
        //headerCredit.setTextStyleBold();
        headerRow.addView(headerCredit);

        TextView headerCheckbox = new TextView(requireContext());
        headerCheckbox.setText("Checkbox");
        headerCheckbox.setPadding(8, 8, 8, 8);
        //headerCheckbox.setTextStyleBold();
        headerRow.addView(headerCheckbox);

        tableLayout.addView(headerRow);

        List<Course> courses = databaseHelper.getCoursesBySemester(semesterType);
        for (Course course : courses) {
            TableRow row = new TableRow(requireContext());



            TextView textViewCourseCode = new TextView(requireContext());
            textViewCourseCode.setText(course.getCourseCode());
            textViewCourseCode.setPadding(17, 17, 17, 17);
            textViewCourseCode.setBackgroundResource(R.drawable.cell_border);
            row.addView(textViewCourseCode);

            TextView textViewName = new TextView(requireContext());
            textViewName.setText(course.getName());
            textViewName.setPadding(17, 17, 17, 17);
            textViewName.setBackgroundResource(R.drawable.cell_border);
            row.addView(textViewName);

            TextView textViewCredit = new TextView(requireContext());
            textViewCredit.setText(String.valueOf(course.getCredit()));
            textViewCredit.setPadding(17, 17, 17, 17);
            textViewCredit.setBackgroundResource(R.drawable.cell_border);
            row.addView(textViewCredit);

            CheckBox checkBox = new CheckBox(requireContext());
            //checkBox.setPadding(8, 8, 8, 8);

            checkBox.setTextSize(14);
            checkBox.setBackgroundResource(R.drawable.cell_border);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            checkBox.setLayoutParams(layoutParams);

            boolean dataExists = false;
            try {
                dataExists = courseDatabaseHelper.selectByCourseCodeAndSid(course.getCourseCode(),sid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            checkBox.setChecked(dataExists);
            checkBox.setEnabled(!dataExists); // Disable checkbox if data exists
            // Disable checkbox if data exists

            row.addView(checkBox);
            tableLayout.addView(row);
        }
    }

}
