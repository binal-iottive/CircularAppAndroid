package com.tofa.circular;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tofa.circular.adapter.AppListAdapter;
import com.tofa.circular.customclass.AppAlertModel;
import com.tofa.circular.customclass.Utils;

import java.util.ArrayList;

public class AlertNewActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gv_app, gv_repetType;
    private AppListAdapter appListAdapter, repeatListAdapter;
    private ArrayList<AppAlertModel> arrayListApp = new ArrayList<>();
    private ArrayList<AppAlertModel> arrayListRepeat = new ArrayList<>();
    private ImageView iv_add_alert;
    private TextView tv_contact;
    private String contact = "All", ContactNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_new);

        gv_app = findViewById(R.id.gv_app);
        gv_repetType = findViewById(R.id.gv_repetType);
        iv_add_alert = findViewById(R.id.iv_add_alert);
        tv_contact = findViewById(R.id.tv_contact);
        iv_add_alert.setOnClickListener(this);
        tv_contact.setOnClickListener(this);

        addGridList();
        appListAdapter = new AppListAdapter(getApplicationContext(), arrayListApp, 1);
        gv_app.setAdapter(appListAdapter);

        repeatListAdapter = new AppListAdapter(getApplicationContext(), arrayListRepeat, 2);
        gv_repetType.setAdapter(repeatListAdapter);
    }

    private void addGridList() {
        arrayListApp.add(new AppAlertModel(R.drawable.phone_call,getString(R.string.phone_call),false));
        arrayListApp.add(new AppAlertModel(R.drawable.message,getString(R.string.text_message),false));
        arrayListApp.add(new AppAlertModel(R.drawable.mail,getString(R.string.apple_mail),false));
        arrayListApp.add(new AppAlertModel(R.drawable.facebook,getString(R.string.facebook),false));
        arrayListApp.add(new AppAlertModel(R.drawable.linkedin,getString(R.string.linkedin),false));
        arrayListApp.add(new AppAlertModel(R.drawable.instagram,getString(R.string.instagram),false));
        arrayListApp.add(new AppAlertModel(R.drawable.messenger,getString(R.string.messanger),false));
        arrayListApp.add(new AppAlertModel(R.drawable.twitter,getString(R.string.twitter),false));
        arrayListApp.add(new AppAlertModel(R.drawable.whatsapp,getString(R.string.whatsapp),false));

        arrayListRepeat.add(new AppAlertModel(R.drawable.dots_1,R.drawable.dots_1_black,true, 1));
        arrayListRepeat.add(new AppAlertModel(R.drawable.dots_2,R.drawable.dots_2_black,false, 2));
        arrayListRepeat.add(new AppAlertModel(R.drawable.dots_3,R.drawable.dots_3_black,false, 3));
        arrayListRepeat.add(new AppAlertModel(R.drawable.dots_4,R.drawable.dots_4_black,false, 4));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_add_alert:
                String alertType="";
                int repeatCount=1, imageDrawable = 0;
                for (AppAlertModel alertModel:arrayListRepeat){
                    if (alertModel.isSelected){
                        repeatCount = alertModel.repeatCount;
                        break;
                    }
                }

                for (AppAlertModel alertModel:arrayListApp){
                    if (alertModel.isSelected){
                        alertType = alertModel.alertType;
                        imageDrawable = alertModel.imageDrawable;
                        break;
                    }
                }

                if (alertType.equals("")){
                    Toast.makeText(this, getString(R.string.please_select_a_app), Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.appAlertArraylist.add(new AppAlertModel(imageDrawable, alertType, repeatCount,contact, ContactNumber));
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.tv_contact:
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, Utils.REQUEST_CONTACT_PICKER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.REQUEST_CONTACT_PICKER:
                    Cursor cursor = null;
                    try {
                        String phoneNo = null;
                        String name = null;

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        phoneNo = cursor.getString(phoneIndex);
                        name = cursor.getString(nameIndex);

                        contact = name;
                        ContactNumber = phoneNo;
                        Log.d("contact:",name+","+phoneNo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.d("contact: Failed", "Not able to pick contactName");
        }
    }
}
