package com.brz.headunit;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.brz.headunit.databinding.ActivityFullscreenBinding;
import com.brz.headunit.services.DefenceService;
import com.brz.headunit.services.DiagnosticService;
import com.brz.headunit.services.MediaService;
import com.brz.headunit.services.NavigationService;
import com.brz.headunit.services.SafetyService;
import com.brz.headunit.services.TrafficAdvisorService;

public class FullscreenActivity extends AppCompatActivity {

    //legal mode is true to begin with such that hidden features cannot be revealed by rebooting the system
    boolean legalMode = false; //TODO: set to true during final compilation
    //this is used to count keystrokes in a code to unlock illegal features once they have been hidden
    //any time the right key is hit, the counter goes up unless the wrong key is hit in which case it goes to 0 again
    int legalModeUnlockCounter = 0;
    private View mControlsView;

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

    private NavigationFragment navigationFragment;
    private DiagnosticFragment diagnosticFragment;
    private DefenceFragment defenceFragment;
    private MediaFragment mediaFragment;
    private SoundBoardFragment soundBoardFragment;
    private PopUpDefenceFragment popUpDefenceFragment;
    private TrafficAdvisorFragment trafficAdvisorFragment;

    DefenceService defenceService = new DefenceService(this);
    DiagnosticService diagnosticService = new DiagnosticService(this);
    MediaService mediaService = new MediaService();
    NavigationService navigationService = new NavigationService();
    SafetyService safetyService = new SafetyService(this);
    TrafficAdvisorService trafficAdvisorService = new TrafficAdvisorService(this);

    ImageView bigFragmentHighlight;
    ImageView smallFragmentHighlight;

    private final int navigationFragmentID = 0;
    private final int diagnosticFragmentID = 1;
    private final int defenceFragmentID = 2;
    private final int mediaFragmentID = 0;
    private final int soundBoardFragmentID = 1;
    private final int popUpDefenceFragmentID = 2;
    private final int trafficAdvisorFragmentID = 3;

    private int highlightedFragmentID = 0;
    private boolean fragmentSwapState = false;
    public static int liveBigFragmentID = 0;
    public static int liveSmallFragmentID = 0;

    boolean popUpDefenseFragmentState = false;
    static boolean isBigFragmentAlerting = false;
    static boolean isSmallFragmentAlerting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFullscreenBinding binding;
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mControlsView = binding.fullscreenContentControls;

        hideUI();
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
        defenceFragment = new DefenceFragment();
        popUpDefenceFragment = new PopUpDefenceFragment();
        diagnosticFragment = new DiagnosticFragment();
        mediaFragment = new MediaFragment();
        navigationFragment = new NavigationFragment();
        trafficAdvisorFragment = new TrafficAdvisorFragment();
        soundBoardFragment = new SoundBoardFragment();


        fragmentTransaction.add(R.id.big_fragment, navigationFragment); //ID 0
        fragmentTransaction.add(R.id.big_fragment, diagnosticFragment); //ID 1
        fragmentTransaction.add(R.id.big_fragment, defenceFragment); //ID 2
        fragmentTransaction.add(R.id.small_fragment, mediaFragment); //ID 0
        fragmentTransaction.add(R.id.small_fragment, soundBoardFragment); //ID 1
        fragmentTransaction.add(R.id.small_fragment, popUpDefenceFragment); //ID 2
        fragmentTransaction.add(R.id.small_fragment, trafficAdvisorFragment); //ID 3

        //show navigation and media fragments
        fragmentTransaction.hide(diagnosticFragment);
        fragmentTransaction.hide(defenceFragment);
        fragmentTransaction.hide(popUpDefenceFragment);
        fragmentTransaction.hide(soundBoardFragment);
        fragmentTransaction.hide(trafficAdvisorFragment);

        liveBigFragmentID = 0;
        liveSmallFragmentID = 0;

        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //check if key is held down and cancel press if so. This will also be a mechanism to disable illegal things
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
            if (fragmentID == -1) { //undershot
                //was at navigation fragment and went backwards
                //check legalMode
                if (legalMode) { //show diagnostic fragment
                    outputFragment = diagnosticFragment;
                    liveBigFragmentID = 1;
                } else {
                    outputFragment = defenceFragment;
                    liveBigFragmentID = 2;
                }
            } else if (fragmentID == 0)
                outputFragment = navigationFragment;
            else if (fragmentID == 1)
                outputFragment = diagnosticFragment;
            else if (fragmentID == 2) {
                if (!legalMode)
                    outputFragment = defenceFragment;
                else {
                    outputFragment = navigationFragment;
                    liveBigFragmentID = 0;
                }
            } else { //overshot valid IDs
                outputFragment = navigationFragment;
                liveBigFragmentID = 0;
            }
        } else {
            if (fragmentID == -1) { //undershot
                if (legalMode) {
                    outputFragment = soundBoardFragment;
                    liveSmallFragmentID = 1;
                } else {
                    outputFragment = trafficAdvisorFragment;
                    liveSmallFragmentID = 3;
                }
            } else if (fragmentID == 0)
                outputFragment = mediaFragment;
            else if (fragmentID == 1)
                outputFragment = soundBoardFragment;
            else if (fragmentID == 2) {
                if (legalMode) {
                    outputFragment = mediaFragment;
                    liveSmallFragmentID = 0;
                } else
                    outputFragment = popUpDefenceFragment;
            } else if (fragmentID == 3) {
                if (legalMode) {
                    outputFragment = mediaFragment;
                    liveSmallFragmentID = 0;
                } else
                    outputFragment = trafficAdvisorFragment;
            } else { //overshot
                outputFragment = mediaFragment;
                liveSmallFragmentID = 0;
            }
        }
        return outputFragment;
    }

    void setFragmentSwapState(boolean state) {
        if (state) {
            //remember that fragment swap state is now active
            fragmentSwapState = true;
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
        if (liveSmallFragmentID != 1) {
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

    private void hideUI() {
        // Hide app UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        //test stuff
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }
}