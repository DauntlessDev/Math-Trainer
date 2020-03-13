package com.dauntlessdev.braintrainer.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dauntlessdev.braintrainer.DatabaseHelper;
import com.dauntlessdev.braintrainer.LoginActivity;
import com.dauntlessdev.braintrainer.MainActivity;
import com.dauntlessdev.braintrainer.R;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    static Context context;
    DatabaseHelper db;
    public TextView highest,plays,username;
    public ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> sentence;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        highest = root.findViewById(R.id.highestText);
        plays = root.findViewById(R.id.playsText);
        username = root.findViewById(R.id.usernameText);
        username.setText("IN: " + LoginActivity.usernameText.getText());

        listView = root.findViewById(R.id.listView);



        return root;

    }

    @Override
    public void onStart() {
        super.onStart();

        highest.setText("Highest: " +MainActivity.highScore);
        plays.setText("Plays: " + MainActivity.plays);

        sentence = new ArrayList<>();
        for(int i = Integer.parseInt(MainActivity.plays ) -1; i>= 0; i--)
            sentence.add("In play number " + (i + 1) + ", you got a score of " +
                    MainActivity.allScore.get(i)+"/"+MainActivity.answered.get(i));

        arrayAdapter = new ArrayAdapter<String>(
                        getActivity().getApplicationContext(), R.layout.list_item, R.id.list_content, sentence);
        listView.setAdapter(arrayAdapter);
        listView.setClickable(false);

    }

}