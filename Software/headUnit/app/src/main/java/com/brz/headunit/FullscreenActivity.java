package com.brz.headunit;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.brz.headunit.databinding.ActivityFullscreenBinding;
import com.brz.headunit.media.MediaService;

import java.security.Key;

public class FullscreenActivity extends AppCompatActivity {

    public boolean legalMode = false;
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

    ImageView bigFragmentHighlight;
    ImageView smallFragmentHighlight;

    private DefenseFragment defenseFragment;
    private PopUpDefenseFragment popUpDefenseFragment;
    private DiagnosticFragment diagnosticFragment;
    private MediaFragment mediaFragment;
    private NavigationFragment navigationFragment;

    private int highlightedFragmentID = 0;
    private boolean fragmentSwapState = false;
    private int liveBigFragmentID = 0;
    private int liveSmallFragmentID = 0;

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
        defenseFragment = new DefenseFragment();
        popUpDefenseFragment = new PopUpDefenseFragment();
        diagnosticFragment = new DiagnosticFragment();
        mediaFragment = new MediaFragment();
        navigationFragment = new NavigationFragment();

        fragmentTransaction.add(R.id.big_fragment, navigationFragment); //ID 0
        fragmentTransaction.add(R.id.big_fragment, diagnosticFragment); //ID 1
        fragmentTransaction.add(R.id.big_fragment, defenseFragment); //ID 2
        fragmentTransaction.add(R.id.small_fragment, mediaFragment); //ID 0
        fragmentTransaction.add(R.id.small_fragment, popUpDefenseFragment); //ID 1

        //show specific fragments
        fragmentTransaction.hide(diagnosticFragment);
        fragmentTransaction.hide(defenseFragment);
        fragmentTransaction.hide(popUpDefenseFragment);

        liveBigFragmentID = 0;
        liveSmallFragmentID = 0;

        fragmentTransaction.commit();

        //initialize keyboard listener here

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //check if key is held down and cancel press if so. This will also be a mechanism to disable illegal things
        if (event.isCanceled()) {
            if (keyCode == ls_key_source || keyCode == rs_key_voice)
                legalMode = true;
        } else {
            //if key wasn't held for too long then execute action
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
            } else if (keyCode == rs_key_enter) { //end fragment swap
                if (fragmentSwapState) {
                    bigFragmentHighlight.setVisibility(View.INVISIBLE);
                    smallFragmentHighlight.setVisibility(View.INVISIBLE);
                    fragmentSwapState = false;
                }
            } else if (keyCode == rs_key_up) { //swap fragment in current container
                if (fragmentSwapState) {
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
                        if (liveSmallFragmentID == 0) { //media fragment is live, switch to popupdefense
                            if (!legalMode) { //if legal mode is active, do nothing
                                fragmentTransaction.hide(mediaFragment);
                                fragmentTransaction.show(popUpDefenseFragment);
                                liveSmallFragmentID++;
                            }
                        } else if (liveSmallFragmentID == 1) { //popupdefense fragment live, swap back to media fragment
                            fragmentTransaction.hide(popUpDefenseFragment);
                            fragmentTransaction.show(mediaFragment);
                            liveSmallFragmentID = 0;
                        }
                    }
                    try { //surrounded by try/catch because legalMode can cause fragment swap to be abandoned
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (keyCode == rs_key_down) { //swap fragment in current container
                if (fragmentSwapState) {
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
                        if (liveSmallFragmentID == 0) { //media fragment is live, switch to popupdefense
                            if (!legalMode) { //if legal mode is active, do nothing
                                fragmentTransaction.hide(mediaFragment);
                                fragmentTransaction.show(popUpDefenseFragment);
                                liveSmallFragmentID++;
                            }
                        } else if (liveSmallFragmentID == 1) { //popupdefense fragment live, swap back to media fragment
                            fragmentTransaction.hide(popUpDefenseFragment);
                            fragmentTransaction.show(mediaFragment);
                            liveSmallFragmentID = 0;
                        }
                    }
                    try { //surrounded by try/catch because legalMode can cause fragment swap to be abandoned
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (keyCode == rs_key_right || keyCode == rs_key_left) { //change selected fragment
                if (fragmentSwapState) {
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
            } else if (keyCode == ls_key_source) //media stuff
                MediaService.toggleSource();
            else if (keyCode == ls_key_enter)
                MediaService.togglePlay();
            else if (keyCode == ls_key_right)
                MediaService.skipSong(true);
            else if (keyCode == ls_key_left)
                MediaService.skipSong(false);
            else if (keyCode == ls_key_volume_up)
                MediaService.changeVolume(true);
            else if (keyCode == ls_key_volume_down)
                MediaService.changeVolume(false);
        }
        return super.onKeyUp(keyCode, event);
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