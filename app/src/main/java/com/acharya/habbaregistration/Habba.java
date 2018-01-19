package com.acharya.habbaregistration;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.acharya.habbaregistration.Apl.HttpHandler;
import com.acharya.habbaregistration.Apl.RegisterUserClass;
import com.acharya.habbaregistration.HomeScreen.HomeScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.widget.Toast.LENGTH_LONG;

public class Habba extends AppCompatActivity{

    private EditText editTextName,editTextEmail;
    private EditText editTextPhone;
    private EditText editTextDescribe;
    private EditText editTextUsn;
    private EditText editTextSuggestion;
    private EditText editTextSkills;
    private static String url = null;
    private static String url1 = null;
    private CheckBox c1,c2,c3,c4,c5,c6,c7;
    private static String interest = " ";
    Map<String,String> map=new HashMap<String,String>();
    String idzz2;
    String email,name,clg,dept,year,exp,skills;
    Spinner s1, s2;
                                                                                                     // Spinner yearspin;
    RadioGroup rgyear,rgexp;
    RadioButton rbyear,rbexp;
    private Button buttonRegister;
    private static final String REGISTER_URL = "http://www.acharyahabba.in/habba18/vol_app.php";
    private String nullstring = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habba);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            name = bundle.getString("name");
            if(name.equals(nullstring)||name.equals(" "))
                name="N/A";
        }
        url = "http://acharyahabba.in/apl/college.php";
        spinnerlist = new ArrayList<>();
        new Habba.GetContacts().execute();

        initfilelds();
        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void initfilelds() {
        editTextName = (EditText)findViewById(R.id.Name);
        editTextPhone = (TextInputEditText)findViewById(R.id.Phone);
        editTextDescribe = (TextInputEditText)findViewById(R.id.Describe);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextUsn = (TextInputEditText)findViewById(R.id.Usn);
        editTextSuggestion = (TextInputEditText)findViewById(R.id.Suggestion);
        editTextSkills = (TextInputEditText)findViewById(R.id.Skills);
        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        rgyear = (RadioGroup)findViewById(R.id.rgyear);
        rgexp = (RadioGroup)findViewById(R.id.rgexp);
        buttonRegister = (Button) findViewById(R.id.ButtonRegister);
        editTextName.setText(name, TextView.BufferType.EDITABLE);
        editTextName.setEnabled(false);
        editTextEmail.setText(email,TextView.BufferType.EDITABLE);
        editTextEmail.setEnabled(false);
        rbyear = (RadioButton) findViewById(rgyear.getCheckedRadioButtonId());
        year = rbyear.getText().toString();
        rbexp = (RadioButton) findViewById(rgexp.getCheckedRadioButtonId());
        exp = rbexp.getText().toString();                       //  yearspin = findViewById(R.id.yearspinner);
        c1 = (CheckBox)findViewById(R.id.one);
        c2 = (CheckBox)findViewById(R.id.two);
        c3 = (CheckBox)findViewById(R.id.three);
        c4 = (CheckBox)findViewById(R.id.four);
        c5 = (CheckBox)findViewById(R.id.five);
        c6 = (CheckBox)findViewById(R.id.six);
        c7 = (CheckBox)findViewById(R.id.seven);
         rgyear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = rgyear.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbyear = (RadioButton) findViewById(selectedId);
                year = rbyear.getText().toString();
            }

        });


        rgexp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = rgexp.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbexp = (RadioButton) findViewById(selectedId);
                exp = rbexp.getText().toString();
            }

        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private String TAG = Habba.class.getSimpleName();
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

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(Habba.this,android.R.layout.simple_spinner_dropdown_item, spinnerlist);
                    s1.setAdapter(spinnerAdapter);

                    s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String hahaah="";
//                            long z2=id+1;
//                            hahaah=hahaah+z2;
//                            Toast.makeText(getApplicationContext(),hahaah,Toast.LENGTH_SHORT).show();
                            clg = s1.getSelectedItem().toString();
                            idzz2=map.get(clg);

                            url1 = "http://acharyahabba.in/apl/dept.php?cid=" + idzz2;
                            System.out.println("spinner"+url1);
                            subspinnerlist = new ArrayList<>();
                            new Habba.GetCategory().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            s2.setSelection(position);
                            //((TextView) parent.getChildAt(0)).setTextColor(0x00000000);
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
                            R.layout.spinner_item, subspinnerlist);
                    subspinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
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

        if( editTextName.getText().toString().trim().equals(""))
            editTextName.setError( "Name is required!" );

        if( editTextPhone.getText().toString().trim().equals(""))
            editTextPhone.setError( "Phone number is required!" );

        if( editTextDescribe.getText().toString().trim().equals("")) {
            editTextDescribe.setError("Description is required!");
            }
        if( editTextSuggestion.getText().toString().trim().equals(""))
            editTextSuggestion.setError( "Suggestion is required!" );
        if( editTextUsn.getText().toString().trim().equals(""))
            editTextUsn.setError( "Usn is required!" );
        interest="";
        if(c1.isChecked()&&(!interest.contains(c1.getText().toString()))) interest = interest + c1.getText().toString() + ",";
        if(c2.isChecked()&&(!interest.contains(c2.getText().toString()))) interest = interest + c2.getText().toString() + ",";
        if(c3.isChecked()&&(!interest.contains(c3.getText().toString()))) interest = interest + c3.getText().toString() + ",";
        if(c4.isChecked()&&(!interest.contains(c4.getText().toString()))) interest = interest + c4.getText().toString() + ",";
        if(c5.isChecked()&&(!interest.contains(c5.getText().toString()))) interest = interest + c5.getText().toString() + ",";
        if(c6.isChecked()&&(!interest.contains(c6.getText().toString()))) interest = interest + c6.getText().toString() + ",";
        if(c7.isChecked()&&(!interest.contains(c7.getText().toString()))) interest = interest + c7.getText().toString() + ",";

        Log.e(TAG,interest);
        String usn = editTextUsn.getText().toString().trim().toLowerCase();
        String describe = editTextDescribe.getText().toString().trim().toLowerCase();
        String phone = editTextPhone.getText().toString().trim();
        String suggestion = editTextSuggestion.getText().toString().trim().toLowerCase();
        Log.e(TAG,year+exp);
        skills=editTextSkills.getText().toString().trim().toLowerCase();
        if(usn.isEmpty()||describe.isEmpty()||phone.isEmpty()||year.isEmpty()||exp.isEmpty()||skills.isEmpty()||suggestion.isEmpty())
        { Toast.makeText(getApplicationContext(),"Enter Required Values",Toast.LENGTH_SHORT).show();}
        else
        register(name,email,usn,clg,dept,year,exp,describe,phone,suggestion,interest,skills);
    }

    private void register(String name, String email, String usn, String clg, String dept, String year, String exp, String describe, String phone, String suggestion, String interest, String skills) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Habba.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("email",params[1]);
                data.put("usn",params[2]);
                data.put("clg",params[3]);
                data.put("dept",params[4]);
                data.put("year",params[5]);
                data.put("exp",params[6]);
                data.put("aboutme",params[7]);
                data.put("num",params[8]);
                data.put("suggest",params[9]);
                data.put("interest",params[10]);
                data.put("skill",params[11]);
                Log.e(TAG,data.get("interest"));
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,email,usn,clg,dept,year,exp,describe,phone,suggestion,interest,skills);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
        finish();


    }
}
