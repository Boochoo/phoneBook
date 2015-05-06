package com.example.joojoo.phonebook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by joojoo on 05/05/15.
 */

//this is where the UI functionality magic happens
public class AddActivity extends FragmentActivity {
    private final String LOG_TAG = AddActivity.class.getSimpleName();
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private Button mButton;
    private ContentResolver mContentResolver;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        // if the home button is selected; one can go to the top level of the app
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mNameTextView = (TextView) findViewById(R.id.friend_name);
        mEmailTextView = (TextView) findViewById(R.id.friend_email);
        mPhoneTextView = (TextView) findViewById(R.id.friend_phone);

        mContentResolver = AddActivity.this.getContentResolver();

        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){

                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.FRIENDS_NAME, mNameTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL, mEmailTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE, mPhoneTextView.getText().toString());

                    Uri returned = mContentResolver.insert(FriendsContract.URI_TABLE , values);
                    Log.d(LOG_TAG, "record id returned is" + returned.toString());
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else{

                    Toast.makeText(getApplicationContext(), "please make sure if you added a valid data or not",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public boolean isValid(){
        if(mNameTextView.getText().toString().length() == 0 ||
                mEmailTextView.getText().toString().length() == 0 ||
                mPhoneTextView.getText().toString().length() == 0){

            return false;
        }else{
            return true;
        }



    }

    public boolean someDataEntered(){
        if(mNameTextView.getText().toString().length() > 0 ||
                mEmailTextView.getText().toString().length() > 0 ||
                mPhoneTextView.getText().toString().length()  > 0){

            return true;
        }else{
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        if(someDataEntered()){
            PhoneBookDialog dialog = new PhoneBookDialog();
            Bundle args = new Bundle();
            args.putString(PhoneBookDialog.DIALOG_TYPE, PhoneBookDialog.CONFIRM_EXIT);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "confirm-exit");

        }else{
            super.onBackPressed();
        }
    }
}
