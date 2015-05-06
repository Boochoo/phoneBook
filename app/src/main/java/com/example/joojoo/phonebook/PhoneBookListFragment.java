package com.example.joojoo.phonebook;

import android.content.ContentResolver;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;


import java.util.List;

/**
 * Created by joojoo on 04/05/15.
 */

// This class uses a fragment for the ListView to display phoneBook

public class PhoneBookListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<PhoneBook>> {

    private static final String LOG_TAG = PhoneBookListFragment.class.getSimpleName();
    private PhoneBookCustomAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<PhoneBook> mPhoneBooks;

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        mContentResolver = getActivity().getContentResolver();
        mAdapter = new PhoneBookCustomAdapter(getActivity(), getChildFragmentManager());
        setEmptyText("Got No Friends");
        setListAdapter(mAdapter);
        setListShown(false);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<PhoneBook>> onCreateLoader(int id, Bundle args) {
        mContentResolver = getActivity().getContentResolver();
        return new PhoneBookListLoader(getActivity(), FriendsContract.URI_TABLE, mContentResolver);

    }

    @Override
    public void onLoadFinished(Loader<List<PhoneBook>> loader, List<PhoneBook> phoneBooks) {
        mAdapter.setData(phoneBooks);
        mPhoneBooks = phoneBooks;

        if(isResumed()){
            setListShown(true);
        }else{
            setListShownNoAnimation(false);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<PhoneBook>> loader) {
        mAdapter.setData(null);
    }
}
