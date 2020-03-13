package com.dauntlessdev.braintrainer.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dauntlessdev.braintrainer.MainActivity;
import com.dauntlessdev.braintrainer.Music;
import com.dauntlessdev.braintrainer.R;

import static com.dauntlessdev.braintrainer.MainActivity.db;

public class SettingsFragment extends Fragment {
    Switch musicSettings;
    Switch notifSettings;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        musicSettings = root.findViewById(R.id.musicSwitch);
        notifSettings = root.findViewById(R.id.notifSwitch);

        if(MainActivity.notifSwitch == 1){
            notifSettings.setChecked(true);
        }else if(MainActivity.notifSwitch == 0){
            notifSettings.setChecked(false);
        }

        if(MainActivity.musicSwitch == 1){
            musicSettings.setChecked(true);
        }else if(MainActivity.musicSwitch == 0){
            musicSettings.setChecked(false);

        }
        musicSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean info ;
                if(b){
                    Music.mediaPlayer.start();
                    info = db.updateMusic(MainActivity.username, 1);
                }else{
                    Music.mediaPlayer.pause();
                    info = db.updateNotif(MainActivity.username, 0);
                }
                Log.i("Music", info +"");
            }
        });
        notifSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    MainActivity.notifCheck = true;
                }else{
                    MainActivity.notifCheck = false;
                }
            }
        });

        return root;
    }

}