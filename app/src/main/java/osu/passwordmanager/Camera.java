package osu.passwordmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Camera extends AppCompatActivity {
    Button btnTakePhoto;
    ImageView imageTakenPhoto;
    private static final int CAM_REQUEST =1319;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        btnTakePhoto = (Button) findViewById(R.id.button1);
        imageTakenPhoto = (ImageView) findViewById(R.id.imageview1);
        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == CAM_REQUEST)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageTakenPhoto.setImageBitmap(thumbnail);

        }

    }
    class btnTakePhotoClicker implements Button.OnClickListener{
        @Override
        public void onClick(View v){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,CAM_REQUEST);
        }
    }

}
