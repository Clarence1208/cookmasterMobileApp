package com.example.cookmastermobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class CourseDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private CheckBox checkBox;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView durationTextView;
    private TextView placeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // Get the selected course from the intent
        Course selectedCourse = getIntent().getParcelableExtra("selectedCourse");

        // Find the TextViews in the layout
        titleTextView = findViewById(R.id.tv_title);
        descriptionTextView = findViewById(R.id.tv_description);
        checkBox = findViewById(R.id.cb_is_completed);
        startDateTextView = findViewById(R.id.tv_start_date);
        endDateTextView = findViewById(R.id.tv_end_date);
        durationTextView = findViewById(R.id.tv_nb_hours);
        placeTextView = findViewById(R.id.tv_place);

        // Display the details of the selected course
        if (selectedCourse != null) {
            titleTextView.setText(selectedCourse.getTitle());
            descriptionTextView.setText(selectedCourse.getDescription());
            //add to the current text of the textview
            descriptionTextView.append("   " + selectedCourse.getStartDate());
            checkBox.setChecked(selectedCourse.isCompleted());
            /*startDateTextView.setText(selectedCourse.getStartDate().toString());
            endDateTextView.setText(selectedCourse.getEndDate().toString());
            // calculate the duration of the course in hours
            long duration = selectedCourse.getEndDate().getTime() - selectedCourse.getStartDate().getTime();
            durationTextView.setText(String.valueOf(duration / (1000 * 60 * 60)));*/
            placeTextView.setText(selectedCourse.getPlace());

        }
    }
}