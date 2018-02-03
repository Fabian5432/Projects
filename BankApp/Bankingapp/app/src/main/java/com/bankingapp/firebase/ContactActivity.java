package com.bankingapp.firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {
    private Button btnCall;
    private TextView phone;
    private Button mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        btnCall = (Button) findViewById(R.id.btn_phone);
        phone = (TextView) findViewById(R.id.textView3);
        mail = (Button) findViewById(R.id.mail);
    Thread t=new Thread()
    {
        public void run()
        {

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialer();
            }
        });

    }

    };
    t.start();
    mail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OpenMail();
        }
    });
    }
    private void OpenDialer()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone.getText().toString()));
        startActivity(intent);
    }
    private void OpenMail()
    {
        Intent intent=new Intent(Intent.ACTION_SEND);
        String[] recipients={"bankingappv1@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.setType("text/html");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }
}
