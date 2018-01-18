package com.acharya.habbaregistration.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.acharya.habbaregistration.Apl.Apl;
import com.acharya.habbaregistration.Habba;
import com.acharya.habbaregistration.R;

import java.net.URL;


public class HomeScreen extends AppCompatActivity {

    String email,name,personPhoto;
    int mWidth,mHeight;
    Button bhabba,bapl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            name = bundle.getString("name");
          }

        getSupportActionBar().hide();

        bhabba = (Button)findViewById(R.id.habba);
        bapl = (Button)findViewById(R.id.apl);
        bhabba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Habba.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("photo",personPhoto);
                startActivity(intent);
            }
        });

        bapl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Apl.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });




        mWidth= this.getResources().getDisplayMetrics().widthPixels;
        mHeight= this.getResources().getDisplayMetrics().heightPixels;




    }

}

