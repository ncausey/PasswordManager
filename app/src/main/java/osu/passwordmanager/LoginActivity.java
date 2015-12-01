package osu.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import java.lang.reflect.Type;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{

    private static final int VOICE_REQUEST = 43210;
    private static final int CAM_REQUEST =1319;
    private static final int TYPE_REQUEST =12345;

    private boolean VoiceSuccess = false;
    private boolean PictureSuccess = false;
    private boolean TypeSuccess = false;

    public static String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button VoiceSignInButton = (Button) findViewById(R.id.voicebutton);
        VoiceSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!VoiceSuccess)
                    attemptVoiceLogin();
                else
                    showToastMessage("Completed");
            }
        });

        final Button PictureSignInButton = (Button) findViewById(R.id.picturebutton);
        PictureSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!PictureSuccess)
                    attemptPictureLogin();
                else
                    showToastMessage("Completed");
            }
        });

        Button TypeSignInButton = (Button) findViewById(R.id.typebutton);
        TypeSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TypeSuccess)
                    attemptTypeLogin();
                else
                    showToastMessage("Completed");
            }
        });
    }
    private void attemptVoiceLogin() {
        Intent i = new Intent(getBaseContext(), VoiceActivity.class);
        //i.putExtra("PersonID", personID);
        startActivityForResult(i, VOICE_REQUEST);
    }
    private void attemptPictureLogin() {
        Intent i = new Intent(getBaseContext(), PictureActivity.class);
        //i.putExtra("PersonID", personID);
        startActivityForResult(i, CAM_REQUEST);
    }
    private void attemptTypeLogin() {
        Intent i = new Intent(getBaseContext(), TypeActivity.class);
        //i.putExtra("PersonID", personID);
        startActivityForResult(i, TYPE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_REQUEST)
            if(resultCode == RESULT_OK) {
                VoiceSuccess = data.getBooleanExtra("VoiceSuccess",false);
            }
        if (requestCode == CAM_REQUEST)
            if(resultCode == RESULT_OK) {
                PictureSuccess = data.getBooleanExtra("PictureSuccess",false);
            }
        if (requestCode == TYPE_REQUEST)
            if(resultCode == RESULT_OK) {
                TypeSuccess = data.getBooleanExtra("TypeSuccess",false);
            }
        showToastMessage(VoiceSuccess + " " + PictureSuccess + " " + TypeSuccess);

        if((VoiceSuccess&&PictureSuccess)||(VoiceSuccess&&TypeSuccess)||(TypeSuccess&&PictureSuccess))
        {
            VoiceSuccess = false;
            PictureSuccess = false;
            TypeSuccess = false;
            Context context = getApplicationContext();
            Intent intent = new Intent(context, PasswordList.class);
            startActivity(intent);
        }
    }
    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

