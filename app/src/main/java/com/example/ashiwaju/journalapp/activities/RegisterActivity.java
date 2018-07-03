package com.example.ashiwaju.journalapp.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.ashiwaju.journalapp.R;
import com.example.ashiwaju.journalapp.helper.InputValidation;
import com.example.ashiwaju.journalapp.model.User;
import com.example.ashiwaju.journalapp.sql.DatabaseHelper;

/**
 * Created by ashiwaju on 6/30/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton registerButton;

    private AppCompatTextView textViewLoginLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }
    public void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollViewRegister);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.userName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.userEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.userPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.confirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.editUserName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.editUserEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.editUserPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.editConfirmPassword);

        registerButton = (AppCompatButton) findViewById(R.id.registerButton);

        textViewLoginLink = (AppCompatTextView) findViewById(R.id.backToLogin);
    }

    private void initListeners(){
        registerButton.setOnClickListener(this);
        textViewLoginLink.setOnClickListener(this);
    }

    private void initObjects(){
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.registerButton:
                postDataToSQLite();
                break;
            case R.id.backToLogin:
                finish();
                break;
        }
    }

    private void postDataToSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.name_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.email_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.password_not_match))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.email__exist_error_message), Snackbar.LENGTH_LONG).show();
        }


    }

    private void emptyInputEditText(){
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

}
