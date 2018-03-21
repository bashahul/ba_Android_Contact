package bashahul.com.ba_android_contractapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int INTENT_REQUESTCODE_CONTACTPICKER = 1;
    private final int PERM_REQ_CONTACTREAD = 1;

    Button mButton;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.editText);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndCallPicker3();
            }
        });


        //pickContact1();
        //pickContact2();
        checkAndCallPicker3();
    }

    private void checkAndCallPicker3()
    {
        if (IsContactReadPermissionGranted()) {
            pickContact3();
        }
    }

    private void pickContact1()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, INTENT_REQUESTCODE_CONTACTPICKER);
    }

    private void pickContact2()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, INTENT_REQUESTCODE_CONTACTPICKER);
    }

    private void pickContact3()
    {
        new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .showPickerForResult(INTENT_REQUESTCODE_CONTACTPICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTENT_REQUESTCODE_CONTACTPICKER){
            if(resultCode == RESULT_OK) {

                mTextView.setText("");

                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                StringBuilder sb = new StringBuilder();

                for(ContactResult result : results)
                {
                    sb.append(result.getDisplayName()).append(", ");
                }

                mTextView.setText(sb.toString());
                //Log.d("MyTag", "");
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_REQ_CONTACTREAD)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                pickContact3();
            }
        }
    }

    private boolean IsContactReadPermissionGranted()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
           return true;
        }

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS))
        {
            Toast.makeText(this, "Please enable contact read permission", Toast.LENGTH_LONG).show();
        }
        else
        {
            String[] permission = {Manifest.permission.READ_CONTACTS};
            requestPermissions(permission, PERM_REQ_CONTACTREAD);
        }
        return false;
    }
}
