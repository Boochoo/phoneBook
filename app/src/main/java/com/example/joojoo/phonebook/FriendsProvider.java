package com.example.joojoo.phonebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

/**
 * Created by joojoo on 29/04/15.
 */
public class FriendsProvider extends ContentProvider {
    //call the database
    private FriendsDatabase mOpenHelper;

    private static String TAG = FriendsProvider.class.getSimpleName();
    //s is a standard for static
    // UriMatcher checks if the Content is valid or not
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //unique ID
    public static final int FRIENDS = 100;
    public static final int FRIENDS_ID = 101;

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;
        //get a single or all
        matcher.addURI(authority, "friends", FRIENDS);
        matcher.addURI(authority, "friends/*", FRIENDS_ID);
        return matcher;
    }

    // delete db onCreate

   @Override
    public boolean onCreate(){
       mOpenHelper = new FriendsDatabase(getContext());
       return true;

   }

   public void deleteDatabase(){
       mOpenHelper.close();
       FriendsDatabase.deleteDatabase(getContext());
       mOpenHelper = new FriendsDatabase(getContext());
   }

   @Override
   public String getType(Uri uri){
       final int match = sUriMatcher.match(uri);
       switch(match){
           case FRIENDS:
               return FriendsContract.Friends.CONTENT_TYPE;
           case FRIENDS_ID:
               return FriendsContract.Friends.CONTENT_ITEM_TYPE;
           default:
               throw new IllegalArgumentException("Unknown Uri" + uri);
       }
   }

    // the query

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match){
            case FRIENDS:
            //do nothing
            break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                 queryBuilder.appendWhere(BaseColumns._ID +"=" + id);

                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);


        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            return null;


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, " insert(uri = " + uri + ", values=" + values.toString());

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FRIENDS:
                long recordId = db.insert(FriendsDatabase.Tables.FRIENDS, null, values);
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);

        }
    }


    //update


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, " insert(uri = " + uri + ", values=" + values.toString());

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;

        switch (match) {
            case FRIENDS:
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                    selectionCriteria = BaseColumns._ID + "=" + id
                            + (!TextUtils.isEmpty(selection)? "AND (" + selection +")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);

        }

        int updateCount = db.update(FriendsDatabase.Tables.FRIENDS, values, selectionCriteria,selectionArgs);
        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, " delete(uri = " + uri );

        if(uri.equals(FriendsContract.URI_TABLE)){
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection)? "AND (" + selection +")" : "");
                int updateCount = db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria,selectionArgs);
                return updateCount;

            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);

        }


    }
}