package osu.passwordmanager;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordList extends AppCompatActivity {

    List<Map<String, String>> passList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(R.layout.activity_password_list);

        initList();

        ListView lv = (ListView) findViewById(R.id.passwordList);

        // Now create a new list adapter bound to the cursor.
        // SimpleListAdapter is designed for binding to a Cursor.
        ListAdapter adapter = new SimpleAdapter(this, passList, android.R.layout.simple_list_item_1, new String[] {"password"}, new int[] {android.R.id.text1});

        // Bind to our new adapter.
        lv.setAdapter(adapter);
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
