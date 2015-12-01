package osu.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
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
import java.io.FileOutputStream;

@SuppressWarnings("deprecation")
public class PictureActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private static final int MAX_FACES = 10;
    private static final String IMAGE_FN = "face.jpg";
    private Bitmap background_image;
    private FaceDetector.Face[] faces;
    private int face_count;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
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
            File outputDir = getCacheDir(); // context being the Activity pointer
            File outputFile;
            try {
                outputFile = File.createTempFile("image", "png", outputDir);
                FileOutputStream fos = new FileOutputStream(getCacheDir()+IMAGE_FN);
                fos.write(data);
                fos.close();
            }
            catch(Exception e){
                // if any error occurs
                e.printStackTrace();
            }

            FaceDetector.Face[] faces = updateImage(getCacheDir()+IMAGE_FN);
            // Implement face detector
            success();


        }
    };

    public FaceDetector.Face[] updateImage(String image_fn) {
        // Set internal configuration to RGB_565
        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;

        background_image = BitmapFactory.decodeFile(image_fn, bitmap_options);
        FaceDetector face_detector = new FaceDetector(
                background_image.getWidth(), background_image.getHeight(),
                MAX_FACES);

        faces = new FaceDetector.Face[MAX_FACES];
        // The bitmap must be in 565 format (for now).
        face_count = face_detector.findFaces(background_image, faces);
        return faces;
    }
    private void success()
    {
        Intent resultData = new Intent();
        resultData.putExtra("PictureSuccess", true);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}