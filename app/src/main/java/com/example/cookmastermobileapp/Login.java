package com.example.cookmastermobileapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login {

    /*static String authenticate(String username, String password) {
        String endpoint = "http://localhost:9089/realms/jhipster/protocol/openid-connect/token";
       // RequestQueue requestQueue = Volley.newRequestQueue();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("grant_type", "password");
            requestBody.put("client_id", "web_app");
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response to extract the authentication token
                            String accessToken = response.getString("access_token");
                            // Handle the access token
                            // ...
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle the error
                        // ...
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

       // requestQueue.add(request);
        return null; // Return null for now, handle the access token in the onResponse callback
    }*/

    //protected StringRequest authRequest (String username, String password) {

}
