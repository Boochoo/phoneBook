package com.example.joojoo.phonebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;

/**
 * Created by joojoo on 05/05/15.
 */

// DialogFragment is for the popup message

public class PhoneBookDialog extends DialogFragment {
    private static final String LOG_TAG = PhoneBookDialog.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = " Command ";
    public static final String DELETE_RECORD = " deleteRecord ";
    public static final String DELETE_DATABASE = " deleteDataBase ";
    public static final String CONFIRM_EXIT = " confirmExit ";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        final View view = mLayoutInflater.inflate(R.layout.friends_layout, null);
        String command = getArguments().getString(DIALOG_TYPE);
        if (command.equals(DELETE_RECORD)) {
            final int _id = getArguments().getInt(FriendsContract.FriendsColumns.FRIENDS_ID);
            String name = getArguments().getString(FriendsContract.FriendsColumns.FRIENDS_NAME);
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete" + name + " from your list");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = FriendsContract.Friends.buildFriendUri(String.valueOf(_id));
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);

                    // clearing up if there is any open activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            })
                    // adding another button which cancels if the user is not sure of going out


                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else if (command.equals(DELETE_DATABASE)) {

            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete the whole database");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = FriendsContract.URI_TABLE;
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);

                    // clearing up if there is any open activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            })
            // adding another button which cancels if the user is not sure of going out
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

            }else if(command.equals(CONFIRM_EXIT)){

            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete the whole database");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }else{
            Log.d(LOG_TAG, "Invalid command passed as parameter");
        }
            return builder.create();


    }


}