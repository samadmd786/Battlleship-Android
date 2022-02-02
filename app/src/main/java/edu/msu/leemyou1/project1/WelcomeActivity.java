package edu.msu.leemyou1.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import edu.msu.leemyou1.project1.Cloud.Cloud;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    String namePlayer1;
    String namePlayer2;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button login_button = findViewById(R.id.login_button);
        Button create_button = findViewById(R.id.buttonCreateNew);

        remember = findViewById(R.id.rememberCheckBox);

        login_button.setOnClickListener(this);
        create_button.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if(checkbox.equals("true"))
        {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(!checkbox.equals("true"))
        {
            Toast.makeText(this, "Please Sign In", Toast.LENGTH_SHORT).show();
        }


        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.putString("user", getString(R.string.username));
                    editor.putString("pass", getString(R.string.password));
                    editor.apply();
                }
                else if(!compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });
    }


    public void onStartGame(View view) {
        EditText textPlayer1 = findViewById(R.id.inputUsername);
        EditText textPlayer2 = findViewById(R.id.inputPassword);
        namePlayer1 = textPlayer1.getText().toString();
        namePlayer2 = textPlayer2.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("player2", namePlayer2);
        intent.putExtra("player1", namePlayer1);
        startActivity(intent);
    }

    public void onCreateNew(View view) {
        Intent intent = new Intent(this, CreateNewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:

                // Checking login info against database
                EditText name = findViewById(R.id.inputUsername);
                EditText pass = findViewById(R.id.inputPassword);

                String username = name.getText().toString();
                String password = pass.getText().toString();

                new Thread(() -> {
                    // cloud stuff
                    Cloud theCloud = Cloud.getInstance();
                    // get result
                    String access = theCloud.login(username, password);
                    // if result is not yes
                    if (!access.equals("yes")) {
                        v.post(() -> {
                            // display toast
                            Toast.makeText(v.getContext(), R.string.notexists, Toast.LENGTH_LONG).show();
                        });
                    } else {
                        v.post(() -> {
                            // display toast
                            onStartGame(v);
                        });
                    }

                }).start();
                break;
            case R.id.buttonCreateNew:
                onCreateNew(v);
                break;
        }
    }

}