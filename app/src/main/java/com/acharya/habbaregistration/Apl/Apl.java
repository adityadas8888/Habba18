package com.acharya.habbaregistration.Apl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acharya.habbaregistration.HomeScreen.HomeScreen;
import com.acharya.habbaregistration.R;
import com.acharya.habbaregistration.UploadImage.UImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_LONG;

public class Apl extends AppCompatActivity {

    EditText editTextUsn, editTextDob, editTextPhone, editTextName,editTextEmail;
    TextView t1,t2;
    Spinner s1, s2;
    Button buttonRegister;
    Map<String,String> map=new HashMap<String,String>();
    String idzz2;
    RadioGroup radioGroupGender,radioGroupDesignation,radioGroupCategory;
    RadioButton radioButtonGender,radioButtonDesignation,radioButtonCategory;
    public String email,name,clg,number,gender,designation,usn,category,dept,dob;
    TextInputLayout textinputusn;
    private static String url = null;
    private static String url1 = null;
    boolean flagz1=false;
    View rootLayout;
    private int revealX;
    private int revealY;
    int mWidth,mHeight;

    private static final String REGISTER_URL = "http://www.acharyahabba.in/apl/register.php";
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apl);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().hide();

        //default values for clg, dept and usn
        clg="N/A";
        dept="N/A";
        usn="N/A";
        final Intent intent = getIntent();
        mWidth= this.getResources().getDisplayMetrics().widthPixels;
        mHeight= this.getResources().getDisplayMetrics().heightPixels;

        rootLayout = findViewById(R.id.root_layout);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

        t1 = (TextView)findViewById(R.id.textView2);
        t2 = (TextView)findViewById(R.id.textView3);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editPhone);
        editTextUsn = (EditText) findViewById(R.id.editTextUsn);
        textinputusn = (TextInputLayout) findViewById(R.id.text_input_usn);
        s1 = (Spinner) findViewById(R.id.spinner);                          //College
        s2 = (Spinner) findViewById(R.id.spinner2);                         //Branch
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextDob = (EditText)findViewById(R.id.editTextDob);
        radioGroupGender = (RadioGroup)findViewById(R.id.gender);
        radioGroupDesignation = (RadioGroup)findViewById(R.id.designation);
        radioGroupCategory = (RadioGroup)findViewById(R.id.category);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
         Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            name = bundle.getString("name");
            if(name.equals(""))
                name="N/A";
        }
        editTextEmail.setText(email);
        editTextEmail.setEnabled(false);
        url = "http://acharyahabba.in/apl/college.php";
        spinnerlist = new ArrayList<>();
        new GetContacts().execute();
        initeditfileds();
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(600);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, mWidth/2, mHeight/2, finalRadius, 0);

            circularReveal.setDuration(600);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }

    public void initeditfileds() {
        editTextName.setText(name);
        editTextName.setEnabled(false);
        s1.setVisibility(View.GONE);
        s2.setVisibility(View.GONE);
        t1.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);
        textinputusn.setVisibility(View.GONE);
        editTextUsn.setVisibility(View.GONE);

        editTextDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Apl.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
          DateDialog dialog=new DateDialog(v);
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });


        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroupGender.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonGender = (RadioButton) findViewById(selectedId);
                gender = radioButtonGender.getText().toString();
                if(gender.equals(""))
                    gender="N/A";
            }

        });

        radioGroupDesignation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroupDesignation.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButtonDesignation = (RadioButton) findViewById(selectedId);
                designation = radioButtonDesignation.getText().toString();
                if(designation.equals("Student")){
                    s1.setVisibility(View.VISIBLE);
                    s2.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                    editTextUsn.setVisibility(View.VISIBLE);
                    textinputusn.setVisibility(View.VISIBLE);
                    flagz1=true;
                }
              else {
                    editTextUsn.setVisibility(View.GONE);
                    textinputusn.setVisibility(View.GONE);
                    s1.setVisibility(View.GONE);
                    s2.setVisibility(View.GONE);
                    t1.setVisibility(View.GONE);
                    t2.setVisibility(View.GONE);
                    flagz1=false;

                }
                if(designation.equals("Faculty")||designation.equals("Others")){
                    editTextUsn.setVisibility(View.GONE);
                    textinputusn.setVisibility(View.GONE);
                    s1.setVisibility(View.GONE);
                    s2.setVisibility(View.GONE);
                    t1.setVisibility(View.GONE);
                    t2.setVisibility(View.GONE);
                    flagz1=false;
                }
                if(designation.isEmpty()) {
                    designation = "N/A";
                    flagz1=false;
                }

            }


        });

        radioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroupCategory.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButtonCategory = (RadioButton) findViewById(selectedId);
                category = radioButtonCategory.getText().toString();
            }

        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
    }


    private String TAG = Apl.class.getSimpleName();

    // URL to get contacts JSON

    public static ArrayList<String> spinnerlist;


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("result");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String name = c.getString("name");
                        String idzz1=c.getString("id");

                        map.put(name,idzz1);
                        spinnerlist.add(name);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(Apl.this,android.R.layout.simple_spinner_dropdown_item, spinnerlist);
                    s1.setAdapter(spinnerAdapter);

                    s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            clg = s1.getSelectedItem().toString();
                            idzz2=map.get(clg);
                            url1 = "http://acharyahabba.in/apl/dept.php?cid=" + idzz2;
                            System.out.println("spinner"+url1);
                            subspinnerlist = new ArrayList<>();
                            new GetCategory().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            s2.setSelection(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            });
        }


    }

    public static ArrayList<String> subspinnerlist;


    private class GetCategory extends AsyncTask<Void, Void, Void> {
        String amount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url1);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("result");
                    for(int i = 0; i<contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String name = c.getString("dept_name");
                        //amount = c.getString("amount");

                        subspinnerlist.add(name);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> subspinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item , subspinnerlist);
                    s2.setAdapter(subspinnerAdapter);
                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            dept = s2.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            });
        }


    }


    private void registerUser() {

        usn = editTextUsn.getText().toString().trim().toLowerCase();
        if( editTextUsn.getText().toString().trim().equals("")) {
            editTextUsn.setError( "USN is required!" );
            if(usn.isEmpty())
                usn="N/A";
        }

        dob = editTextDob.getText().toString();

        number = editTextPhone.getText().toString().trim().toLowerCase();
        if( editTextPhone.getText().toString().trim().equals("")) {
            editTextPhone.setError( "Phone number is required!" );
            if(number.isEmpty())
                number="N/A";
        }
        if( editTextDob.getText().toString().trim().equals("")) {
            editTextDob.setError( "Phone number is required!" );
            if(dob.isEmpty())
                dob="08/08/1994";
        }
            clg="N/A";
            dept="N/A";
            //usn="N/A";
            if(flagz1==true) {
                clg = s1.getSelectedItem().toString();
                dept = s2.getSelectedItem().toString();//null pointer exception
            }
        register(name,gender,dob,designation,category,email,number,clg,dept,usn);
        System.out.println("final" + name + gender + dob + designation + category + email + number + clg + dept + usn);

    }


    private void register(String name, String gender, String dob, String designation, String category, String email, String number, String clg, String dept, String usn) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Apl.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if(s.contentEquals("Registration Succefull!"))
                {
                Intent intent = new Intent(Apl.this,UImageActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                startActivity(intent);
                finish();}

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("gender",params[1]);
                data.put("dob",params[2]);
                data.put("desig",params[3]);
                data.put("cat",params[4]);
                data.put("email",params[5]);
                data.put("num",params[6]);
                data.put("clg",params[7]);
                data.put("dept",params[8]);
                data.put("usn",params[9]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,gender,dob,designation,category,email,number,clg,dept,usn);

    }
}