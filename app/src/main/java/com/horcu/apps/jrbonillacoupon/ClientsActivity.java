package com.horcu.apps.jrbonillacoupon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class ClientsActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String SELECTED_FRIENDS = "selected";
    private SharedPreferences settings;
    private String me;
    private RecyclerView friendsList;
    ArrayList<User> DisplayedUsersList = new ArrayList<>();

    public ArrayList<String> returnedSelectedUsers = new ArrayList<>();
    public ArrayList<User> allLocalFriends;
    public ArrayList<User> selectedFriends = new ArrayList<>();
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        friendsList = (RecyclerView)findViewById(R.id.users_list);
        friendsList.setLayoutManager(new LinearLayoutManager(this));

        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(this);
        settings = getSharedPreferences("Peez", 0);

        showContacts();

        //eventually save the last set of users in the
        //userPreference and show those intially in the list of contacts
    }

    protected void returnDataToBetpage(Intent intent){
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    protected void showContacts()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                    allLocalFriends = getLocalContacts();

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                try {

                    DisplayedUsersList = new ArrayList<>();
                    for (User user : allLocalFriends) {
                        String email = user.getEmail();
                        if (email != null && !email.equals(me)) {
                            DisplayedUsersList.add(user);
                        }
                    }

                    UserAdapter userAdapter = new UserAdapter(DisplayedUsersList,ClientsActivity.this);// new ArrayAdapter<>(getApplicationContext(), R.layout.user_item, R.id.friend, friends);
                    friendsList.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private ArrayList<User> getLocalContacts(){
        ArrayList<User> users = null;
        try {
            users = new ArrayList<User>();
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            assert phones != null;
            while (phones.moveToNext())
            {
                User user = new User();
                user.setUserName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                user.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                user.setEmail(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME)));
                user.setImageUrl(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));

                users.add(user);

            }
            phones.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.done) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(SELECTED_FRIENDS, selectedFriends);//change to selected friends
            returnDataToBetpage(intent);
        }
    }
}
