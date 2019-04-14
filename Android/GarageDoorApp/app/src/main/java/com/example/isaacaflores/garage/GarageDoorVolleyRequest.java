package com.example.isaacaflores.garage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

public class GarageDoorVolleyRequest
{
    public static String TAG = "GarageDoorVolleyRequest";
    private static int buttonViewColor;

    public GarageDoorVolleyRequest(Activity context)
    {
        buttonViewColor = 0;
    }

    public static class toggleRequestOnClick implements View.OnClickListener
    {

        @Override
        public void onClick( final View view)
        {
            final View toggleButtonView;
            toggleButtonView = view.findViewById(R.id.toggleRequestButton);
            toggleButtonView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

            String serverUrl = sharedPreferences.getString("server_address", "");
            String apiMapping = sharedPreferences.getString("toggleRequestMapping", "");

            if (serverUrl.contentEquals(view.getResources().getString(R.string.pref_default_server_address)))
            {
                Toast.makeText(view.getContext(), "Update your server information in my network settings.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl+apiMapping, new Response.Listener<String>()
                    {
                    @Override
                    public void onResponse(String response)
                    {

                        //Toggle button color only when we get a response from the HTTPS server
                        if (buttonViewColor == 0) {
                            toggleButtonView.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
                            buttonViewColor = 1;
                        } else {
                            toggleButtonView.setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
                            buttonViewColor = 0;
                        }
                    }
                }, new Response.ErrorListener()
                    {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, error.getMessage());
                        Log.e(TAG, String.valueOf(error.getStackTrace()));
                        toggleButtonView.setBackgroundColor(Color.BLACK);
                        Toast.makeText(view.getContext(), "Request failed.", Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String username = sharedPreferences.getString("server_username", "");
                        String password = sharedPreferences.getString("server_password", "");
                        String credentials = username + ":" + password;
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
    }

    ///Checks for a valid connection to the server.
    /// Changes image button to green (valid connection) or red (no connection)
    public static class mqttBridgeStatusRequest implements View.OnClickListener
    {

        @Override
        public void onClick(final View view) {
            final FloatingActionButton mqttBridgeStatusButton = (FloatingActionButton) view.findViewById(R.id.mqttBridgeStatusButton);
            mqttBridgeStatusButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(view.getContext());

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            String serverUrl = sharedPreferences.getString("server_address", "");
            String apiMapping = sharedPreferences.getString("mqttBridge", "");

            if (serverUrl.contentEquals(view.getResources().getString(R.string.pref_default_display_mqtt_bridge_status_request)))
                Toast.makeText(view.getContext(), "Update your server information in my network settings.", Toast.LENGTH_SHORT).show();
            else {

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl + apiMapping, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //valid connection change button to green
                        mqttBridgeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //No valid connection change button to black
                        Log.d(TAG, error.getMessage());
                        Log.d(TAG, String.valueOf(error.getStackTrace()));
                        mqttBridgeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        Toast.makeText(view.getContext(), "Request failed.", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String username = sharedPreferences.getString("server_username", "");
                        String password = sharedPreferences.getString("server_password", "");
                        String credentials = username + ":" + password;
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
    }

    public static class doorSensorRequest implements View.OnClickListener
    {
        @Override
        public void onClick(final View view) {
            final ImageView doorSensorView;

            doorSensorView = (ImageView) view.findViewById(R.id.doorSensorRequestButton);
            doorSensorView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

            String serverUrl = sharedPreferences.getString("server_address", "");
            String apiMapping = sharedPreferences.getString("doorSensorRequestMapping", "");

            if (serverUrl.contentEquals(view.getResources().getString(R.string.pref_default_display_door_sensor_request)))
                Toast.makeText(view.getContext(), "Update your server information in my network settings.", Toast.LENGTH_SHORT).show();
            else {
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl + apiMapping, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "sensor response: " + response);
                        //valid connection change button to green
                        if (response.equals("open"))
                            doorSensorView.setBackgroundColor(Color.RED);
                        else if (response.equals("closed"))
                            doorSensorView.setBackgroundColor(Color.GREEN);
                        else
                            doorSensorView.setBackgroundColor(Color.BLUE);
                    }
                }

                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //No valid connection change button to gray
                        Log.d(TAG, error.getMessage());
                        Log.d(TAG, String.valueOf(error.getStackTrace()));
                        doorSensorView.setBackgroundColor(Color.BLACK);
                        Toast.makeText(view.getContext(), "Request failed.", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String username = sharedPreferences.getString("server_username", "");
                        String password = sharedPreferences.getString("server_password", "");
                        String credentials = username + ":" + password;
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

                queue.add(stringRequest);
            }
        }
    }

}
