package com.example.joojoo.phonebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joojoo on 04/05/15.
 */

// this class will receive data from the fragment and displays it
// a conjuctional route for the layout and the fragment
public class PhoneBookCustomAdapter extends ArrayAdapter<PhoneBook> {

    // a variable that expands the layout
    private LayoutInflater mLayoutInflater;
    private static FragmentManager sFragmentManager;


    // initialize the adapter
    public PhoneBookCustomAdapter(Context context, FragmentManager fragmentManager){
        // letting the adapter know the default layout
        super(context, android.R.layout.simple_list_item_2);
        // a mechanism used by the xml to inflate
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sFragmentManager = fragmentManager;
    }

    //getView gets the data from xml puts on the screen


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.custom_friend, parent, false);
        }else{
            view = convertView;
        }

        //gets the correct record
        final PhoneBook phoneBook = getItem(position);

        final int _id = phoneBook.getId();
        final String name = phoneBook.getName();
        final String email = phoneBook.getEmail();
        final String phone = phoneBook.getPhone();

// get the data
        ((TextView) view.findViewById(R.id.friend_name)).setText(name);
        ((TextView) view.findViewById(R.id.friend_phone)).setText(phone);
        ((TextView) view.findViewById(R.id.friend_email)).setText(email);

        Button editButton = (Button) view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent will take us to the other

                Intent intent = new Intent(getContext(), EditActivity.class);

                //send the parameters to the EditActivity class
                intent.putExtra(FriendsContract.FriendsColumns.FRIENDS_ID, String.valueOf(_id));
                intent.putExtra(FriendsContract.FriendsColumns.FRIENDS_EMAIL, email);
                intent.putExtra(FriendsContract.FriendsColumns.FRIENDS_NAME, name);
                intent.putExtra(FriendsContract.FriendsColumns.FRIENDS_PHONE, phone);

                // then start the activity

                getContext().startActivity(intent);
            }
        });

        Button deleteButton = (Button) view.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneBookDialog dialog = new PhoneBookDialog();

                Bundle args = new Bundle();

                args.putString(PhoneBookDialog.DIALOG_TYPE, PhoneBookDialog.DELETE_RECORD);
                args.putInt(FriendsContract.FriendsColumns.FRIENDS_ID, _id);
                args.putString(FriendsContract.FriendsColumns.FRIENDS_NAME, name);
                dialog.setArguments(args);
                dialog.show(sFragmentManager, "delete-record");
            }
        });

        return view;
    }

    //a method for setting the data that will be implemented

    public void setData(List<PhoneBook> phoneBooks){
        clear();
        if(phoneBooks != null ){
             for(PhoneBook phoneBook : phoneBooks){
                 add(phoneBook);
             }
        }
    }
}
