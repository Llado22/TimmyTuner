package tuners.timmy.timmytuner;

import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.SoundPool;


public class MetroFragment extends Fragment implements View.OnClickListener {

    private int mBeatSoundId = -1;
    private static long interval = 500;
    private int bpm = 120;

    public TextView tempo_view;
    private SeekBar tempo_selector;
    private ImageView play_img;
    private Button minustempo;
    private Button plustempo;
    private boolean isPlaying = false;

    //audio
    private SoundPool mSoundPool;

    //HANDLER
    Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg){
            super.handleMessage(msg);
            PlayBeat play = (PlayBeat) msg.obj;
            if(msg.what == 1){ Toast.makeText(getActivity(), "Tick", Toast.LENGTH_SHORT).show();}
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metro, container, false);

        //REFERENCIES
        tempo_view = view.findViewById(R.id.tempo_view);
        tempo_selector = view.findViewById(R.id.tempo_selector);
        plustempo = view.findViewById(R.id.plustempo);
        minustempo = view.findViewById(R.id.minustempo);
        play_img = view.findViewById(R.id.play_img);
        play_img.setOnClickListener(this);
        plustempo.setOnClickListener(this);
        minustempo.setOnClickListener(this);

        //AUDIO
        //per inicialitzar soundpool segons versió
        interval = toInterval(bpm);

        //build SoundPool
        initializeSP();

        //SEEKBAR
        tempo_view.setText(String.valueOf(bpm));
        tempo_selector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progressChangedValue = progress;
                bpm = progress;
                interval = toInterval(bpm);
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

    public void initializeSP (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .build();
        } else mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }
    //si l'interval està en ms, en un compas 4/4 tenim 120 BPM, amb la longitud de 0.5 segons per beat -> 500 mspb * 60000 = 120 bpm
    public int toBpm(long interval) {
        return (int) (60000 / interval);
    }

    static public long toInterval(int bpm) {
        return (long) 60000 / bpm;
    }

    public class PlayBeat {
        Handler handler;

        public PlayBeat(Handler handler) {this.handler = handler;}

        public void updateStatus(int status){
            Message msg = handler.obtainMessage(status, this);
            handler.sendMessage(msg);
        }

        public void play(){
            new Thread(new playRunnable(this)).start();
        }
    }

    public class playRunnable implements Runnable {
        PlayBeat playBeat;

        public playRunnable(PlayBeat playBeat) {this.playBeat = playBeat;}
        //LOOP
        @Override
        public void run() {
            if(isPlaying){
                interval=toInterval(getBpm());
                handler.postDelayed(this,interval);
                if (mSoundPool != null && mBeatSoundId != -1) {
                    //playBeat.updateStatus(1); //per comprobar que envia
                    mSoundPool.play(mBeatSoundId, 1.0f /*leftVolume*/, 1.0f /*rightVolume*/, 0 /*priority*/, 0 /*loop*/, 1.0f /*rate*/);
                }
            } else {
                mSoundPool.stop(mBeatSoundId);
                handler.removeCallbacks(this);
            }
            //mSoundPool.release();
            //handler.postDelayed((Runnable) getActivity(), interval);

        }

    }

    public int getBpm(){
        int nbpm = Float.valueOf(tempo_view.getText().toString()).intValue();
        return nbpm;
    }

    /***
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
        ***/

//buttons
    @Override
    public void onClick(View view) {

        int temp = getBpm();

        switch(view.getId()){
            case R.id.plustempo:
                if(temp!=tempo_selector.getMax()){
                    temp += 1;
                    tempo_view.setText(String.valueOf(temp));
                    //modificar seekbar
                    tempo_selector.setProgress(temp);
                }
                break;
            case R.id.minustempo:
                if(temp!=30){
                    temp -= 1;
                    tempo_view.setText(String.valueOf(temp));
                    //modificar seekbar
                    tempo_selector.setProgress(temp);
                }
                break;
            case R.id.play_img:
                isPlaying = !isPlaying;
                //canviar el botó de play o pause
                if(isPlaying){
                    play_img.setImageResource(R.drawable.pause_button);
                } else {
                    play_img.setImageResource(R.drawable.play_button);
                }
                //load
                mBeatSoundId = mSoundPool.load(getActivity(), R.raw.click, 1);
                //fer l'execució
                new PlayBeat(handler).play();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mSoundPool.release();
        play_img.setImageResource(R.drawable.play_button);
        Log.e("ERROR", "Crida al onStop()");
    }
}




