package com.example.joojoo.phonebook;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by joojoo on 05/05/15.
 */
public class SearchActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<List<PhoneBook>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private PhoneBookCustomAdapter mPhoneBookCustomAdapter;
    private static int LOADER_ID = 2;
    private ContentResolver mContentResolver;
    private List<PhoneBook> phoneBooksRetrieved;
    private ListView listView;
    private EditText mSearchEditText;
    private Button mSearchFriendButton;
    private String matchText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        listView = (ListView) findViewById(R.id.searchResultsList);
        mSearchEditText = (EditText) findViewById(R.id.searchName);
        mSearchFriendButton = (Button) findViewById(R.id.searchButton);
        mContentResolver = getContentResolver();
        mPhoneBookCustomAdapter = new PhoneBookCustomAdapter(SearchActivity.this, getSupportFragmentManager());
        listView.setAdapter(mPhoneBookCustomAdapter);

        mSearchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchText = mSearchEditText.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
            }
        });
    }

    @Override
    public Loader<List<PhoneBook>> onCreateLoader(int i, Bundle bundle) {
        return new PhoneBookSearchListLoader(SearchActivity.this,
                FriendsContract.URI_TABLE, this.mContentResolver, matchText);
    }

    @Override
    public void onLoadFinished(Loader<List<PhoneBook>> listLoader, List<PhoneBook> phoneBooks) {
        mPhoneBookCustomAdapter.setData(phoneBooks);
        this.phoneBooksRetrieved = phoneBooks;
    }

    @Override
    public void onLoaderReset(Loader<List<PhoneBook>> loader) {
        mPhoneBookCustomAdapter.setData(null);
    }
}
