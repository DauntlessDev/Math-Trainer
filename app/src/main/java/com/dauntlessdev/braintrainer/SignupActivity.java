

package com.dauntlessdev.braintrainer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {


    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);


        final TextView createButton = findViewById(R.id.createButton);
        final TextView usernameText = findViewById(R.id.usernameText);
        final TextView passwordText = findViewById(R.id.passwordText);
        final TextView confirmationText = findViewById(R.id.confirmPasswordText);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SignupActivity.this)
                        .setTitle("Create")
                        .setMessage("Are you sure you want to create the account?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String user = usernameText.getText().toString().trim();
                                String pwd = passwordText.getText().toString().trim();
                                String cpwd = confirmationText.getText().toString().trim();

                                if(user.isEmpty() || pwd.isEmpty() || cpwd.isEmpty()){
                                    Toast.makeText(SignupActivity.this, "Please input all required details.", Toast.LENGTH_SHORT).show();
                                }else{
                                    if (pwd.equals(cpwd)){
                                        boolean userExists = db.checkUsername(user);
                                        if (userExists){
                                            Toast.makeText(SignupActivity.this, "Username exists.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            long val = db.addUser(user,pwd);
                                            if(val > 0) {
                                                Toast.makeText(SignupActivity.this, "Created Account.", Toast.LENGTH_SHORT).show();
                                                long vals = db.newSettings(user);
                                                if(vals > 0){
                                                    Log.i("Status","Success");
                                                }else{

                                                    Log.i("Status","Failure");
                                                }
                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(SignupActivity.this, "Registration Error, try again.", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }
                                    else{
                                        Toast.makeText(SignupActivity.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}

