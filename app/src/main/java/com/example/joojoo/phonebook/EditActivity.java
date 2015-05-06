package com.example.joojoo.phonebook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by joojoo on 05/05/15.
 */
public class EditActivity extends FragmentActivity {
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

        mContentResolver = EditActivity.this.getContentResolver();

        Intent intent = getIntent();
        final String _id = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_ID);
        final String name = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_NAME);
        final String phone = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_PHONE);
        final String email = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_EMAIL);

        mNameTextView.setText(name);
        mPhoneTextView.setText(phone);
        mEmailTextView.setText(email);

        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.FRIENDS_NAME, mNameTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL, mEmailTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE, mPhoneTextView.getText().toString());

                    Uri uri = FriendsContract.Friends.buildFriendUri(_id);
                    int recordUpdated = mContentResolver.update(uri, values, null, null);
                    Log.d(LOG_TAG, "number of record updated" + recordUpdated);
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


            }
        });

    }

    //if a user pressed the back button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}
