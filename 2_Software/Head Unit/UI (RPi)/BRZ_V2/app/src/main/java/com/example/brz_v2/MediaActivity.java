package com.example.brz_v2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.brz_v2.MediaDisplay.DefenceFragment;
import com.example.brz_v2.MediaDisplay.DiagnosticFragment;
import com.example.brz_v2.MediaDisplay.LightFragment;
import com.example.brz_v2.MediaDisplay.MediaFragment;
import com.example.brz_v2.MediaDisplay.MergingFragment;
import com.example.brz_v2.MediaDisplay.NavigationFragment;
import com.example.brz_v2.MediaDisplay.ParkingFragment;
import com.example.brz_v2.MediaDisplay.PopUpDefenceFragment;
import com.example.brz_v2.MediaDisplay.SafetyFragment;
import com.example.brz_v2.MediaDisplay.SettingsFragment;
import com.example.brz_v2.MediaDisplay.SoundBoardFragment;
import com.example.brz_v2.MediaDisplay.TrafficAdvisorFragment;
import com.example.brz_v2.Services.DefenceService;
import com.example.brz_v2.Services.DiagnosticService;
import com.example.brz_v2.Services.MediaService;
import com.example.brz_v2.Services.NavigationService;
import com.example.brz_v2.Services.NetworkService;
import com.example.brz_v2.Services.SafetyService;
import com.example.brz_v2.Services.TrafficAdvisorService;
import com.example.brz_v2.databinding.ActivityMediaBinding;

import org.json.JSONObject;

public class MediaActivity extends AppCompatActivity {

    //legal mode is true to begin with such that hidden features cannot be revealed by rebooting the system
    boolean legalMode = false; //TODO: set to true during final compilation
    //this is used to count keystrokes in a code to unlock illegal features once they have been hidden
    //any time the right key is hit, the counter goes up unless the wrong key is hit in which case it goes to 0 again
    int legalModeUnlockCounter = 0;

    //keyboard letters are defined here for ease during coding
    int rs_key_up = KeyEvent.KEYCODE_A;
    int rs_key_right = KeyEvent.KEYCODE_B;
    int rs_key_down = KeyEvent.KEYCODE_C;
    int rs_key_left = KeyEvent.KEYCODE_D;
    int rs_key_enter = KeyEvent.KEYCODE_E;
    int rs_key_back = KeyEvent.KEYCODE_F;
    int rs_key_voice = KeyEvent.KEYCODE_G;
    int ls_key_source = KeyEvent.KEYCODE_H;
    int ls_key_call_pickup = KeyEvent.KEYCODE_I;
    int ls_key_call_hangup = KeyEvent.KEYCODE_J;
    int ls_key_volume_up = KeyEvent.KEYCODE_K;
    int ls_key_volume_down = KeyEvent.KEYCODE_L;
    int ls_key_right = KeyEvent.KEYCODE_M;
    int ls_key_left = KeyEvent.KEYCODE_N;
    int ls_key_enter = KeyEvent.KEYCODE_O;

    //big fragments
    private NavigationFragment navigationFragment;
    private LightFragment lightFragment;
    private SafetyFragment safetyFragment;
    private SettingsFragment settingsFragment;
    private DefenceFragment defenceFragment;

    //small fragments
    private MediaFragment mediaFragment;
    private DiagnosticFragment diagnosticFragment;
    private SoundBoardFragment soundBoardFragment;
    private PopUpDefenceFragment popUpDefenceFragment;
    private TrafficAdvisorFragment trafficAdvisorFragment;

    //fullscreen popup fragments
    private ParkingFragment parkingFragment;
    private MergingFragment mergingFragment;

    DefenceService defenceService = new DefenceService(this);
    DiagnosticService diagnosticService = new DiagnosticService(this);
    MediaService mediaService = new MediaService(this);
    NavigationService navigationService = new NavigationService(this);
    SafetyService safetyService = new SafetyService(this);
    TrafficAdvisorService trafficAdvisorService = new TrafficAdvisorService(this);
    NetworkService networkService = new NetworkService(this);

    ImageView bigFragmentHighlight;
    ImageView smallFragmentHighlight;

    private final int navigationFragmentID = 0;
    private final int lightFragmentID = 1;
    private final int safetyFragmentID = 2;
    private final int settingsFragmentID = 3;
    private final int defenceFragmentID = 4;

    private final int mediaFragmentID = 0;
    private final int diagnosticFragmentID = 1;
    private final int soundBoardFragmentID = 2;
    private final int popUpDefenceFragmentID = 3;
    private final int trafficAdvisorFragmentID = 4;

    private final int parkingFragmentID = 0;
    private final int mergingFragmentID = 1;

    private int highlightedFragmentID = 0;
    private boolean fragmentSwapState = false;
    public static int liveBigFragmentID = 0;
    public static int liveSmallFragmentID = 0;
    public static int liveOverlayFragmentID = 0;

    boolean popUpDefenseFragmentState = false;
    static boolean isBigFragmentAlerting = false;
    static boolean isSmallFragmentAlerting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMediaBinding binding;
        binding = ActivityMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hide system UI
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //get fragment highlight views
        bigFragmentHighlight = requireViewById(R.id.big_fragment_highlight_view);
        smallFragmentHighlight = requireViewById(R.id.small_fragment_highlight_view);

        //initialize fragments now that activity is created
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //add fragments to their containers
        //parent activity is passed if fragment provides input to service
        navigationFragment = new NavigationFragment();
        lightFragment = new LightFragment(this);
        safetyFragment = new SafetyFragment();
        settingsFragment = new SettingsFragment();
        defenceFragment = new DefenceFragment(this);

        mediaFragment = new MediaFragment(this);
        diagnosticFragment = new DiagnosticFragment(this);
        soundBoardFragment = new SoundBoardFragment(this);
        trafficAdvisorFragment = new TrafficAdvisorFragment(this);
        popUpDefenceFragment = new PopUpDefenceFragment();

        parkingFragment = new ParkingFragment();
        mergingFragment = new MergingFragment();


        fragmentTransaction.add(R.id.big_fragment, navigationFragment); //ID 0
        fragmentTransaction.add(R.id.big_fragment, lightFragment); //ID 1
        fragmentTransaction.add(R.id.big_fragment, safetyFragment); //ID 2
        fragmentTransaction.add(R.id.big_fragment, settingsFragment); //ID 3
        fragmentTransaction.add(R.id.big_fragment, defenceFragment); //ID 4
        //TODO:add convenience fragment?
        fragmentTransaction.add(R.id.small_fragment, mediaFragment); //ID 0
        fragmentTransaction.add(R.id.small_fragment, diagnosticFragment); //ID 1
        fragmentTransaction.add(R.id.small_fragment, soundBoardFragment); //ID 2
        fragmentTransaction.add(R.id.small_fragment, popUpDefenceFragment); //ID 3
        fragmentTransaction.add(R.id.small_fragment, trafficAdvisorFragment); //ID 4

        fragmentTransaction.add(R.id.overlay_fragment, parkingFragment);
        fragmentTransaction.add(R.id.overlay_fragment, mergingFragment);

        //show navigation and media fragments
        fragmentTransaction.hide(lightFragment);
        fragmentTransaction.hide(settingsFragment);
        fragmentTransaction.hide(defenceFragment);
        fragmentTransaction.hide(mediaFragment); //MODIFIED
        fragmentTransaction.hide(soundBoardFragment);
        fragmentTransaction.hide(popUpDefenceFragment);
        fragmentTransaction.hide(trafficAdvisorFragment);
        fragmentTransaction.hide(parkingFragment);
        fragmentTransaction.hide(mergingFragment);

        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //first portion handles fragment swapping and highlights
        if (keyCode == rs_key_back) {
            //fragment swap initiated
            //check if fragment was was already initiated and if not, highlight big fragment
            if (!fragmentSwapState) {
                setFragmentSwapState(true);
            } else //exit fragment swap
                setFragmentSwapState(false);
        } else if (keyCode == rs_key_enter) {
            if (fragmentSwapState) { //end fragment swap
                setFragmentSwapState(false);
            } else if (liveBigFragmentID == defenceFragmentID && !legalMode) { //toggle jammer hail mary
                if ((defenceService.isCellJamState() && defenceService.isRadioJamState()) && (defenceService.isLidarJamState() && defenceService.isRadarJamState())) {
                    defenceService.setCellJamState(false);
                    defenceService.setRadioJamState(false);
                    defenceService.setLidarJamState(false);
                    defenceService.setRadarJamState(false);
                } else {
                    defenceService.setCellJamState(true);
                    defenceService.setRadioJamState(true);
                    defenceService.setLidarJamState(true);
                    defenceService.setRadarJamState(true);
                }
            }
        } else if (keyCode == rs_key_up) {
            if (fragmentSwapState) //swap fragment in current container
                fragmentChange(true); //up
            else if (liveBigFragmentID == defenceFragmentID && !legalMode) //if not swapping fragments, then act as button but check legalMode
                if (defenceService.isCellJamState())
                    defenceService.setCellJamState(false);
                else
                    defenceService.setCellJamState(true);
        } else if (keyCode == rs_key_down) {
            if (fragmentSwapState) //swap fragment in current container
                fragmentChange(false);
            else if (liveBigFragmentID == defenceFragmentID && !legalMode)
                if (defenceService.isLidarJamState())
                    defenceService.setLidarJamState(false);
                else
                    defenceService.setLidarJamState(true);
        } else if (keyCode == rs_key_right) {
            if (fragmentSwapState) //change selected fragment
                fragmentSelectionChange();
            else if (liveBigFragmentID == defenceFragmentID && !legalMode)
                if (defenceService.isRadioJamState())
                    defenceService.setRadioJamState(false);
                else
                    defenceService.setRadioJamState(true);
        } else if (keyCode == rs_key_left) {
            if (fragmentSwapState) //same as rs_key_right
                fragmentSelectionChange();
            else if (liveBigFragmentID == defenceFragmentID && !legalMode)
                if (defenceService.isRadarJamState())
                    defenceService.setRadarJamState(false);
                else
                    defenceService.setRadarJamState(true);
        } else if (keyCode == rs_key_voice) { //panic button
            if (liveBigFragmentID == defenceFragmentID) { //defence fragment
                defenceService.setCellJamState(false);
                defenceService.setRadioJamState(false);
                defenceService.setLidarJamState(false);
                defenceService.setRadarJamState(false);
            }
        } else if (keyCode == ls_key_source) {
            if (liveSmallFragmentID == mediaFragmentID || liveSmallFragmentID == popUpDefenceFragmentID) //media stuff
                mediaService.toggleSource();
            else if (liveSmallFragmentID == trafficAdvisorFragmentID) //panic within trafficAdvisorFragment
                setLegalMode();
            else if (legalMode) //if legal mode is enabled, this button becomes jammer toggle
                defenceService.toggleLegalJammers();

        } else if (keyCode == ls_key_enter) {
            if (liveSmallFragmentID == mediaFragmentID || liveSmallFragmentID == popUpDefenceFragmentID)
                mediaService.togglePlay();
            //used to be code to disable police horn but removed since onKeyDown got called repeatedly and it was a mess
        } else if (keyCode == ls_key_right) {
            if (liveSmallFragmentID == mediaFragmentID || liveSmallFragmentID == popUpDefenceFragmentID)
                mediaService.skipSong(true);
            else if (liveSmallFragmentID == trafficAdvisorFragmentID && !legalMode) //toggle light cycle
                trafficAdvisorService.setLightState(trafficAdvisorFragment.getSelectedLightPattern(), !trafficAdvisorService.getLightState());
        } else if (keyCode == ls_key_left) {
            if (liveSmallFragmentID == mediaFragmentID || liveSmallFragmentID == popUpDefenceFragmentID)
                mediaService.skipSong(false);
            else if (liveSmallFragmentID == trafficAdvisorFragmentID)
                trafficAdvisorService.setLegalMode();
        } else if (keyCode == ls_key_volume_up)
            mediaService.changeVolume(true);
        else if (keyCode == ls_key_volume_down)
            mediaService.changeVolume(false);
        else if (keyCode == ls_key_call_pickup) {
            //nothing to be done, removed code for canceling sirenState since it is useless
        } else if (keyCode == ls_key_call_hangup)
            if (liveSmallFragmentID == trafficAdvisorFragmentID && !legalMode)
                trafficAdvisorService.setSirenState(!trafficAdvisorService.getSirenState());

        //check for legalMode unlock code
        //do this at end to prevent any the final keystroke (Enter) from enabling all jammers
        legalModeUnlock(keyCode);

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (liveSmallFragmentID == trafficAdvisorFragmentID && keyCode == ls_key_enter)
            trafficAdvisorService.affirmHornState();
        else if (liveSmallFragmentID == trafficAdvisorFragmentID && keyCode == ls_key_call_pickup)
            trafficAdvisorService.affirmLightState();
        return super.onKeyDown(keyCode, event);
    }

    //forwards data from service to fragment since passing fragment to service did not work
    public void forwardHornState(boolean state) {
        trafficAdvisorFragment.updateHornState(state);
    }

    public void forwardLightState(boolean state) {
        trafficAdvisorFragment.updateLightState(state);

        //also alert fragment if using illegal light pattern
        if (state) {
            if (trafficAdvisorFragment.getSelectedLightPattern() <= 1) //legal but still shouldn't be used
                fragmentAlert(false, 1);
            else
                fragmentAlert(false, 2);
        } else if (!trafficAdvisorService.getSirenState())
            cancelAlert(false);
    }

    public void forwardSirenState(boolean state) {
        trafficAdvisorFragment.updateSirenState(state);

        //also alert fragment
        if (state)
            fragmentAlert(false, 2);
        else if (!trafficAdvisorService.getLightState())
            cancelAlert(false);
    }

    public void forwardSelectedLightState(int lightState) {
        //send state from fragment to trafficAdvisorService
    }

    public void forwardLightSettings(int lightID, boolean state) {

    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == rs_key_voice)
            setLegalMode();
        return super.onKeyLongPress(keyCode, event);
    }

    void fragmentChange(boolean direction) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (direction) { //up
            if (highlightedFragmentID == 0) { //manage big fragments
                fragmentTransaction.hide(getFragment(true, liveBigFragmentID));
                liveBigFragmentID++;
                fragmentTransaction.show(getFragment(true, liveBigFragmentID));
            } else { //manage small fragments
                fragmentTransaction.hide(getFragment(false, liveSmallFragmentID));
                liveSmallFragmentID++;
                fragmentTransaction.show(getFragment(false, liveSmallFragmentID));
            }
        } else {
            if (highlightedFragmentID == 0) { //manage big fragments
                fragmentTransaction.hide(getFragment(true, liveBigFragmentID));
                liveBigFragmentID--;
                fragmentTransaction.show(getFragment(true, liveBigFragmentID));
            } else { //manage small fragments
                fragmentTransaction.hide(getFragment(false, liveSmallFragmentID));
                liveSmallFragmentID--;
                fragmentTransaction.show(getFragment(false, liveSmallFragmentID));
            }
        }
        fragmentTransaction.commit();
    }

    Fragment getFragment(boolean isBigFragment, int fragmentID) {
        //here fragmentID is relative to whether bigFragment or smallFragment container is referenced
        Fragment outputFragment = new Fragment(); //had to add this because android studio gave me shit for returning fragments from nested if statements and not initializing it
        if (isBigFragment) { //return fragments from big fragment
            if (fragmentID == defenceFragmentID || fragmentID == -1) { //return defence if not legalMode
                if (legalMode) {
                    outputFragment = settingsFragment;
                    liveBigFragmentID = settingsFragmentID;
                } else {
                    outputFragment = defenceFragment;
                }
            } else if (fragmentID == navigationFragmentID)
                outputFragment = navigationFragment;
            else if (fragmentID == lightFragmentID)
                outputFragment = lightFragment;
            else if (fragmentID == safetyFragmentID)
                outputFragment = safetyFragment;
            else if (fragmentID == settingsFragmentID)
                outputFragment = settingsFragment;
            else { //return navigation if overshot defence
                outputFragment = navigationFragment;
                liveBigFragmentID = navigationFragmentID;
            }
        } else {
            if (fragmentID == -1 || fragmentID == trafficAdvisorFragmentID) { //undershot
                if (legalMode) {
                    outputFragment = soundBoardFragment;
                    liveSmallFragmentID = soundBoardFragmentID;
                } else {
                    outputFragment = trafficAdvisorFragment;
                    liveSmallFragmentID = trafficAdvisorFragmentID;
                }
            } else if (fragmentID == mediaFragmentID)
                outputFragment = mediaFragment;
            else if (fragmentID == diagnosticFragmentID)
                outputFragment = diagnosticFragment;
            else if (fragmentID == soundBoardFragmentID)
                outputFragment = soundBoardFragment;
            else if (fragmentID == popUpDefenceFragmentID) {
                if (legalMode) {
                    outputFragment = soundBoardFragment;
                    liveSmallFragmentID = soundBoardFragmentID;
                } else
                    outputFragment = popUpDefenceFragment;
            } else { //overshot
                outputFragment = mediaFragment;
                liveSmallFragmentID = mediaFragmentID;
            }
        }
        return outputFragment;
    }

    void setFragmentSwapState(boolean state) {
        if (state) {
            //remember that fragment swap state is now active
            fragmentSwapState = true;
            bigFragmentHighlight.setColorFilter(Color.BLACK); //reset in case another colour is applied
            smallFragmentHighlight.setColorFilter(Color.BLACK);
            if (highlightedFragmentID == 0) {
                //0 is big fragment and 1 is small fragment
                //this is used just as an extra feature where the head unit remembers which fragment was highlighted prior
                bigFragmentHighlight.setVisibility(View.VISIBLE);
            } else {
                smallFragmentHighlight.setVisibility(View.VISIBLE);
            }
        } else {
            bigFragmentHighlight.setVisibility(View.INVISIBLE);
            smallFragmentHighlight.setVisibility(View.INVISIBLE);
            fragmentSwapState = false;
        }
    }

    void fragmentSelectionChange() {
        if (highlightedFragmentID == 0) { //big fragment highlighted so go to small
            highlightedFragmentID++;
            bigFragmentHighlight.setVisibility(View.INVISIBLE);
            smallFragmentHighlight.setVisibility(View.VISIBLE);
        } else {
            highlightedFragmentID = 0;
            bigFragmentHighlight.setVisibility(View.VISIBLE);
            smallFragmentHighlight.setVisibility(View.INVISIBLE);
        }
    }

    public void fragmentAlert(boolean isBigFragment, int alertLevel) {
        //ensure multiple alerts can't be called at once
        if ((isBigFragmentAlerting && isBigFragment) || (isSmallFragmentAlerting && !isBigFragment))
            return;

        //cancel fragment swap state if active
        setFragmentSwapState(false);

        //set state variable which gets checked inside thread loop
        if (isBigFragment)
            isBigFragmentAlerting = true;
        else
            isSmallFragmentAlerting = true;

        Thread thread = new Thread() {
            @Override
            public void run() {
                //remember alertLevel by setting highlight colour
                int highlightColor;
                if (alertLevel == 1)
                    highlightColor = Color.YELLOW;
                else
                    highlightColor = Color.RED;

                //do alert animation
                if (isBigFragment) {
                    while (isBigFragmentAlerting) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bigFragmentHighlight.setColorFilter(highlightColor, PorterDuff.Mode.MULTIPLY);
                                bigFragmentHighlight.setVisibility(View.VISIBLE);
                            }
                        });
                        SystemClock.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bigFragmentHighlight.setVisibility(View.INVISIBLE);
                            }
                        });
                        SystemClock.sleep(250);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bigFragmentHighlight.clearColorFilter();
                            bigFragmentHighlight.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    while (isSmallFragmentAlerting) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                smallFragmentHighlight.setColorFilter(highlightColor, PorterDuff.Mode.MULTIPLY);
                                smallFragmentHighlight.setVisibility(View.VISIBLE);
                            }
                        });
                        SystemClock.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                smallFragmentHighlight.setVisibility(View.INVISIBLE);
                            }
                        });
                        SystemClock.sleep(250);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            smallFragmentHighlight.clearColorFilter();
                            smallFragmentHighlight.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        };
        thread.start();
    }

    public void setPopUpDefenceFragmentState(boolean state) {
        if (state) {
            popUpDefenseFragmentState = true;
            displayPopUpDefenceFragment();
        } else {
            hidePopUpDefenceFragment();
            popUpDefenseFragmentState = false;
        }
    }

    void displayPopUpDefenceFragment() {
        if (liveSmallFragmentID != popUpDefenceFragmentID) {
            //display fragment if another fragment is live
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mediaFragment);
            fragmentTransaction.hide(trafficAdvisorFragment);
            fragmentTransaction.hide(soundBoardFragment);
            fragmentTransaction.show(popUpDefenceFragment);
            fragmentTransaction.commit();
        }
    }

    void hidePopUpDefenceFragment() {
        if (!popUpDefenseFragmentState) //safety check, if it's not active then just exit
            return;
        //change fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(popUpDefenceFragment);
        fragmentTransaction.show(getFragment(false, liveSmallFragmentID));
        fragmentTransaction.commit();
    }

    public static void cancelAlert(boolean isBigFragment) {
        if (isBigFragment)
            isBigFragmentAlerting = false;
        else
            isSmallFragmentAlerting = false;
    }

    void setLegalMode() {
        //set variables and disable all jammers here
        legalMode = true;
        defenceService.setCellJamState(false);
        defenceService.setRadioJamState(false);
        defenceService.setLidarJamState(false);
        defenceService.setRadarJamState(false);
        //hide any visible defence fragments
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(getFragment(true, defenceFragmentID));
        fragmentTransaction.hide(getFragment(false, popUpDefenceFragmentID));
        fragmentTransaction.hide(getFragment(false, trafficAdvisorFragmentID));
        //TODO: broadcast packet to Arduino such that it can hide any illegal functions on its end
        defenceService.setLegalMode();
        trafficAdvisorService.setLegalMode();
        //also save timestamp which is used to prevent fragment swap from being initiated due to legal mode by long pressing back btn
    }

    void legalModeUnlock(int keyCode) {
        //code will be rs_up, rs_down, rs_up, rs_down, voice, hang up, enter
        if (legalModeUnlockCounter <= 1 && keyCode == rs_key_up)
            legalModeUnlockCounter = 1;
        else if (legalModeUnlockCounter == 1 && keyCode == rs_key_down)
            legalModeUnlockCounter++;
        else if (legalModeUnlockCounter == 2 && keyCode == rs_key_up)
            legalModeUnlockCounter++;
        else if (legalModeUnlockCounter == 3 && keyCode == rs_key_down)
            legalModeUnlockCounter++;
        else if (legalModeUnlockCounter == 4 && keyCode == rs_key_voice)
            legalModeUnlockCounter++;
        else if (legalModeUnlockCounter == 5 && keyCode == ls_key_call_hangup)
            legalModeUnlockCounter++;
        else if (legalModeUnlockCounter == 6 && keyCode == rs_key_enter)
            legalMode = false;
        else
            legalModeUnlockCounter = 0;
    }

    //network passthroughs
    public void networkRequest() { networkService.handleRequest(); }

    public void handleIncomingRequest(int serviceID, JSONObject data) {

    }
}