package com.example.cookmastermobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CoursesActivity extends AppCompatActivity {

    private String bearerToken;
    private String userId;
    private ListView listView;



    private SharedPreferences authPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        authPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        bearerToken = authPreferences.getString("token", null);
        userId = authPreferences.getString("id", null);

        listView = findViewById(R.id.lv_courses);

        //Toast.makeText(CoursesActivity.this, userId, Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected course
                Course selectedCourse = (Course) parent.getItemAtPosition(position);

                // Start the details activity and pass the selected course as an extra
                Intent intent = new Intent(CoursesActivity.this, CourseDetailsActivity.class);
                intent.putExtra("selectedCourse", selectedCourse);
                startActivity(intent);
            }
        });

        // Make the API request
        makeApiRequest();
    }

    private void makeApiRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Api.COURSES_URL + userId);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method and headers
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer " + bearerToken);

                    // Get the response code
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        // Read the response
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Parse the JSON response
                        JSONArray jsonArray = new JSONArray(response.toString());

                        // Extract data from the JSON array
                        final List<Course> courses = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String title = jsonObject.getString("title");
                            String description = jsonObject.getString("description");
                            boolean validated = jsonObject.getBoolean("validated");
                            String startDate = jsonObject.getString("startDate");
                            String endDate = jsonObject.getString("endDate");
                            String place = jsonObject.getString("place");
                            courses.add(new Course(id, title, description, validated, startDate, endDate, place));
                        }

                        // Update the UI with the response data
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Create a custom ArrayAdapter to display the courses in the ListView
                                ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(
                                        CoursesActivity.this,
                                        android.R.layout.simple_list_item_2,
                                        android.R.id.text1,
                                        courses
                                ) {
                                    @NonNull
                                    @Override
                                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView text1 = view.findViewById(android.R.id.text1);
                                        TextView text2 = view.findViewById(android.R.id.text2);

                                        // Set the course title as the first line and description as the second line
                                        Course course = getItem(position);
                                        text1.setText(course.getTitle());
                                        text2.setText(course.getDescription());

                                        return view;
                                    }
                                };

                                // Set the adapter for the ListView
                                listView.setAdapter(adapter);
                            }
                        });
                    } else {
                        System.out.println("Request failed. Response Code: " + responseCode);
                    }

                    // Close the connection
                    connection.disconnect();

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
