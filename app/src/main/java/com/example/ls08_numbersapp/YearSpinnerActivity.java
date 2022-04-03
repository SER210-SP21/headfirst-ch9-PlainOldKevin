package com.example.ls08_numbersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YearSpinnerActivity extends AppCompatActivity {

    MainActivity yrHandler = new MainActivity();

    boolean userSelect = false;
    private String url1 = "https://numbersapi.p.rapidapi.com/";
    private String url2= "/year?fragment=true&json=true";
    private String LOG_TAG = YearSpinnerActivity.class.getSimpleName();
    private ShareActionProvider provider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_spinner);

        Spinner spinner = (Spinner)findViewById(R.id.spinner_id);


        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,yrHandler.years);

        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(yearsAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userSelect) {
                    final String year = (String) parent.getItemAtPosition(position);
                    Log.i("onItemSelected :year", year);

                    //TODO : call of async subclass goes here
                    new FetchYearFact().execute(year);
                    userSelect = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSupportActionBar(findViewById(R.id.toolbar2));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.location:
                Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
                break;
            case R.id.refresh:
                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you");
                provider.setShareIntent(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userSelect = true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider((MenuItem) menu.findItem(R.id.share));
        return super.onCreateOptionsMenu(menu);
    }

    class FetchYearFact extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String yearFact = null;
            try {
                URL url = new URL(url1 + strings[0] + url2);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","UygwA3LnI1mshAPcqbrTdu6rvUkxp1Kd1q6jsnETjeLq2t3LzS");
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                if (in == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(in));
                yearFact = getStringFromBuffer(reader);

            }catch (Exception e) {
                Log.e(LOG_TAG,"Error" + e.getMessage());
                return null;
            }finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG,"Error" + e.getMessage());
                        return null;
                    }
                }
            }
            return yearFact;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d(LOG_TAG, "Result is null");
                Intent intent = new Intent(YearSpinnerActivity.this, YearFactActivity.class);
                intent.putExtra("yearfact", result);
                startActivity(intent);
            }
        }
    }

    private String getStringFromBuffer(BufferedReader bufferedReader) {
        StringBuffer buffer = new StringBuffer();
        String line;

        if (bufferedReader != null) {
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + '\n');
                }
                bufferedReader.close();
                return yrHandler.getYearFact(buffer.toString());
            } catch (Exception e) {
                Log.e("MainActivity", "Error" + e.getMessage());
                return null;
            } finally {

            }
        }
        return null;
    }
}
