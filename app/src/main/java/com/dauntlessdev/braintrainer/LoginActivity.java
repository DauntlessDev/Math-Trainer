package com.dauntlessdev.braintrainer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    public static TextView usernameText;
    static TextView passwordText;
    static TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final NotificationCompat.Builder b = new NotificationCompat.Builder(this, "chanellID");
        b.setSmallIcon(R.drawable.sbraintrain);
        b.setContentTitle("Welcome Message");
        b.setContentText("Thankyou for installing the game! :)");
        PendingIntent intent = PendingIntent.getActivity(this,1,new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);
        b.setContentIntent(intent);
        final NotificationManager nm =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        signupText = findViewById(R.id.signupText);

        Button loginButton = findViewById(R.id.createButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login")
                        .setMessage("Are you sure you want to login your account?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                nm.notify(0,b.build());
                                String user = usernameText.getText().toString().trim();
                                String pwd = passwordText.getText().toString().trim();

                                String pass = db.searchPass(user);
                                if(pwd.equals(pass)){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Invalid Username/Password, Try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

}