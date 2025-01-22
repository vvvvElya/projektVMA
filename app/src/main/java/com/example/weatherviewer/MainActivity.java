package com.example.weatherviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText txt_input;
    private Button button;
    private TextView result_txt;
    private TextView result_two_txt;
    private TextView result_three_txt;
    private TextView result_four_txt;
    private TextView result_five_txt;
    private TextView result_six_txt;
    private TextView result_seven_txt;
    private TextView result_eight_txt;
    private TextView textViewlongitude;
    private TextView textViewlatitude;
    private TextView result_nine_txt;

    private TextView error;

    private ProgressBar bar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_input = findViewById(R.id.txt_input);
        button = findViewById(R.id.button);
        result_txt = findViewById(R.id.result_txt);
        result_two_txt = findViewById(R.id.result_two_txt);
        result_three_txt = findViewById(R.id.result_three_txt);
        result_four_txt = findViewById(R.id.result_four_txt);
        result_five_txt = findViewById(R.id.result_five_txt);
        result_six_txt = findViewById(R.id.result_six_txt);
        result_seven_txt = findViewById(R.id.result_seven_txt);
        result_eight_txt = findViewById(R.id.result_eight_txt);
        textViewlongitude = findViewById(R.id.longitude);
        textViewlatitude = findViewById(R.id.latitude);
        result_nine_txt = findViewById(R.id.result_nine_txt);
        bar = findViewById(R.id.progressBar);
        error = findViewById(R.id.errorField);

        bar.setVisibility(View.INVISIBLE);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_input.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.MainText, Toast.LENGTH_LONG).show();
                else {
                    String city = txt_input.getText().toString();
                    String key = "your openweathermap api key";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru&lat&lon";

                    new GetResulUrl().execute(url);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetResulUrl extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();

            bar.setVisibility(View.VISIBLE);
            result_txt.setText("");
            result_two_txt.setText("");
            result_three_txt.setText("");
            result_four_txt.setText("");
            result_five_txt.setText("");
            result_six_txt.setText("");
            result_seven_txt.setText("");
            result_eight_txt.setText("");
            textViewlongitude.setText("");
            textViewlatitude.setText("");
            result_nine_txt.setText("");

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject object = new JSONObject(result);
                result_txt.setText("Temp: " + object.getJSONObject("main").getDouble("temp"));

                JSONObject object_two = new JSONObject(result);
                result_two_txt.setText("Feels like: " + object_two.getJSONObject("main").getDouble("feels_like"));

                JSONObject  object_three = new JSONObject(result);
                result_three_txt.setText("Humidity:" + object_three.getJSONObject("main").getDouble("humidity"));

                JSONObject object_four = new JSONObject(result);
                result_four_txt.setText("Clouds(%):" + object_four.getJSONObject("clouds").getDouble("all"));

                JSONObject object_five = new JSONObject(result);
                result_five_txt.setText("Wind speed:" + object_five.getJSONObject("wind").getDouble("speed"));

                JSONObject object_six = new JSONObject(result);
                result_six_txt.setText("Max temp:" + object_six.getJSONObject("main").getDouble("temp_max"));

                JSONObject object_seven = new JSONObject(result);
                result_seven_txt.setText("Min temp:" + object_seven.getJSONObject("main").getDouble("temp_min"));

                JSONObject object_eight = new JSONObject(result);
                textViewlongitude.setText("Lon:" + object_eight.getJSONObject("coord").getDouble("lon"));

                JSONObject object_nine = new JSONObject(result);
                textViewlatitude.setText("Lat:" + object_nine.getJSONObject("coord").getDouble("lat"));

                JSONObject object_ten = new JSONObject(result);
                result_nine_txt.setText("Country:" + object_ten.getJSONObject("sys").getString("country"));

                JSONObject object_eleven = new JSONObject(result);
                result_eight_txt.setText("Wind gust:" + object_eleven.getJSONObject("wind").getDouble("gust"));

                bar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
                bar.setVisibility(View.INVISIBLE);
                error.setText("Error!");
            }

        }}}
