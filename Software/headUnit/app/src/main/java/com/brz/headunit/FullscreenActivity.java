package com.brz.headunit;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
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

    boolean legalMode = false;
    //this is used to count keystrokes in a code to unlock illegal features once they have been hidden
    //any time the right key is hit, the counter goes up unless the wrong key is hit in which case it goes to 0 again
    int legalModeUnlockCounter = 0;
    private View mControlsView;

    DefenceService defenceService = new DefenceService();
    DiagnosticService diagnosticService = new DiagnosticService();
    MediaService mediaService = new MediaService();
    NavigationService navigationService = new NavigationService();
    SafetyService safetyService = new SafetyService();
    TrafficAdvisorService trafficAdvisorService = new TrafficAdvisorService();

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

    //TODO: investigate implications of this
    static ImageView bigFragmentHighlight;
    static ImageView smallFragmentHighlight;

    private static DefenceFragment defenseFragment;
    private static PopUpDefenseFragment popUpDefenseFragment;
    private static DiagnosticFragment diagnosticFragment;
    private static MediaFragment mediaFragment;
    private static NavigationFragment navigationFragment;
    private static TrafficAdvisorFragment lightFragment;

    private int highlightedFragmentID = 0;
    private boolean fragmentSwapState = false;
    public static int liveBigFragmentID = 0;
    public static int liveSmallFragmentID = 0;

    boolean hornState = false;
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

        hide();
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
        defenseFragment = new DefenceFragment();
        popUpDefenseFragment = new PopUpDefenseFragment();
        diagnosticFragment = new DiagnosticFragment();
        mediaFragment = new MediaFragment();
        navigationFragment = new NavigationFragment();
        lightFragment = new TrafficAdvisorFragment();

        fragmentTransaction.add(R.id.big_fragment, navigationFragment); //ID 0
        fragmentTransaction.add(R.id.big_fragment, diagnosticFragment); //ID 1
        fragmentTransaction.add(R.id.big_fragment, defenseFragment); //ID 2
        fragmentTransaction.add(R.id.small_fragment, mediaFragment); //ID 0
        fragmentTransaction.add(R.id.small_fragment, popUpDefenseFragment); //ID 1
        fragmentTransaction.add(R.id.small_fragment, lightFragment); //ID 2

        //show navigation and media fragments
        fragmentTransaction.hide(diagnosticFragment);
        fragmentTransaction.hide(navigationFragment); //should be defence fragment
        fragmentTransaction.hide(popUpDefenseFragment);
        fragmentTransaction.hide(mediaFragment); //for debug to show lightFragment, should be lightfragment

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
                //remember that fragment swap state is now active
                fragmentSwapState = true;
                if (highlightedFragmentID == 0) {
                    //0 is big fragment and 1 is small fragment
                    //this is used just as an extra feature where the head unit remembers which fragment was highlighted prior
                    bigFragmentHighlight.setVisibility(View.VISIBLE);
                } else {
                    smallFragmentHighlight.setVisibility(View.VISIBLE);
                }
            } else { //exit fragment swap
                bigFragmentHighlight.setVisibility(View.INVISIBLE);
                smallFragmentHighlight.setVisibility(View.INVISIBLE);
                fragmentSwapState = false;
            }
        } else if (keyCode == rs_key_enter) {
            if (fragmentSwapState) { //end fragment swap
                bigFragmentHighlight.setVisibility(View.INVISIBLE);
                smallFragmentHighlight.setVisibility(View.INVISIBLE);
                fragmentSwapState = false;
            } else if (liveBigFragmentID == 2 && !legalMode) { //toggle jammer hail mary
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
            else if (liveBigFragmentID == 2 && !legalMode) //if not swapping fragments, then act as button but check legalMode
                if (defenceService.isCellJamState())
                    defenceService.setCellJamState(false);
                else
                    defenceService.setCellJamState(true);
        } else if (keyCode == rs_key_down) {
            if (fragmentSwapState) //swap fragment in current container
                fragmentChange(false);
            else if (liveBigFragmentID == 2 && !legalMode)
                if (defenceService.isLidarJamState())
                    defenceService.setLidarJamState(false);
                else
                    defenceService.setLidarJamState(true);
        } else if (keyCode == rs_key_right) {
            if (fragmentSwapState) //change selected fragment
                fragmentSelectionChange();
            else if (liveBigFragmentID == 2 && !legalMode)
                if (defenceService.isRadioJamState())
                    defenceService.setRadioJamState(false);
                else
                    defenceService.setRadioJamState(true);
        } else if (keyCode == rs_key_left) {
            if (fragmentSwapState) //same as rs_key_right
                fragmentSelectionChange();
            else if (liveBigFragmentID == 2 && !legalMode)
                if (defenceService.isRadarJamState())
                    defenceService.setRadarJamState(false);
                else
                    defenceService.setRadarJamState(true);
        } else if (keyCode == rs_key_voice) { //panic button
            if (liveBigFragmentID == 2) { //defence fragment
                defenceService.setCellJamState(false);
                defenceService.setRadioJamState(false);
                defenceService.setLidarJamState(false);
                defenceService.setRadarJamState(false);
            }
        } else if (keyCode == ls_key_source) {
            if (liveSmallFragmentID <= 1) //media stuff
                mediaService.toggleSource();
            else if (liveSmallFragmentID == 2) //panic
                setLegalMode();

        } else if (keyCode == ls_key_enter) {
            if (liveSmallFragmentID <= 1)
                mediaService.togglePlay();
            else if (liveSmallFragmentID == 2 && !legalMode) { //handle light keystroke here
                //police horn
                hornState = false;
                trafficAdvisorService.setHornState(false);
            }
        }
        else if (keyCode == ls_key_right) {
            if (liveSmallFragmentID <= 1)
                mediaService.skipSong(true);
            else if (liveSmallFragmentID == 2 && !legalMode) //handle light keystroke
                trafficAdvisorService.toggleLightCycle(0); //fast police cycle
        }
        else if (keyCode == ls_key_left)
            if (liveSmallFragmentID <= 1)
                mediaService.skipSong(false);
            else if (liveSmallFragmentID == 2 && !legalMode) //slow police cycle
                trafficAdvisorService.toggleLightCycle(1);
        else if (keyCode == ls_key_volume_up)
            mediaService.changeVolume(true);
        else if (keyCode == ls_key_volume_down)
            mediaService.changeVolume(false);

        //check for legalMode unlock code
        //do this at end to prevent any the final keystroke (Enter) from enabling all jammers
        legalModeUnlock(keyCode);

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (liveSmallFragmentID == 2 && keyCode == ls_key_enter) {
            hornState = true;
            trafficAdvisorService.setHornState(true);
        } else if (liveSmallFragmentID == 2 && keyCode == ls_key_left)
            trafficAdvisorService.toggleLightCycle(3);
        else if (liveSmallFragmentID == 2 && keyCode == ls_key_right)
            trafficAdvisorService.toggleLightCycle(1);

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == rs_key_back) //TODO: check if this is valid key choice (doesn't trigger any illegal things)
            setLegalMode();
        return super.onKeyLongPress(keyCode, event);
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

    public static void fragmentAlert(boolean isBigFragment, int alertLevel) {
        //ensure multiple alerts can't be called at once
        if ((isBigFragmentAlerting && isBigFragment) || (isSmallFragmentAlerting && !isBigFragment))
            return;

        //TODO: probably also want to nullify fragment swap state if active

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
                        bigFragmentHighlight.setColorFilter(highlightColor);
                        bigFragmentHighlight.setVisibility(View.VISIBLE);
                        SystemClock.sleep(250);
                        bigFragmentHighlight.setVisibility(View.INVISIBLE);
                        SystemClock.sleep(250);
                    }
                    bigFragmentHighlight.setColorFilter(Color.BLACK);
                    bigFragmentHighlight.setVisibility(View.INVISIBLE);
                } else {
                    while (isSmallFragmentAlerting) {
                        smallFragmentHighlight.setColorFilter(highlightColor);
                        smallFragmentHighlight.setVisibility(View.VISIBLE);
                        SystemClock.sleep(250);
                        smallFragmentHighlight.setVisibility(View.INVISIBLE);
                        SystemClock.sleep(250);
                    }
                    smallFragmentHighlight.setColorFilter(Color.BLACK);
                    smallFragmentHighlight.setVisibility(View.INVISIBLE);
                }
            }
        };
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
            //display fragment if media fragment is live
            //TODO: change fragment from static context???
            //TODO: solution --> created variable behind is/set function pair for this
            //TODO: ineffective, figure something out
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mediaFragment);
            fragmentTransaction.hide(lightFragment);
            fragmentTransaction.show(popUpDefenseFragment);
            fragmentTransaction.commit();
        }
    }

    void hidePopUpDefenceFragment() {
        if (!popUpDefenseFragmentState) //safety check, if it's not active then just exit
            return;
        //change fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(popUpDefenseFragment);
        if (liveSmallFragmentID == 2 && !legalMode)
            fragmentTransaction.show(lightFragment);
        else
            fragmentTransaction.show(mediaFragment);
        fragmentTransaction.commit();
    }

    public static void cancelAlert(boolean isBigFragment) {
        if (isBigFragment)
            isBigFragmentAlerting = false;
        else
            isSmallFragmentAlerting = false;
    }

    void fragmentChange(boolean direction) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (direction) { //up
            if (highlightedFragmentID == 0) { //manage big fragments
                if (liveBigFragmentID == 0) { //navigation fragment is live, switch to diagnostic
                    liveBigFragmentID++;
                    fragmentTransaction.hide(navigationFragment);
                    fragmentTransaction.show(diagnosticFragment);
                } else if (liveBigFragmentID == 1) { //diagnostic fragment live, show defense fragment
                    fragmentTransaction.hide(diagnosticFragment);
                    //check if illegal features enabled
                    if (legalMode) { //if legal mode is active, swap back to navigation fragment
                        fragmentTransaction.show(navigationFragment);
                        liveBigFragmentID = 0;
                    } else { //if legal mode is inactive, move to defense fragment
                        fragmentTransaction.show(defenseFragment);
                        liveBigFragmentID++;
                    }
                } else {
                    fragmentTransaction.hide(defenseFragment);
                    fragmentTransaction.show(navigationFragment);
                    liveBigFragmentID = 0;
                }
            } else { //manage small fragments
                if (liveSmallFragmentID == 0) { //media fragment is live, switch to popupdefence
                    //check if illegal features enabled
                    if (legalMode) { //if legal mode is active, do nothing
                        return;
                    } else { //if legal mode is inactive, move to popupdefence fragment
                        fragmentTransaction.hide(mediaFragment);
                        fragmentTransaction.show(popUpDefenseFragment);
                        liveSmallFragmentID++;
                    }
                } else if (liveSmallFragmentID == 1) { //popupdefence fragment live, show light fragment
                    fragmentTransaction.hide(popUpDefenseFragment);
                    //check if illegal features enabled
                    if (legalMode) { //if legal mode is active, swap back to media fragment
                        fragmentTransaction.show(mediaFragment);
                        liveSmallFragmentID = 0;
                    } else { //if legal mode is inactive, move to defense fragment
                        fragmentTransaction.show(lightFragment);
                        liveSmallFragmentID++;
                    }
                } else {
                    fragmentTransaction.hide(lightFragment);
                    fragmentTransaction.show(mediaFragment);
                    liveSmallFragmentID = 0;
                }
            }
        } else {
            if (highlightedFragmentID == 0) { //manage big fragments
                if (liveBigFragmentID == 0) { //navigation fragment is live, switch to defense is allowed or diagnostic if not
                    fragmentTransaction.hide(navigationFragment);
                    if (legalMode) { //if legal mode is active, swap to diagnostic fragment
                        fragmentTransaction.show(diagnosticFragment);
                        liveBigFragmentID = 1;
                    } else { //if legal mode is inactive, move to defense fragment
                        fragmentTransaction.show(defenseFragment);
                        liveBigFragmentID = 2;
                    }
                } else if (liveBigFragmentID == 1) { //diagnostic fragment live, show navigation fragment
                    fragmentTransaction.hide(diagnosticFragment);
                    fragmentTransaction.show(navigationFragment);
                    liveBigFragmentID--;
                } else {
                    fragmentTransaction.hide(defenseFragment);
                    fragmentTransaction.show(diagnosticFragment);
                    liveBigFragmentID = 1;
                }
            } else { //manage small fragments
                if (liveSmallFragmentID == 0) { //media fragment is live, switch to light
                    //check if illegal features enabled
                    if (legalMode) { //if legal mode is active, do nothing
                        return;
                    } else { //if legal mode is inactive, move to light fragment
                        fragmentTransaction.hide(mediaFragment);
                        fragmentTransaction.show(lightFragment);
                        liveSmallFragmentID = 2;
                    }
                } else if (liveSmallFragmentID == 1) { //popupdefence fragment live, show media fragment
                    fragmentTransaction.hide(popUpDefenseFragment);
                    fragmentTransaction.show(mediaFragment);
                    liveSmallFragmentID--;
                } else { //light fragment live, show popupdefence
                    fragmentTransaction.hide(lightFragment);
                    //check if illegal features enabled
                    if (legalMode) { //if legal mode is active, swap back to media fragment
                        fragmentTransaction.show(mediaFragment);
                        liveSmallFragmentID = 0;
                    } else { //if legal mode is inactive, move to popupdefense fragment
                        fragmentTransaction.show(popUpDefenseFragment);
                        liveSmallFragmentID--;
                    }
                }
            }
        }
        fragmentTransaction.commit();
    }


    void setLegalMode() {
        //set variables and disable all jammers here
        legalMode = true;
        defenceService.setCellJamState(false);
        defenceService.setRadioJamState(false);
        defenceService.setLidarJamState(false);
        defenceService.setRadarJamState(false);
        //hide any visible defence fragments
        if (liveBigFragmentID == 2) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(defenseFragment);
            fragmentTransaction.show(navigationFragment);
            fragmentTransaction.commit();
            liveBigFragmentID = 0;
        }
        if (liveSmallFragmentID == 1) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(popUpDefenseFragment);
            fragmentTransaction.show(mediaFragment);
            fragmentTransaction.commit();
            liveSmallFragmentID = 0;
        }
        //TODO: broadcast packet to Arduino such that it can hide any illegal functions on its end
        defenceService.setLegalMode();
        trafficAdvisorService.setLegalMode();
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
        else if (legalModeUnlockCounter == 5 && keyCode == rs_key_enter)
            legalMode = false;
        else
            legalModeUnlockCounter = 0;
    }

    private void hide() {
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