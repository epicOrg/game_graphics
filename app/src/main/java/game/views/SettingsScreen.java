package game.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alessandro.computergraphicsexample.R;

import org.json.JSONException;
import org.json.JSONObject;

import game.GameManager;
import game.Team;
import game.player.Player;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by Andrea on 12/05/2015.
 */
public class SettingsScreen {

    public static final String LOG_TAG = "SettingsScreen";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private Activity activity;
    private LinearLayout container;
    private String username, roomName;
    private int hashcode;

    private SettingsScreen settingsScreen;
    private GameManager gameManager;

    public SettingsScreen(Activity activity, LinearLayout container, String username, int hashcode, String roomName) {
        this.activity = activity;
        this.container = container;
        this.username = username;
        this.hashcode = hashcode;
        this.roomName = roomName;

        settingsScreen = this;

        setup();
    }

    private void setup() {
        gameManager = GameManager.getInstance();

        SettingsContainer settingsContainer = new SettingsContainer(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        settingsContainer.setLayoutParams(layoutParams);

        int padding = activity.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        settingsContainer.setPadding(padding, padding, padding, padding);
        settingsContainer.setOrientation(LinearLayout.VERTICAL);

        Button muteButton = (Button) activity.findViewById(R.id.game_menu_mute_unmute);
        Button quitButton = (Button) activity.findViewById(R.id.game_menu_quit);
        Button cancelButton = (Button) activity.findViewById(R.id.game_menu_cancel);

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Muting/UnMuting..", Toast.LENGTH_SHORT).show();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Quitting..", Toast.LENGTH_SHORT).show();
                try {
                    serverCommunicationThread.send(createExitRequest());
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsScreen.hide();
            }
        });

        container.removeAllViews();
        settingsContainer.addView(muteButton);
        settingsContainer.addView(quitButton);
        settingsContainer.addView(cancelButton);

        ScrollView scrollView = new ScrollView(activity);
        scrollView.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout playersContainer = new LinearLayout(activity);
        LinearLayout.LayoutParams playersLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (Team t : gameManager.getRoom().getTeams()) {
            playersContainer.setLayoutParams(playersLayout);
            playersContainer.setOrientation(LinearLayout.VERTICAL);

            TextView teamName = new TextView(activity);
            teamName.setText(t.getName());
            teamName.setTypeface(teamName.getTypeface(), Typeface.BOLD);

            playersContainer.addView(teamName);

            for (Player p : t.getPlayers()) {
                TextView playerName = new TextView(activity);
                playerName.setText(p.getName());
                playerName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                playersContainer.addView(playerName);
            }
        }

        scrollView.addView(playersContainer);
        settingsContainer.addView(scrollView);
        container.addView(settingsContainer);
    }

    public void show() {
        Log.d(LOG_TAG, "show");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.VISIBLE);
                container.bringToFront();
            }
        });
    }

    private JSONObject createExitRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.GAME);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_STATUS);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
            request.put(FieldsNames.ROOM_NAME, roomName);
            request.put(FieldsNames.GAME_EXIT, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    public void hide() {
        Log.d(LOG_TAG, "hide");

        container.setVisibility(View.GONE);
    }

    private class SettingsContainer extends LinearLayout {

        public SettingsContainer(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }
    }

}
