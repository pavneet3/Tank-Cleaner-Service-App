package com.example.pavneet.tankcleaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MidActivity extends AppCompatActivity {

    Button request,price;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mid);
        request=(Button)findViewById(R.id.Request);
        price=(Button)findViewById(R.id.priceList);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            intent=new Intent(MidActivity.this,Form.class);
                startActivity(intent);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        intent=new Intent(MidActivity.this,Price.class);
                startActivity(intent);
            }
        });


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
}
