package com.example.ls08_numbersapp;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class YearFactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_fact);

        String fact = (String) getIntent().getExtras().get("yearfact");
        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText(fact);

        setSupportActionBar(findViewById(R.id.toolbar2));
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
