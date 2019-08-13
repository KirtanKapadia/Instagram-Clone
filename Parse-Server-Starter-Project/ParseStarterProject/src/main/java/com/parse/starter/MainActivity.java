/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpModeActive = true;
  TextView changeSignUpTextView;
  EditText password;

  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(), userListActivity.class);
    startActivity(intent);
  }


  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
      if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            signUp(view);
      }
        return false;
  }


  @Override
  public void  onClick(View view){
    if(view.getId() == R.id.changeSignUpMode){
      Button signUp = (Button)findViewById(R.id.signUpButton);
      if(signUpModeActive){
          signUpModeActive = false;
          signUp.setText("LogIn");
          changeSignUpTextView.setText("or, SignUp");
      }else {
        signUpModeActive = true;
        signUp.setText("Sign Up");
        changeSignUpTextView.setText("or, LogIn");
      }
    }else if(view.getId() == R.id.constraintLayout || view.getId() == R.id.Logo){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  public void signUp(View view){
    EditText username = (EditText)findViewById(R.id.username);


    if(username.getText().toString().equals("") || password.getText().toString().equals("")){
      Toast.makeText(this,"Username and Password is required", Toast.LENGTH_SHORT).show();
    }else{
          if(signUpModeActive) {
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(username.getText().toString());
            parseUser.setPassword(password.getText().toString());

            parseUser.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                if (e == null) {
                  Log.i("Sign Up", "Successful");
                  showUserList();
                } else {
                  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              }
            });
          } else {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser parseUser, ParseException e) {
                if(parseUser != null){
                  Log.i("Log In", "Successful");
                  showUserList();
                }else {
                  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              }
            });
          }


    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    changeSignUpTextView = (TextView)findViewById(R.id.changeSignUpMode);
    changeSignUpTextView.setOnClickListener(this);

    ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

    ImageView logo = (ImageView)findViewById(R.id.Logo);

    constraintLayout.setOnClickListener(this);
    logo.setOnClickListener(this);

    password = (EditText)findViewById(R.id.password);
    password.setOnKeyListener(this);

    if(ParseUser.getCurrentUser() != null){
      showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}