package fr.example.mape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Information extends AppCompatActivity {

    String latitude;
    String longitude;
    String title;
    String snippet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent  = getIntent();

        latitude = ((Double)intent.getExtras().getDouble("latitude")).toString();
        longitude = ((Double)intent.getExtras().getDouble("longitude")).toString();
        title =intent.getExtras().getString("title");
        snippet=intent.getExtras().getString("snippet");

        ((TextView)findViewById(R.id.titleInfo)).setText(title);
        ((TextView)findViewById(R.id.snippetInfo)).setText(snippet);
        ((TextView)findViewById(R.id.latitudeInfo)).setText(latitude);
        ((TextView)findViewById(R.id.longitudeInfo)).setText(longitude);
    }

    public boolean finish(View view){
        finish();
        return true;
    }


}
