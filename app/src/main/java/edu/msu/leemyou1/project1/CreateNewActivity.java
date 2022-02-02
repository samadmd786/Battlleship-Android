package edu.msu.leemyou1.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import edu.msu.leemyou1.project1.Cloud.Cloud;

public class CreateNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
    }

    public void onCreate(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onCreateButton(View view) {
        TextInputLayout name = findViewById(R.id.inputLayoutUsername);
        EditText pass = findViewById(R.id.editTextPassword);
        EditText ver = findViewById(R.id.editTextVerifyPassword);

        String username = name.getEditText().getText().toString();
        String password = pass.getText().toString();
        String verify = ver.getText().toString();

        if (!verify.equals(password)) {
            Toast.makeText(CreateNewActivity.this, R.string.mismatch, Toast.LENGTH_SHORT).show();
        } else if (username.isEmpty()) {
            Toast.makeText(CreateNewActivity.this, R.string.empty, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(CreateNewActivity.this, R.string.empty, Toast.LENGTH_SHORT).show();
        } else if (verify.isEmpty()){
            Toast.makeText(CreateNewActivity.this, R.string.empty, Toast.LENGTH_SHORT).show();
        } else {
            new Thread(() -> {
                // cloud stuff
                Cloud theCloud = Cloud.getInstance();
                // get result
                String access = theCloud.register(username, password);
                // if result is not yes

                if (!access.equals("yes")) {
                    view.post(() -> {
                        // display toast
                        Toast.makeText(view.getContext(), R.string.exists, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // start new intent
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }

            }).start();
        }
    }
}