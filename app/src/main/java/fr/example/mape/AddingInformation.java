package fr.example.mape;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class AddingInformation extends AppCompatActivity {

    Bundle extras;
    Bitmap picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

    }

    public boolean validate(View view){

        Intent information = new Intent(getApplicationContext(), MapsActivity.class);

        Bundle bundle =  new Bundle();
        bundle.putParcelable("picture", picture);

        String information1_edit = ((EditText)findViewById(R.id.information1_edit)).getText().toString();
        String information2_edit =((EditText)findViewById(R.id.information2_edit)).getText().toString();


        information.putExtra("bundle", bundle);
        information.putExtra("information1_edit",information1_edit);
        information.putExtra("information2_edit",information2_edit);

        setResult(Activity.RESULT_OK,information);

        finish();
        return true;
    }

    public void addPicture(View view){
        Intent addPicture =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (addPicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(addPicture, 10);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        picture = (Bitmap)extras.get("data");
        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(picture);

    }


}
