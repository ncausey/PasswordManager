package osu.passwordmanager;

import android.app.Dialog;
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
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordList extends AppCompatActivity {

    List<Pair<String, String>> passList = new ArrayList<Pair<String, String>>();
    PasswordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(R.layout.activity_password_list);

        initList();

        ListView lv = (ListView) findViewById(R.id.passwordList);

        // Now create a new list adapter bound to the cursor.
        adapter = new PasswordListAdapter(passList);
        // Bind to our new adapter.
        lv.setAdapter(adapter);



        // we register for the contextmenu
        registerForContextMenu(lv);

        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {


                // We know the View is a TextView so we can cast it
                TextView clickedView = (TextView) view;

                Toast.makeText(PasswordList.this, "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initList() {
        passList.add(createPasswordPair("Google", "test"));
        passList.add(createPasswordPair("Facebook", "test2"));
    }
    private Pair<String, String> createPasswordPair(String key, String value) {
        Pair<String, String> passMap = new Pair<String, String>(key, value);
        return passMap;
    }

    public void addPassword(View view) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.add_password_dialog);
        d.setTitle("Add password");
        d.setCancelable(true);
        final EditText editAccount = (EditText) d.findViewById(R.id.editTextAccount);
        final EditText editPassword = (EditText) d.findViewById(R.id.editTextPassword);
        Button b = (Button) d.findViewById(R.id.add_password_from_dialog);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String accountName = editAccount.getText().toString();
                String password = editPassword.getText().toString();
                PasswordList.this.passList.add(createPasswordPair(accountName, password));

                // We notify the data model is changed
                PasswordList.this.adapter.notifyDataSetChanged();
                d.dismiss();
            }
        });
        d.show();
    }

    // We want to create a context Menu when the user long click on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        Pair<String, String> account =  (Pair<String, String>) adapter.getItem(aInfo.position);

        menu.setHeaderTitle("Options for " + account.first);
        menu.add(1, 1, 1, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        passList.remove(aInfo.position);
        adapter.notifyDataSetChanged();
        return true;
    }

}
