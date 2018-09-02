package com.example.slimshady.googlemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {


   private TextView title,gmail,version,tel;
   private ImageView launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        title=(TextView)findViewById(R.id.about_title);
        gmail=(TextView)findViewById(R.id.about_gmail);
        version=(TextView)findViewById(R.id.about_version);
        tel=(TextView)findViewById(R.id.about_tel);

        launcher=(ImageView)findViewById(R.id.about_image);

    }
}
