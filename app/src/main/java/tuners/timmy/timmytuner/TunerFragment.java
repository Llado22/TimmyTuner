package tuners.timmy.timmytuner;

import android.media.AudioAttributes;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;


public class TunerFragment extends Fragment implements View.OnClickListener {

    private String sNote;
    boolean ContinueRecording = true;
    private AudioRecord ar = null;
    private Button btn_record;
    public TextView note;
    private int bufferSize;
    private int SAMPLE_RATE = 4000;
    private String LOG_TAG = "Tuner";
    private String nota;
    //buttons
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    //soundpool
    private int idSound;
    public SoundPool sp;
    private MediaPlayer mp;


    RecordingThread recordingThread = new RecordingThread(new RecordingThread.Listener() {
        @Override
        public void onAudioDataReceived(final float pitch) {
            final String sPitch = String.format("%.2f", pitch);
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        note.setText(sPitch);
                        processPitch(pitch);
                    }
                });
            } catch (Exception e) {
                Log.e("ERROR", "En setText");
            }
        }
    });



    @Override
    public void onStart() {
        super.onStart();
        Log.e("ACTION", "Crida al onStart() tuner");
    }

    // Fer que quan s'executa el onStop() s'apagui el micro!!!!!
    @Override
    public void onStop() {
        super.onStop();
        Log.e("ACTION", "Crida al onStop() tuner");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuner, container, false);

        note = view.findViewById(R.id.textView);
        btn_1 = view.findViewById(R.id.btn_1);
        btn_2 = view.findViewById(R.id.btn_2);
        btn_3 = view.findViewById(R.id.btn_3);
        btn_4 = view.findViewById(R.id.btn_4);
        btn_5 = view.findViewById(R.id.btn_5);
        btn_6 = view.findViewById(R.id.btn_6);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);

        return view;
    }




    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_1:
                nota = btn_1.getText().toString().toLowerCase();
                break;

            case R.id.btn_2:
                nota = btn_2.getText().toString().toLowerCase();
                break;

            case R.id.btn_3:
                nota = btn_3.getText().toString().toLowerCase();
                break;

            case R.id.btn_4:
                nota = btn_4.getText().toString().toLowerCase();
                break;

            case R.id.btn_5:
                nota = btn_5.getText().toString().toLowerCase();
                break;

            case R.id.btn_6:
                nota = btn_6.getText().toString().toLowerCase();
                break;

        }

        try {
            int id = this.getResources().getIdentifier(nota,"raw",getActivity().getPackageName());
            if(id!=0){
                mp = MediaPlayer.create(getActivity(), id);
                mp.start();
            } else{
                Toast.makeText(getActivity(), nota + ": ID=" + String.valueOf(id) + " error",Toast.LENGTH_SHORT).show();
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (Exception e) {
            Log.e("ERROR", "Error en media player");
        }

    }

    //conversió pitch a nota
    public void processPitch(float pitchInHz) {


        if(pitchInHz >= 110 && pitchInHz < 123.47) {
            sNote = "A";
        }
        else if(pitchInHz >= 123.47 && pitchInHz < 130.81) {
            sNote = "B";
        }
        else if(pitchInHz >= 130.81 && pitchInHz < 146.83) {
            sNote = "C";
        }
        else if(pitchInHz >= 146.83 && pitchInHz < 164.81) {
            sNote = "D";
        }
        else if(pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            sNote = "E";
        }
        else if(pitchInHz >= 174.61 && pitchInHz < 185) {
            sNote = "F";
        }
        else if(pitchInHz >= 185 && pitchInHz < 196) {
            sNote = "G";
        } else {
            sNote = "?";
        }
        try {
            //Toast.makeText(getActivity(), sNote ,Toast.LENGTH_SHORT).show();
            note.setText(sNote);
        } catch (Exception e) {
            Log.e("Error","Error en setText");
        }

    }

    public void rep(){
        sp.play(idSound,1,1,1,0,0);
    }

    public void startRecording() {
        if (!recordingThread.recording()) {
            recordingThread.startRecording();
        }
    }

    public void stopRecording() {
        if (recordingThread.recording()) {
            recordingThread.stopRecording();
        }
    }
}

