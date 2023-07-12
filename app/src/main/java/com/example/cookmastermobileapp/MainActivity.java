package com.example.cookmastermobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cookmastermobileapp.Login;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// Import statements

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;
    private SharedPreferences authPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        emailField = findViewById(R.id.emailEditText);
        passwordField = findViewById(R.id.passwordEditText);
        authPreferences = getSharedPreferences("auth", MODE_PRIVATE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = emailField.getText().toString();
                String password = passwordField.getText().toString();
                new AuthenticateTask().execute(username, password);
                Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                startActivity(intent);
            }
        });
    }

    private class AuthenticateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            return authenticate(username, password);
        }

        @Override
        protected void onPostExecute(String accessToken) {
            if (accessToken != null) {
                //Toast.makeText(MainActivity.this, accessToken, Toast.LENGTH_SHORT).show();
                Log.d("token", accessToken);
            }
        }
    }

    private String authenticate(String username, String password) {
        try {
            URL url = new URL(Api.LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set the request parameters
            String grantType = URLEncoder.encode("password", "UTF-8");
            String clientId = URLEncoder.encode("web_app", "UTF-8");
            String encodedUsername = URLEncoder.encode(username, "UTF-8");
            String encodedPassword = URLEncoder.encode(password, "UTF-8");
            String parameters = "grant_type=" + grantType + "&client_id=" + clientId +
                    "&username=" + encodedUsername + "&password=" + encodedPassword;

            // Write the request parameters to the connection output stream
            connection.getOutputStream().write(parameters.getBytes());

            // Get the response from the connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response to extract the authentication token
            String jsonResponse = response.toString();
            // Extract the "access_token" value from the JSON response
            String accessToken = jsonResponse.substring(jsonResponse.indexOf(":") + 2, jsonResponse.indexOf(",") - 1);

            getLoginInfo(accessToken);

            authPreferences.edit().putString("token", accessToken).apply();
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getLoginInfo(String bearerToken){

        try {
            URL url = new URL(Api.PROFILE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method and headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(response.toString());

                // Extract data from the JSON object
                final String id = jsonObject.getString("id");
                final String login = jsonObject.getString("login");
                final String firstName = jsonObject.getString("firstName");
                final String lastName = jsonObject.getString("lastName");
                final String email = jsonObject.getString("email");
                final boolean activated = jsonObject.getBoolean("activated");
                final String langKey = jsonObject.getString("langKey");
                final String createdDate = jsonObject.getString("createdDate");
                final String lastModifiedDate = jsonObject.getString("lastModifiedDate");
                final String authorities = jsonObject.getJSONArray("authorities").toString();

                // write to shared preferences
                authPreferences.edit().putString("id", id).apply();
                authPreferences.edit().putString("login", login).apply();
                authPreferences.edit().putString("firstName", firstName).apply();
                authPreferences.edit().putString("lastName", lastName).apply();
                authPreferences.edit().putString("email", email).apply();
                authPreferences.edit().putBoolean("activated", activated).apply();
                authPreferences.edit().putString("langKey", langKey).apply();
                authPreferences.edit().putString("authorities", authorities).apply();

            } else {
                System.out.println("Request failed. Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
