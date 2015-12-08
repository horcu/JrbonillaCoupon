package com.horcu.apps.jrbonillacoupon;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.squareup.picasso.Picasso;

import org.angmarch.views.NiceSpinner;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogHandler
        , NumberPickerDialogFragment.NumberPickerDialogHandler {

    LinearLayout info;
    Button saveChanges;
    Button send;
    TextView cpnExp;
    Button cpnVal;
    LinearLayout couponCard;

    NiceSpinner pctOrVal;
    NiceSpinner cpnType;

    LinearLayout getClients;

    TextView featuredClient;
    TextView clientCount;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Button save;
    private ImageView hideBuilder;
    private ArrayList<String> MobNumbers = new ArrayList<>();
    private CircleImageView profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

        String txt = "offer expires " + dayOfMonth + " " + monthOfYear + ", " + year;
        cpnExp.setText(txt);

    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        cpnVal.setText("$" + number);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class CouponFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public CouponFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

           info = (LinearLayout)rootView.findViewById(R.id.info);

            couponCard = (LinearLayout)rootView.findViewById(R.id.coupon_card);
            couponCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(info.getVisibility() == View.GONE)
                    info.setVisibility(View.VISIBLE);
                }
            });


            hideBuilder = (ImageView) rootView.findViewById(R.id.hide_cpn_builder);
            hideBuilder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   couponCard.setVisibility(View.GONE);
                }
            });


            clientCount = (TextView)couponCard.findViewById(R.id.client_count);
            featuredClient = (TextView)couponCard.findViewById(R.id.featured_client);
            profile_pic = (CircleImageView)couponCard.findViewById(R.id.profile_image);



//            save = (Button) rootView.findViewById(R.id.save_cpn_builder);
//            save.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//
//                 String txt = ((NiceSpinner)info.findViewById(R.id.coupon_type)).getText().toString();
//                 ((TextView)couponCard.findViewById(R.id.coupon_dtls)).setText(txt);
//
//                  }
//                });

            send = (Button) rootView.findViewById(R.id.send);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(MobNumbers.size() < 1)
                    {
                        Snackbar.make(v,"No clients selected", Snackbar.LENGTH_LONG).show();
                        return;
                    }

//                    for (int i = 0; i < MobNumbers.size(); i++)
//                    {
//                        String message = "This is  a test";
//                        String tempMobileNumber = MobNumbers.get(i).toString();
//                        MultipleSMS(tempMobileNumber, message);
//                    }

                    MultipleSMS("5409152215", "test");
                    Snackbar.make(v,"Sent!", Snackbar.LENGTH_LONG).show();
                }
            });

            cpnExp = (TextView) rootView.findViewById(R.id.expir);
            cpnExp.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DatePickerBuilder datePicker = new DatePickerBuilder()
                          .setFragmentManager(getFragmentManager())
                          .setReference(2)
                          .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                  datePicker.show();
                     }
                });


            cpnVal = (Button) rootView.findViewById(R.id.percOrValTxt);
            cpnVal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

             NumberPickerBuilder numberPicker = new NumberPickerBuilder()
            .setFragmentManager(getFragmentManager())
            .setReference(2)
            .setStyleResId(R.style.BetterPickersDialogFragment_Light);
             numberPicker.show();
                }
            });


            getClients = (LinearLayout) rootView.findViewById(R.id.get_clients);
            getClients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),ClientsActivity.class);
                    startActivityForResult(intent,9999);
                }
            });


           // pctOrVal = (NiceSpinner)rootView.findViewById(R.id.percent_value);

        //    List<String> options = new LinkedList<>(Arrays.asList("off your next appointment", "off for a friend"));
         //   List<String> percorval = new LinkedList<>(Arrays.asList("%", "$"));
         //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_dropdown_item_1line, options);
          //  ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_dropdown_item_1line, percorval);
           // cpnType.setAdapter(adapter);
          //  pctOrVal.setAdapter(adapter2);
            return rootView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == 75535)
        {
            ArrayList<User> users = data.getParcelableArrayListExtra("selected");

            for (User usr : users)
            {
                String phone = usr.getPhone();
                if(!MobNumbers.contains(phone))
                MobNumbers.add(usr.getPhone());
            }

            if(users.size() > 0) {
                featuredClient.setText(users.get(0).getUserName());
                Picasso.with(this).load(users.get(0).getImageUrl()).into(profile_pic);
                String txt = users.size() > 1 ? "+ " + String.valueOf(users.size() - 1) + " others.." : "";
                clientCount.setText(txt);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void MultipleSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ContentValues values = new ContentValues();
                        for (int i = 0; i < MobNumbers.size() - 1; i++) {
                            values.put("address", MobNumbers.get(i).toString());
                            // txtPhoneNo.getText().toString());
                            values.put("body", "this is a test");
                        }
                        getContentResolver().insert(
                                Uri.parse("content://sms/sent"), values);
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a CouponFragment (defined as a static inner class below).
            //return CouponFragment.newInstance(position + 1);
            return new CouponFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
