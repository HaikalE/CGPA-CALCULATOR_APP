package com.example.projectkelbaru1.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectkelbaru1.Course;
import com.example.projectkelbaru1.CourseData;
import com.example.projectkelbaru1.CourseDatabaseHelper;
import com.example.projectkelbaru1.DatabaseHelper;
import com.example.projectkelbaru1.R;
import com.example.projectkelbaru1.databinding.FragmentNotificationsBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;
    private CourseDatabaseHelper databaseHelper;
    private DatabaseHelper databaseHelperC;
    private TextView gpaText;
    private TextView cgpaText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        databaseHelper = new CourseDatabaseHelper(requireContext());
        databaseHelperC = new DatabaseHelper(requireContext());
        // Set up the spinner
        Spinner semesterSpinner = binding.semesterSpinner;
        List<Integer> semesterValues = getSemesterValues();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, semesterValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(adapter);
        semesterSpinner.setOnItemSelectedListener(this);
        //CGPA
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int sid = sharedPreferences.getInt("sid", 0);
        int totCredit = 0;
        int totScore = 0;
        for (int sem=1;sem<=8;sem++){

            List<CourseData> courseList = databaseHelper.selectBySemesterAndSid(sem, sid);
            for (CourseData course : courseList){
                Course courseDetails = databaseHelperC.getCourseDetailsByCode(course.getCourseCode());
                if (courseDetails != null) {
                    int credit =courseDetails.getCredit();
                    totCredit+=credit;
                    totScore+=getsGradeLabel(course.getGradeCode())*credit;
                }
            }
            float calculatedGpa;
            if (totCredit != 0) {
                calculatedGpa = (float) totScore / totCredit;
            } else {
                calculatedGpa = 0.0f; // or any other default value
            }
            cgpaText = binding.textView8;
            String hasil = String.format("%.2f", calculatedGpa);
            cgpaText.setText(hasil);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<Integer> getSemesterValues() {
        List<Integer> semesterValues = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            semesterValues.add(i);
        }
        return semesterValues;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectedSemester = (int) parent.getItemAtPosition(position);
        notificationsViewModel.setSelectedSemester(selectedSemester);

        // Query the database for courses based on selected semester and sid
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int sid = sharedPreferences.getInt("sid", 0);
        List<CourseData> courseList = databaseHelper.selectBySemesterAndSid(selectedSemester, sid);
        int gpa = 0;
        int credit = 0;
        for (CourseData course : courseList) {
            int grade = course.getGradeCode();
            Course courseDetails = databaseHelperC.getCourseDetailsByCode(course.getCourseCode());
            if (courseDetails != null) {
                int courseCredit = courseDetails.getCredit();
                credit+=courseCredit;
                gpa+=getsGradeLabel(grade)*courseCredit;
            }
        }
        float calculatedGpa;
        if (credit != 0) {
            calculatedGpa = (float) gpa / credit;
        } else {
            calculatedGpa = 0.0f; // or any other default value
        }
        gpaText = binding.textView6;
        String hasil = String.format("%.2f", calculatedGpa);
        gpaText.setText(hasil);
        TextView deanText = binding.textView4;
        if (calculatedGpa>=3.5){
            deanText.setText("YES");
        }else {
            deanText.setText("NO");
        }

        // Clear the existing table rows
        TableLayout tableLayout = binding.tableLayout;
        tableLayout.removeAllViews();

        // Create the header row
        TableRow headerRow = new TableRow(requireContext());
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView headerCourseCodeTextView = new TextView(requireContext());
        headerCourseCodeTextView.setText("Course Code");
        headerCourseCodeTextView.setBackgroundResource(R.drawable.cell_border);
        headerCourseCodeTextView.setPadding(8, 8, 8, 8);
        headerRow.addView(headerCourseCodeTextView);

        TextView headerNameTextView = new TextView(requireContext());
        headerNameTextView.setText("Course Name");
        headerNameTextView.setBackgroundResource(R.drawable.cell_border);
        headerNameTextView.setPadding(8, 8, 8, 8);
        headerRow.addView(headerNameTextView);

        TextView headerCreditTextView = new TextView(requireContext());
        headerCreditTextView.setText("Credit");
        headerCreditTextView.setBackgroundResource(R.drawable.cell_border);
        headerCreditTextView.setPadding(8, 8, 8, 8);
        headerRow.addView(headerCreditTextView);

        TextView headerScoreTextView = new TextView(requireContext());
        headerScoreTextView.setText("Score");
        headerScoreTextView.setBackgroundResource(R.drawable.cell_border);
        headerScoreTextView.setPadding(8, 8, 8, 8);
        headerRow.addView(headerScoreTextView);

        tableLayout.addView(headerRow);
        // Create table rows dynamically and populate the table
        for (CourseData course : courseList) {
            TableRow tableRow = new TableRow(requireContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            String courseCode = course.getCourseCode();
            TextView courseCodeTextView = new TextView(requireContext());
            courseCodeTextView.setText(courseCode);
            courseCodeTextView.setBackgroundResource(R.drawable.cell_border);
            courseCodeTextView.setPadding(17, 17, 17, 17);
            tableRow.addView(courseCodeTextView);

            TextView nameTextView = new TextView(requireContext());
            // Set the name value for the course
            Course courseDetails = databaseHelperC.getCourseDetailsByCode(courseCode);
            if (courseDetails != null) {
                String courseName = courseDetails.getName();
                nameTextView.setPadding(17, 17, 17, 17);
                nameTextView.setBackgroundResource(R.drawable.cell_border);
                nameTextView.setText(courseName);
            }
            tableRow.addView(nameTextView);

            TextView creditTextView = new TextView(requireContext());
            // Set the credit value for the course
            creditTextView.setText(String.valueOf(courseDetails.getCredit()));
            creditTextView.setPadding(17, 17, 17, 17);
            creditTextView.setBackgroundResource(R.drawable.cell_border);
            tableRow.addView(creditTextView);

            TextView scoreTextView = new TextView(requireContext());
            // Set the score value for the course
            int grade = course.getGradeCode();
            scoreTextView.setText(String.valueOf(getGradeLabel(grade)));
            scoreTextView.setPadding(17, 17, 17, 17);
            scoreTextView.setBackgroundResource(R.drawable.cell_border);
            tableRow.addView(scoreTextView);

            tableLayout.addView(tableRow);
        }
    }
    private float getsGradeLabel(int gradeCode) {
        float gradeLabel;
        switch (gradeCode) {
            case 1:
            case 2:
                gradeLabel = 4.0f;
                break;
            case 3:
                gradeLabel = 3.67f;
                break;
            case 4:
                gradeLabel = 3.33f;
                break;
            case 5:
                gradeLabel = 3.0f;
                break;
            case 6:
                gradeLabel = 2.67f;
                break;
            case 7:
                gradeLabel = 2.33f;
                break;
            case 8:
                gradeLabel = 2.00f;
                break;
            case 9:
                gradeLabel = 1.67f;
                break;
            case 10:
                gradeLabel = 1.33f;
                break;
            case 11:
                gradeLabel = 1.00f;
                break;
            case 12:
                gradeLabel = 0.67f;
                break;
            default:
                gradeLabel = 0.0f; // Or any other suitable default value
                break;
        }
        return gradeLabel;
    }

    private String getGradeLabel(int gradeCode) {
        String gradeLabel;
        switch (gradeCode) {
            case 0:
                gradeLabel = "NULL";
                break;
            case 1:
                gradeLabel = "A+";
                break;
            case 2:
                gradeLabel = "A";
                break;
            case 3:
                gradeLabel = "A-";
                break;
            case 4:
                gradeLabel = "B+";
                break;
            case 5:
                gradeLabel = "B";
                break;
            case 6:
                gradeLabel = "B-";
                break;
            case 7:
                gradeLabel = "C+";
                break;
            case 8:
                gradeLabel = "C";
                break;
            case 9:
                gradeLabel = "C-";
                break;
            case 10:
                gradeLabel = "D+";
                break;
            case 11:
                gradeLabel = "D";
                break;
            case 12:
                gradeLabel = "E";
                break;
            default:
                gradeLabel = "F";
                break;
        }
        return gradeLabel;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
