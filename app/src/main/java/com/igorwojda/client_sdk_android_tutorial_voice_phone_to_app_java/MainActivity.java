package com.igorwojda.client_sdk_android_tutorial_voice_phone_to_app_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.nexmo.client.NexmoCall;
import com.nexmo.client.NexmoClient;

public class MainActivity extends AppCompatActivity {

    TextView connectionStatusTextView;
    Button answerCallButton;
    Button rejectCallButton;
    Button endCallButton;

    NexmoCall call;
    Boolean incomingCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionStatusTextView = findViewById(R.id.connectionStatus);
        answerCallButton = findViewById(R.id.answerCallButton);
        rejectCallButton = findViewById(R.id.rejectCallButton);
        endCallButton = findViewById(R.id.endCallButton);

        // Init Nexmo client
        NexmoClient client = new NexmoClient.Builder().build(this);

        // set Connection status listener
        client.setConnectionListener((connectionStatus, connectionStatusReason) -> runOnUiThread(() -> connectionStatusTextView.setText(connectionStatus.toString())));

        // login client
        client.login("ALICE_TOKEN");

        // listen for incoming calls
        client.addIncomingCallListener(it -> {
            call = it;

            incomingCall = true;
            updateUI();
        });

        // add buttons listeners
        answerCallButton.setOnClickListener(view -> {
            incomingCall = false;
            updateUI();
            call.answer(null);

        });

        rejectCallButton.setOnClickListener(view -> {
            incomingCall = false;
            updateUI();

            call.hangup(null);
        });

        endCallButton.setOnClickListener(view -> {
            call.hangup(null);

            call = null;
            updateUI();
        });
    }

    private void updateUI() {
        answerCallButton.setVisibility(View.GONE);
        rejectCallButton.setVisibility(View.GONE);
        endCallButton.setVisibility(View.GONE);

        if (incomingCall) {
            answerCallButton.setVisibility(View.VISIBLE);
            rejectCallButton.setVisibility(View.VISIBLE);
        } else if (call != null) {
            endCallButton.setVisibility(View.VISIBLE);
        }
    }
}