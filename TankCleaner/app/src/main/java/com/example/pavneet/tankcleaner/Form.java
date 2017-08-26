package com.example.pavneet.tankcleaner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Form extends AppCompatActivity implements LocationListener {

    EditText name,email,phone,etaddress;
    String str_name,str_phone,str_email,str_address;

    LocationManager locationManager;
    String provider;
    double lat, lng;
    final int MY_PERMISSION_REQUEST_CODE = 7171;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        name=(EditText)findViewById(R.id.etname);
        email=(EditText)findViewById(R.id.etemail);
        phone=(EditText)findViewById(R.id.etphone);
        etaddress=(EditText)findViewById(R.id.etaddress);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            getlocation();
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myloc = locationManager.getLastKnownLocation(provider);
        lat = myloc.getLatitude();
        lng = myloc.getLongitude();
        new GetAddress().execute(String.format("%.4f,%.4f", lat, lng));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Aboutus:

                Intent intent=new Intent(this,AboutUs.class);
                startActivity(intent);
                break;
            case R.id.help:
                Intent intent1=new Intent(this,Help.class);
                startActivity(intent1);

                break;
        }
        return false;
    }

    public void initialize(){
        str_name=name.getText().toString();
        str_phone=phone.getText().toString();
        str_email=email.getText().toString();
        str_address=etaddress.getText().toString();
    }

    public boolean validation(){
        boolean valid=true;
        if(str_name.isEmpty()|| str_name.length()<2 || str_name.length()>32){
            name.setError("please enter valid name");
            valid=false;
        }

        if(str_email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            email.setError("please enter valid email address");
            valid=false;
        }
        if(str_phone.isEmpty()|| !Patterns.PHONE.matcher(str_phone).matches()){
            phone.setError("please enter valid contact no.");
            valid=false;
        }
        if(str_address.isEmpty()){
            etaddress.setError("please enter address");
            valid=false;
        }
        return valid;

    }



    public void onreg(View view){

        initialize();
        if(validation()){
            String type="register";
            BcWorker bcWorker=new BcWorker(this);
            bcWorker.execute(type,str_name,str_email,str_phone,str_address);


            name.setText("");
            phone.setText("");
            email.setText("");
        }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getlocation();
                }
                break;
        }
    }

    private void getlocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        final Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Log.e("ERROR", "Location is null");
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lng=location.getLongitude();
        new GetAddress().execute(String.format("%.4f,%.4f",lat,lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
    private  class GetAddress extends AsyncTask<String,Void,String> {

        ProgressDialog dialog= new ProgressDialog(Form.this);
        @Override
        protected String doInBackground(String... strings) {
            try{
                double lat =Double.parseDouble(strings[0].split(",") [0]);
                double lng =Double.parseDouble(strings[0].split(",") [1]);
                String response;

                HttpDataHandler http =new HttpDataHandler();
                String url=String.format("http://maps.googleapis.com/maps/api/geocode/json?latlng=%.4f,%.4f&sensor=false",lat,lng);
                response=http.GetHttpData(url);
                return response;
            }catch (Exception e){
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject=new JSONObject(s);
                String address=((JSONArray)jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();
                etaddress.setText(address);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
