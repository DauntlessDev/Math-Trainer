package com.dauntlessdev.braintrainer.ui.home;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.dauntlessdev.braintrainer.DatabaseHelper;
import com.dauntlessdev.braintrainer.MainActivity;
import com.dauntlessdev.braintrainer.R;

import java.util.ArrayList;
import java.util.Random;

import static java.util.Collections.max;

public class HomeFragment extends Fragment {


    Button[] buttonViews = new Button[4];
    TextView timerTextView;
    TextView scoreTextView;
    Button goButton;
    Button againButton;
    TextView questionTextView;
    TextView statusTextView;
    public static int score = 0;
    int numAnswered = 0;
    int randomAssignCorrect;
    public static int play = 0;
    DatabaseHelper db;
    CountDownTimer cd;
    boolean inFragment;




    public void startTimer(){
        cd =new CountDownTimer(5000+100,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText((millisUntilFinished/1000) + "s");
            }
            @Override
            public void onFinish() {
                timerTextView.setText("0s");
                statusTextView.setText("Done!");
                againButton.setVisibility(View.VISIBLE);
                againButton.setEnabled(true);
                for(Button buttonView : buttonViews){
                    buttonView.setEnabled(false);
                }

                if(inFragment) {

                    long res = db.addScore(MainActivity.username, score, numAnswered);
                    if (res > 0) {
                        Log.i("result", "Success");
                    } else {

                        Log.i("result", "fail");
                    }
                    ArrayList<Integer> allScore = db.getAllScore(MainActivity.username);

                    if(max(allScore) > Integer.parseInt(MainActivity.highScore)){
                        noti();
                    }
                    MainActivity.highScore = String.valueOf(max(allScore));
                    MainActivity.plays = String.valueOf(allScore.size());
                    MainActivity.allScore = allScore;
                    MainActivity.answered = db.getAllAnswered(MainActivity.username);


                }

            }
        }.start();
    }

    public void noti(){



        NotificationCompat.Builder b = new NotificationCompat.Builder(getActivity(), "chanellID");
        b.setSmallIcon(R.drawable.sbraintrain);
        b.setContentTitle("BrainTrainer Achievement");
        b.setContentText("Congrats "+ MainActivity.username +"! You got a new high score.");
        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        notificationIntent.putExtra("menuFragment", "ProfileFragment");
        PendingIntent intent = PendingIntent.getActivity(getActivity(),1,notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        b.setContentIntent(intent);
        b.setPriority(Notification.PRIORITY_MAX);
        b.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        NotificationManager nm =(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,b.build());


    }
    public void generateQuestions(){
        Random random = new Random();
        int randomNum;
        int var1 = random.nextInt(30);
        int var2 = random.nextInt(30);
        questionTextView.setText(var1 + " + " + var2);
        int correctAnswer = var1 + var2;
        randomAssignCorrect = random.nextInt(4);
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        integerArrayList.add(correctAnswer);
        buttonViews[randomAssignCorrect].setText(String.valueOf(correctAnswer));
        for (int i = 0; i < 4; i++){
            if(i == randomAssignCorrect) continue;

            randomNum = random.nextInt(55);
            while (integerArrayList.contains(randomNum)){
                randomNum = random.nextInt(55);
            }integerArrayList.add(randomNum);
            buttonViews[i].setText(String.valueOf(randomNum));
        }
        scoreTextView.setText(score + "/" + numAnswered);
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = new DatabaseHelper(getActivity());
        questionTextView = root.findViewById(R.id.questionTextView);
        timerTextView = root.findViewById(R.id.timerTextView);
        scoreTextView = root.findViewById(R.id.scoreTextView);
        statusTextView = root.findViewById(R.id.statusTextView);
        buttonViews[0] = root.findViewById(R.id.buttonView0);
        buttonViews[1] = root.findViewById(R.id.buttonView1);
        buttonViews[2] = root.findViewById(R.id.buttonView2);
        buttonViews[3] = root.findViewById(R.id.buttonView3);
        goButton = root.findViewById(R.id.goButton);
        againButton = root.findViewById(R.id.againButton);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Start")
                        .setMessage("Are you sure you want to start the playing?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inFragment = true;
                                goButton.setVisibility(View.INVISIBLE);
                                questionTextView.setVisibility(View.VISIBLE);
                                timerTextView.setVisibility(View.VISIBLE);
                                scoreTextView.setVisibility(View.VISIBLE);
                                goButton.setEnabled(false);
                                for(Button buttonView : buttonViews){
                                    buttonView.setVisibility(View.VISIBLE);
                                    buttonView.setEnabled(true);
                                }
                                startTimer();
                                generateQuestions();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        buttonViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = view.getTag().toString().trim();
                numAnswered++;
                statusTextView.setText("Wrong!");

                if (answer.equals(String.valueOf(randomAssignCorrect).trim())){
                    score++;
                    statusTextView.setText("Correct!");
                }generateQuestions();
            }
        });
        buttonViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = view.getTag().toString().trim();
                numAnswered++;
                statusTextView.setText("Wrong!");

                if (answer.equals(String.valueOf(randomAssignCorrect).trim())){
                    score++;
                    statusTextView.setText("Correct!");
                }generateQuestions();
            }
        });
        buttonViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = view.getTag().toString().trim();
                numAnswered++;
                statusTextView.setText("Wrong!");

                if (answer.equals(String.valueOf(randomAssignCorrect).trim())){
                    score++;
                    statusTextView.setText("Correct!");
                }generateQuestions();
            }
        });
        buttonViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = view.getTag().toString().trim();
                numAnswered++;
                statusTextView.setText("Wrong!");

                if (answer.equals(String.valueOf(randomAssignCorrect).trim())){
                    score++;
                    statusTextView.setText("Correct!");
                }generateQuestions();
            }
        });

        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTimer();
                generateQuestions();
                score = 0;
                numAnswered = 0;
                scoreTextView.setText(score + "/" + numAnswered);
                statusTextView.setText("");
                againButton.setVisibility(View.INVISIBLE);
                againButton.setEnabled(false);
                for(Button buttonView : buttonViews){
                    buttonView.setEnabled(true);
                }
            }
        });
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        score = 0;
        numAnswered = 0;
        scoreTextView.setText(score + "/" + numAnswered);
        statusTextView.setText("");

    }

    @Override
    public void onStop() {
        super.onStop();
        inFragment = false;

    }
}