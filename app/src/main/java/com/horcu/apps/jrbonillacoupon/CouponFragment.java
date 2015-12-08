package com.horcu.apps.jrbonillacoupon;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class CouponFragment extends Fragment {

    LinearLayout info;
    Button saveChanges;
    Button send;

    LinearLayout couponCard;

    NiceSpinner pctOrVal;
    NiceSpinner cpnType;

    TextView cpnExp;
    Button cpnVal;

    TextView featuredClient;
    TextView clientCount;

    LinearLayout getClients;
    private Button save;
    private ImageView hideBuilder;
    private ArrayList<String> MobNumbers = new ArrayList<>();
    private CircleImageView profile_pic;
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


        send = (Button) rootView.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MobNumbers.size() < 1)
                {
                    Snackbar.make(v,"No clients selected", Snackbar.LENGTH_LONG).show();
                    return;
                }

                int width = couponCard.getLayoutParams().width;
                int height = couponCard.getLayoutParams().width;;
                Bitmap coupon = getCoupon(couponCard,width, height);

                int n = 10000;
                Random generator = new Random();
                n = generator.nextInt(n);
                String fname = "Image-" + n + ".jpg";
                String storageLocation = SaveImageToExternalStorage(fname, coupon);

                SendMMS("5409152215", "test", storageLocation);
                // MultipleSMS();
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

        return rootView;
    }

    private void SendMMS(String number, String text, String imgUrl)
    {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
        sendIntent.putExtra("sms_body", text);
        sendIntent.putExtra("address", number);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgUrl)); //"file:///sdcard/image_4.png"
        sendIntent.setType("image/png");
        startActivity(sendIntent);
    }

    public static Bitmap getCoupon(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    private String SaveImageToExternalStorage(String fname,Bitmap bm)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();


        File file = new File(myDir, fname);
        Log.i("Coupon Page", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
