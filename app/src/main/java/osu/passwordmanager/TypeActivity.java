package osu.passwordmanager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TypeActivity extends Activity {

    private EditText password;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        password = (EditText) findViewById(R.id.txtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(password.getText().toString().equals("stuff"))
                {
                    success();
                }

            }

        });

    }
    private void success()
    {
        Intent resultData = new Intent();
        resultData.putExtra("TypeSuccess", true);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}