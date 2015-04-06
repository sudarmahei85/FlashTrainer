package hci.divinesymphony.net.flashtrainer;

import hci.divinesymphony.net.flashtrainer.backend.SampleProblemSet;
import hci.divinesymphony.net.flashtrainer.backend.Selector;
import hci.divinesymphony.net.flashtrainer.beans.ProblemSet;
import hci.divinesymphony.net.flashtrainer.beans.DisplayItem;
import hci.divinesymphony.net.flashtrainer.sync.BackgroundDownloader;
import hci.divinesymphony.net.flashtrainer.sync.Client;
import hci.divinesymphony.net.flashtrainer.sync.Downloader;
import hci.divinesymphony.net.flashtrainer.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.io.IOException;
import java.io.FileDescriptor;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Quiz extends Activity implements
        SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private ProblemSet probSet;
    private DisplayItem reward;

    private Button btn_problem;
    private ImageButton btn_problem_img;
    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;

    private MediaPlayer mediaPlayer;
    private SurfaceView vidSurface;
    private Client client;

    private View controlsView;
    private View contentView;
    private View mainUIView;

    private boolean playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);

        //locate the button controls and save for later
        this.btn_problem = ((Button)findViewById(R.id.btn_problem));
        this.btn_problem_img = ((ImageButton)findViewById(R.id.btn_problem_img));
        this.btn_0 = ((Button)findViewById(R.id.btn_0));
        this.btn_1 = ((Button)findViewById(R.id.btn_1));
        this.btn_2 = ((Button)findViewById(R.id.btn_2));
        this.btn_3 = ((Button)findViewById(R.id.btn_3));

        //create click listeners
        this.btn_problem.setOnClickListener(oclProblem);
        this.btn_0.setOnClickListener(oclResponse_0);
        this.btn_1.setOnClickListener(oclResponse_1);
        this.btn_2.setOnClickListener(oclResponse_2);
        this.btn_3.setOnClickListener(oclResponse_3);

        this.controlsView = findViewById(R.id.fullscreen_content_controls);
        this.controlsView.setVisibility(View.GONE);

        this.mainUIView = findViewById(R.id.main_ui);

        this.contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //Initialize the video playback surface
//        this.mediaPlayer = new MediaPlayer();

        this.vidSurface = (SurfaceView) findViewById(R.id.fullscreen_content);
        this.vidSurface.setVisibility(View.VISIBLE);
        this.vidSurface.getHolder().addCallback(this);

        this.mediaPlayer = new MediaPlayer();

        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);

        //Initialize client preferences
        this.client = Client.initialize(this);

        //Kick off background downloads
        BackgroundDownloader downloader = new BackgroundDownloader(this);
        downloader.go();

        //Initialize with a real problem set
        this.populate_problem();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void populate_problem() {
        InputStream is = Client.getClient().getConfigXML(this);
        Selector selector = new Selector(is);
        this.probSet = selector.getProblemSet();
        this.reward = selector.getReward();

        DisplayItem problem = probSet.getProblem();
        List<DisplayItem> responses = probSet.getResponses();

        this.btn_problem.setText(problem.getText());
        if (problem.isImage()) {
            this.btn_problem_img.setImageURI(problem.getLocalUri());
            this.btn_problem_img.setVisibility(View.VISIBLE);
            this.btn_problem.setVisibility(View.GONE);
        } else {
            this.btn_problem_img.setVisibility(View.GONE);
            this.btn_problem.setVisibility(View.VISIBLE);
        }

        //fill in responses
        this.btn_0.setText(responses.get(0).getText());
        this.btn_1.setText(responses.get(1).getText());
        this.btn_2.setText(responses.get(2).getText());
        this.btn_3.setText(responses.get(3).getText());

    }

    /**
     * OnClick handler for problem
     */
    OnClickListener oclProblem = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO this could be used to play a sound or animation on a click
        }
    };

    /**
     * OnClick handler for response selections
     */
    OnClickListener oclResponse_0 = new OnClickListener() {
        @Override
        public void onClick(View v) {
            process_response(0);
        }
    };
    OnClickListener oclResponse_1 = new OnClickListener() {
        @Override
        public void onClick(View v) {
            process_response(1);
        }
    };
    OnClickListener oclResponse_2 = new OnClickListener() {
        @Override
        public void onClick(View v) {
            process_response(2);
        }
    };
    OnClickListener oclResponse_3 = new OnClickListener() {
        @Override
        public void onClick(View v) {
            process_response(3);
        }
    };

    void process_response(int N) {
        //TODO process the click, compare result, and either delay for lockout, or present reward
        String selectedId = this.probSet.getResponses().get(N).getId();

        boolean correct = hci.divinesymphony.net.flashtrainer.backend.AnswerChecker.isCorrect(this.probSet, selectedId);

        synchronized(this) {
            if (correct && !playing) {
                this.reward();
                this.populate_problem();
            } else {
                this.punish();
            }
        }
    }

    void reward() {
        synchronized(this) {
            this.vidSurface.setVisibility(View.VISIBLE);
            this.playing = true;
        }

        String vidFile = Client.getClient().getMediaPath()+this.reward.getFile();
        Log.v(this.getClass().getName(), "attempting to play "+vidFile);
        try {
            FileInputStream is = new FileInputStream(vidFile);
            this.mediaPlayer.setDataSource(is.getFD());
            this.mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            throw new RuntimeException(e);
            //intentionally do nothing, as we can't recover from a missing video other than immediately returning to the quiz
        }
    }

    synchronized void punish() {
        //TODO punish the user with a delay and or lockout - this code may not work, needs testing
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Log.w(this.getClass().getName(), "The punishment delay failed", e);
            //intentionally do nothing here.  If there is an interruption, the
            //punishment is just less severe and no real harm done
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.mediaPlayer.setDisplay(holder);
        this.mediaPlayer.setScreenOnWhilePlaying(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //auto-generated method stub
    }

    public void onPrepared(MediaPlayer mp) {
        this.mainUIView.setVisibility(View.INVISIBLE);
        mp.start();
    }

    public void onCompletion(MediaPlayer mp) {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.reset();
        }
        this.vidSurface.setVisibility(View.INVISIBLE);
        this.mainUIView.setVisibility(View.VISIBLE);

        synchronized (this) {
            this.playing = false;
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

}
