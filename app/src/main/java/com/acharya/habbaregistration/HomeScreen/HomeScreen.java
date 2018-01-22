package com.acharya.habbaregistration.HomeScreen;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.acharya.habbaregistration.Apl.Apl;
import com.acharya.habbaregistration.Apl.HttpHandler;
import com.acharya.habbaregistration.Habba;
import com.acharya.habbaregistration.Login.Login;
import com.acharya.habbaregistration.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import it.beppi.tristatetogglebutton_library.TriStateToggleButton;

import static android.graphics.Color.argb;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    String email,name;
    private static long back_pressed;
    private Button signout;
    private GoogleApiClient mGoogleApiClient;
    private String result1 = null;
    final String checkurl = "http://www.acharyahabba.in/apl/checks.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        new GetContacts().execute();
        signout = findViewById(R.id.out);
        signout.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)   //gets identity
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
                        if(result1.contains("0"))
                            Toast.makeText(getApplicationContext(),"APL registrations are not open",Toast.LENGTH_LONG).show();
                        else {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(HomeScreen.this, Apl.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                }
                            }, 300);
                        }
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
    @Override

    public void onBackPressed()

    {

        if(back_pressed +2000 >System.currentTimeMillis()) {
           super.onBackPressed();
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Press back again to exit ",Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Intent i = new Intent(HomeScreen.this,Login.class);
                Toast.makeText(getApplicationContext(),"You have been signed out",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out:
                signOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private class GetContacts extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg) {

            HttpHandler sh = new HttpHandler();
            String allowed = sh.makeServiceCall(checkurl);
            return allowed;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            result1=result;
            System.out.println(result);
        }


    }
}

