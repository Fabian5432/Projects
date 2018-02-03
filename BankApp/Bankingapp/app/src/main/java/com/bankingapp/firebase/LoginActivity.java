package com.bankingapp.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText textEmail;
    private EditText textPass;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button buttonRegister;
    private Button buttonReset;
    private CheckBox buttonCheckBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    private String UnameValue;
    private String PasswordValue;
    private final String DefaultUnameValue = "";
    private final String DefaultPasswordValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        textEmail = (EditText) findViewById(R.id.etxt_email);
        textPass = (EditText) findViewById(R.id.etxt_password);
        buttonLogin= (Button) findViewById(R.id.btn_login);
        buttonRegister=(Button) findViewById((R.id.register));
        buttonReset=(Button) findViewById(R.id.reset_password);
        buttonCheckBox=(CheckBox)findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        buttonCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(LoginActivity.this,ResetPasswordActivity.class)));
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Toast.makeText(LoginActivity.this, "Now you are logged In ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, ActivityAccount.class);
                    startActivity(intent);
                    finish();
                    //mAuth.signOut();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

        private void doLogin() {
        String email = textEmail.getText().toString().trim();
        String password = textPass.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressDialog.setMessage("Loging , please wait");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login succesful", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void savePreferences()
    {
        SharedPreferences settings=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        //edit and commit
        UnameValue=textEmail.getText().toString();
        PasswordValue=textPass.getText().toString();
        editor.putString(KEY_USERNAME, UnameValue);
        editor.putString(KEY_USERNAME, PasswordValue);
        editor.commit();
    }
    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(KEY_USERNAME, DefaultUnameValue);
        PasswordValue = settings.getString(KEY_PASS, DefaultPasswordValue);
        textEmail.setText(UnameValue);
        textEmail.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        savePreferences();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        loadPreferences();
    }
}
