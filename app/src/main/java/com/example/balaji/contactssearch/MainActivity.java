package com.example.balaji.contactssearch;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest ;

public class MainActivity extends AppCompatActivity {

    EditText search ;
    Button searchBtn ;
    TextView contactsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.serach);
        searchBtn = findViewById(R.id.search_button);
        contactsList = findViewById(R.id.contact_list);

        searchBtn.setOnClickListener(buttonClickListener);

        requestReadContactsPermission() ;

    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                contactsList.setText("");

                Uri contactUri ;

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)){

                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},0);

                    String s = search.getText().toString() ;

                    contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,s);

                    Cursor contactCursor = getContentResolver().query(contactUri,null,null,null) ;

                    while ((contactCursor.moveToNext())){
                        String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        contactsList.append(Html.fromHtml(name+"<br/>"));
                    }
                }
                else{
                    requestReadContactsPermission();
                }

            }
        }
    };

    public void requestReadContactsPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},100);
        }
    }

}
