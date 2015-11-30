package osu.passwordmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.LauncherActivity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PasswordListAdapter extends BaseAdapter {

    private List<Pair<String, String>> passwordList;

    public PasswordListAdapter(List<Pair<String, String>> passwordList) {
        this.passwordList = passwordList;
    }

    public int getCount() {
        return passwordList.size();
    }

    public Pair<String, String> getItem(int position) {
        return passwordList.get(position);
    }

    public long getItemId(int position) {
        return passwordList.get(position).hashCode();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.listitem, parent, false);
        }

        TextView accountName = (TextView) view.findViewById(R.id.accountView);
        TextView accountPassword = (TextView) view.findViewById(R.id.passwordView);

        Pair<String, String> account = passwordList.get(position);

        accountName.setText(account.first);
        accountPassword.setText(account.second);

        return view;
    }
}