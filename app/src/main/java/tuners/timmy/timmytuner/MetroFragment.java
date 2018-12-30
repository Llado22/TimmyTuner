package tuners.timmy.timmytuner;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.SoundPool;


public class MetroFragment extends Fragment implements View.OnClickListener {

    //audio
    private SoundPool mSoundPool;
    private Handler handler;
    private int mBeatSoundId = -1;
    private Runnable mBeatRunnable;
    private long interval = 500;
    private int bpm;

    public TextView tempo_view;
    private SeekBar tempo_selector;
    private Button play_btn;
    private Button minustempo;
    private Button plustempo;
    private boolean isPlaying;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metro, container, false);

        tempo_view = view.findViewById(R.id.tempo_view);
        tempo_selector = view.findViewById(R.id.tempo_selector);
        play_btn = view.findViewById(R.id.play_btn);
        plustempo = view.findViewById(R.id.plustempo);
        minustempo = view.findViewById(R.id.minustempo);
        play_btn.setOnClickListener(this);
        plustempo.setOnClickListener(this);
        minustempo.setOnClickListener(this);

        // para inicializar el soundpool según la versión de sdk
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .build();
        } else mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        //cárrega del beat
        mBeatSoundId = mSoundPool.load(getActivity(), R.raw.beat, 1);
        bpm = toBpm(interval);

        //inici handler
        handler = new Handler();

        //seek bar
        tempo_view.setText("120");
        tempo_selector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progressChangedValue = progress;
                String sprogressChangedValue = String.valueOf(progressChangedValue);
                tempo_view.setText(sprogressChangedValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getActivity(), "Seek bar progress is :" + progressChangedValue,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //si l'interval està en ms, en un compas 4/4 tenim 120 BPM, amb la longitud de 0.5 segons per beat -> 500 mspb * 60000 = 120 bpm
    private int toBpm(long interval) {
        return (int) (60000 / interval);
    }

    private static long toInterval(int bpm) {
        return (long) 60000 / bpm;
    }

    public void setBeatRunnable(Runnable beatRunnable) {
        mBeatRunnable = beatRunnable;
    }

    class PlayBeat implements Runnable {
        @Override
        public void run() {

            //handler.postDelayed((Runnable) getActivity(), interval);
            if (mSoundPool != null && mBeatSoundId != -1) {
                mSoundPool.play(mBeatSoundId, 1.0f /*leftVolume*/, 1.0f /*rightVolume*/, 0 /*priority*/, 0 /*loop*/, 1.0f /*rate*/);
            }
        }
    }
    /*
        @Override
        public void run() {
            if (isPlaying) {
                handler.postDelayed((Runnable) getActivity(), interval);

                if (emphasisIndex >= emphasisList.size())
                    emphasisIndex = 0;
                boolean isEmphasis = emphasisList.get(emphasisIndex);
                if (listener != null)
                    listener.onTick(isEmphasis, emphasisIndex);
                emphasisIndex++;

                if (mBeatSoundId != -1)
                    mSoundPool.play(mBeatSoundId, 1, 1, 0, 0, 1);
            }
        }
        */
//buttons
    @Override
    public void onClick(View view) {
        String tempo = tempo_view.getText().toString();
        Float ftemp = Float.valueOf(tempo);
        int temp = ftemp.intValue();


        switch(view.getId()){
            case R.id.plustempo:
                if(temp!=tempo_selector.getMax()){
                    temp += 1;
                    tempo_view.setText(String.valueOf(temp));
                }
                break;
            case R.id.minustempo:
                if(temp!=0){
                    temp -= 1;
                    tempo_view.setText(String.valueOf(temp));
                }
                break;
            case R.id.play_btn:
                //audio
                PlayBeat playBeat = new PlayBeat();
                setBeatRunnable(playBeat);
                playBeat.run();
                break;
        }
    }
}



