package com.acharya.habbaregistration.HomeScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.acharya.habbaregistration.Apl.Apl;
import com.acharya.habbaregistration.Habba;
import com.acharya.habbaregistration.Login.Login;
import com.acharya.habbaregistration.R;

import java.net.URL;

import it.beppi.tristatetogglebutton_library.TriStateToggleButton;

import static android.graphics.Color.argb;


public class HomeScreen extends AppCompatActivity {

    String email,name,personPhoto;
    int mWidth,mHeight;
    Button bhabba,bapl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final TriStateToggleButton tstb_1 = (TriStateToggleButton) findViewById(R.id.buttonPanel);
        tstb_1.setMidColor(argb(1,106,132,219));
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //getSupportActionBar().hide();
        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            name = bundle.getString("name");
          }

        tstb_1.setOnToggleChanged(new TriStateToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus, boolean booleanToggleStatus, int toggleIntValue) {
                switch (toggleStatus) {
                    case off: tstb_1.setOffColor(argb(1,106,132,219));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(HomeScreen.this, Apl.class);
                                intent.putExtra("email",email);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        }, 300);
                        break;
                    case mid:break;
                    case on: tstb_1.setOnColor(argb(1,106,132,219));
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent2 = new Intent(HomeScreen.this, Habba.class);
                                intent2.putExtra("email",email);
                                intent2.putExtra("name",name);
                                startActivity(intent2);
                            }
                        }, 300);

                        break;
                }

            }
        });


     /*
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

*/

    }

}

