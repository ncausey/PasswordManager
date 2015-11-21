package osu.passwordmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordList extends AppCompatActivity {

    List<Map<String, String>> passList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_password_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initList();

        ListView lv = (ListView) findViewById(R.id.passwordList);

        ListAdapter adapter = new SimpleAdapter(this, passList, android.R.layout.simple_list_item_1, new String[] {"password"}, new int[] {android.R.id.text1});

        // Bind to our new adapter.
        lv.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initList() {
        passList.add(createPasswordMap("password", "test"));
        passList.add(createPasswordMap("password", "test2"));
    }

    private HashMap<String, String> createPasswordMap(String key, String value) {
        HashMap<String, String> passMap = new HashMap<String, String>();
        passMap.put(key, value);
        return passMap;
    }
}
