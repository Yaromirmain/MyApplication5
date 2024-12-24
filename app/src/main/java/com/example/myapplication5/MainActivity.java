package com.example.myapplication5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText countryInput;
    private Button searchButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryInput = findViewById(R.id.countryInput);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = countryInput.getText().toString();
                if (!country.isEmpty()) {
                    fetchCountryInfo(country);
                }
            }
        });
    }

    private void fetchCountryInfo(final String country) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection;
                URL url = null;
                try {
                    url = new URL("https://restcountries.com/v3.1/name/" + country.toLowerCase());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.connect();
                    Scanner sc = new Scanner(connection.getInputStream());
                    StringBuilder result = new StringBuilder();
                    while (sc.hasNextLine()) {
                        result.append(sc.nextLine());
                    }
                    final String jsonResponse = result.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(jsonResponse);
                                if (jsonArray.length() > 0) {
                                    JSONObject countryInfo = jsonArray.getJSONObject(0);
                                    JSONObject nameObject = countryInfo.getJSONObject("name");
                                    String countryName = nameObject.getString("common");
                                    JSONArray capitalArray = countryInfo.getJSONArray("capital");
                                    String capital = capitalArray.getString(0);
                                    String region = countryInfo.getString("region");
                                    resultTextView.setText("Страна: " + countryName + "\nСтолица: " + capital + "\nРегион: " + region);
                                } else {
                                    resultTextView.setText("Страна не найдена.");
                                }
                            } catch (JSONException e) {
                                resultTextView.setText("Error parsing JSON: " + e.getMessage());
                            }
                        }
                    });
                } catch (IOException e) {
                    final String errorMessage = "Что-то пошло не так, проверьте соединение: " + e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setText(errorMessage);
                        }
                    });
                }
            }
        }).start();
    }
}