package com.example.crudgraduation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.crudgraduation.R;

public class AddMerchantActivity extends AppCompatActivity {

    EditText nameMerchant, descriptionMerchant;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchant);

        nameMerchant =findViewById(R.id.merchantName);
        descriptionMerchant = findViewById(R.id.merchantDesc);
        sendEmail = findViewById(R.id.send_request);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String merchantName = nameMerchant.getText().toString();
                String merchantDescription = descriptionMerchant.getText().toString();
                //not send the email to klementinagorgieva7@hotmail.com

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setPackage("com.google.android.gm");
                String to = "klementinagorgieva7@hotmail.com";
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, merchantName);
                email.putExtra(Intent.EXTRA_TEXT, merchantDescription);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

                //after that
                nameMerchant.setText(null);
                descriptionMerchant.setText(null);
            }

        });
        setUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    this.finish();
                    hideUpButton();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }

    private void setUp() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}