package osu.passwordmanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.hardware.Camera;
import android.hardware.Camera.*;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
@SuppressWarnings("deprecation")
public class ImageActivity extends AppCompatActivity {

        private Camera mCamera;
        private CameraPreview mCameraPreview;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image);

            mCamera = getCameraInstance();
            mCamera.setDisplayOrientation(90);
            mCameraPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mCameraPreview);

            Button captureButton = (Button) findViewById(R.id.button_capture);
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCamera.takePicture(null, null, mPicture);
                }
            });
        }

        /**
         * Helper method to access the camera returns null if it cannot get the
         * camera or does not exist
         *
         * @return
         */
        private Camera getCameraInstance() {
            int cameraCount = 0;
            Camera cam = null;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        cam = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Toast.makeText(this, "Front Camera not working",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            return cam;
        }

        PictureCallback mPicture = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getOutputMediaFile();
                if (pictureFile == null) {
                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {

                } catch (IOException e) {
                }
            }
        };

        private static File getOutputMediaFile() {
            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

            return mediaFile;
        }
    }