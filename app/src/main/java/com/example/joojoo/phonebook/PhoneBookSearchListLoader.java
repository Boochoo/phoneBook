package com.example.joojoo.phonebook;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joojoo on 03/05/15.
 */
public class PhoneBookSearchListLoader extends AsyncTaskLoader<List<PhoneBook>> {
     private static final String LOG_TAG = PhoneBookSearchListLoader.class.getSimpleName();
     private List<PhoneBook> mPhoneBooks;
     private ContentResolver mContentResolver;
     private Cursor mCursor;
    private String mFilterText;

    public PhoneBookSearchListLoader(Context context, Uri uri, ContentResolver contentResolver, String filterText){

        super(context);
        mContentResolver = contentResolver;
        mFilterText = filterText;

    }



    @Override
    public List<PhoneBook> loadInBackground() {
        String[] projection = {
                BaseColumns._ID,
                FriendsContract.FriendsColumns.FRIENDS_NAME,
                FriendsContract.FriendsColumns.FRIENDS_PHONE,
                FriendsContract.FriendsColumns.FRIENDS_EMAIL};
                List<PhoneBook> entries = new ArrayList<PhoneBook>();

        String selection = FriendsContract.FriendsColumns.FRIENDS_NAME + " LIKE '" + mFilterText + "%";

        //initialize the cursor
            mCursor = mContentResolver.query(FriendsContract.URI_TABLE,projection,selection,null,null);
        if(mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));

                    String name = mCursor.getString(
                            mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_NAME)
                    );

                    String email = mCursor.getString(
                            mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_EMAIL)
                    );

                    String phone = mCursor.getString(
                            mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_PHONE)
                    );

                    PhoneBook phoneBook = new PhoneBook(_id, name, phone, email);
                    entries.add(phoneBook);
                }while(mCursor.moveToNext());
            }


        }

        return entries;
    }



    @Override
    public void deliverResult(List<PhoneBook> phoneBooks) {
       if(isReset()){
           if(phoneBooks != null){
               mCursor.close();
           }

       }
        List<PhoneBook> oldPhoneBookList = mPhoneBooks;
        if (mPhoneBooks == null | mPhoneBooks.size() == 0){
            Log.d(LOG_TAG, "+++++++ No data here");
        }
        mPhoneBooks = phoneBooks;
        if(isStarted()){
            super.deliverResult(phoneBooks);
        }
        if(oldPhoneBookList != null && oldPhoneBookList != null){
            mCursor.close();

        }

    }

    @Override
    protected void onStartLoading() {
        if(mPhoneBooks != null){
            deliverResult(mPhoneBooks);
        }
        if(takeContentChanged() || mPhoneBooks == null ){
            forceLoad();
        }


    }

    @Override
    protected void onStopLoading() {

        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null){
            mCursor.close();
        }
        mPhoneBooks = null;
    }

    @Override
    public void onCanceled(List<PhoneBook> phoneBooks) {
        super.onCanceled(phoneBooks);
        if(mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
