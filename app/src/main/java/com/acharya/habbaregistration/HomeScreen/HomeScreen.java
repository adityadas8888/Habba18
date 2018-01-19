package com.acharya.habbaregistration.HomeScreen;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.acharya.habbaregistration.Apl.Apl;
import com.acharya.habbaregistration.Habba;
import com.acharya.habbaregistration.R;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;

import static android.graphics.Color.argb;


public class HomeScreen extends AppCompatActivity {
    String email,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final TriStateToggleButton tstb_1 = (TriStateToggleButton) findViewById(R.id.buttonPanel);
        tstb_1.setMidColor(argb(1,140,164,245));
        tstb_1.setOnColor(argb(1,140,164,245));
        tstb_1.setOffColor(argb(1,140,164,245));
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
                    case off:
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

                    case on:
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

    }

}

