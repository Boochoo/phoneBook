package com.example.joojoo.phonebook;


import android.net.Uri;
import android.provider.BaseColumns;
//import java.lang.String;

/**
 * Created by joojoo on 29/04/15.
 */
public class FriendsContract {

    interface FriendsColumns {
        String FRIENDS_ID = "_id";
        String FRIENDS_NAME = "friends_name";
        String FRIENDS_EMAIL = "friends_email";
        String FRIENDS_PHONE = "friends_phone";

    }

    public static final String CONTENT_AUTHORITY = "com.example.joojoo.phonebook.provider";
    public static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_FRIENDS = "friends";
    //shortcut for the Uri-> to save up some codes
    public static final Uri URI_TABLE =Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_FRIENDS);

    public static final String[] TOP_LEVEL_PATHS = {
        PATH_FRIENDS
    };

    public static class Friends implements FriendsColumns, BaseColumns{

        // path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FRIENDS).build();
       // multiple row
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd"
                + CONTENT_AUTHORITY + ".friends";
        //single row
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd"
                + CONTENT_AUTHORITY + ".friends";

        public static Uri buildFriendUri(String friendId){
            return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
        }

        public static String getFriendId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }


}


//    private String mFilterText;
//filtering the similarity of the lists from the phonebook

//

//
//
//