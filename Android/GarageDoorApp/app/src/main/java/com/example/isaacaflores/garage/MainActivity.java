package com.example.isaacaflores.garage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
{

    private static String TAG = "garage";
    public static final int SETTINGS_ACTIVITY_REQUEST = 0;
    private FloatingActionButton mqttBridgeStatusRequestButton;
    private ImageView doorSensorRequestButton;
    private View toggleRequestButton;

    private GarageDoorVolleyRequest garageDoorVolleyRequest;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        //App link handles
        Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
                Uri appLinkData = appLinkIntent.getData();

            //Invoke button onClick method to get new status on startup
        mqttBridgeStatusRequestButton = (FloatingActionButton) findViewById(R.id.mqttBridgeStatusButton);
        mqttBridgeStatusRequestButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //IndicatorCallback(indicatorButton);
        mqttBridgeStatusRequestButton.callOnClick();

        //Invoke button onClick method to get new status on startup
        doorSensorRequestButton = (ImageView) findViewById(R.id.doorSensorRequestButton);
        doorSensorRequestButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //DoorSensorCallback(doorSensorView);
        doorSensorRequestButton.callOnClick();

        toggleRequestButton = findViewById(R.id.toggleRequestButton);
        toggleRequestButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        garageDoorVolleyRequest = new GarageDoorVolleyRequest(this);

        //Set onClickListeners for all buttons
        toggleRequestButton.setOnClickListener(new GarageDoorVolleyRequest.toggleRequestOnClick());
        mqttBridgeStatusRequestButton.setOnClickListener(new GarageDoorVolleyRequest.mqttBridgeStatusRequest());
        doorSensorRequestButton.setOnClickListener(new GarageDoorVolleyRequest.doorSensorRequest());

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ////Invoke button onClick method to get new status when we return to app
        //IndicatorCallback(indicatorButton);
        mqttBridgeStatusRequestButton.callOnClick();
        doorSensorRequestButton.callOnClick();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Open Setting Activity
            openSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSettingsActivity()
    {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivityForResult(settingsIntent, SETTINGS_ACTIVITY_REQUEST );
    }
}


