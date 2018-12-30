package tuners.timmy.timmytuner;

import android.media.AudioRecord;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.SoundPool;

import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.AudioDispatcher;

public class TunerFragment extends Fragment {

    public SoundPool sp;
    boolean ContinueRecording = true;
    private AudioRecord ar = null;
    private Button btn_record;
    public TextView note;
    private Button btn_stop;
    private int bufferSize;
    private int SAMPLE_RATE = 16000;
    private String LOG_TAG = "Tuner";

    RecordingThread recordingThread = new RecordingThread(new RecordingThread.Listener() {
        @Override
        public void onAudioDataReceived(float max) {
            final String amp = String.format("%.2f", max);
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        note.setText(amp);
                    }
                });
            } catch (Exception e) {
                Log.e("ERROR", "En setText");
            }
        }
    });

    private Button btn_a;
    private Button btn_g;
    private Button btn_d;
    private Button btn_b;
    private Button btn_e6;
    private Button btn_e1;
    int sonido_de_rep;

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ERROR", "Crida al onStart()");
    }



    // Fer que quan s'executa el onStop() s'apagui el micro!!!!!
    @Override
    public void onStop() {
        super.onStop();
        Log.e("ERROR", "Crida al onStop()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuner, container, false);

        note = view.findViewById(R.id.textView);
        btn_record = view.findViewById(R.id.button);
        btn_stop = view.findViewById(R.id.button2);
        btn_a = view.findViewById(R.id.btn_a);
        btn_g = view.findViewById(R.id.btn_g);
        btn_d = view.findViewById(R.id.btn_d);
        btn_b = view.findViewById(R.id.btn_b);
        btn_e6 = view.findViewById(R.id.btn_e6);
        btn_e1 = view.findViewById(R.id.btn_e1);

        btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccio_a();
                rep();
            }
        });

        btn_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccio_g();
                rep();
            }
        });

        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccio_d();
                rep();
            }
        });

        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccio_b();
                rep();
            }
        });

        btn_e6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccio_e6();
                rep();
            }
        });

        btn_e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccio_e1();
                rep();
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);

        Toast.makeText(getActivity(), "Estem en l'on create", Toast.LENGTH_SHORT).show();
        return view;
    }


    public void seleccio_a(){
        String nota = btn_a.getText().toString();
        if (nota.equals("A")) {
            sonido_de_rep = sp.load(getActivity(), R.raw.la, 1);
        }
    }
    public void seleccio_g(){
        String nota = btn_g.getText().toString();
        if (nota.equals("G")){
            sonido_de_rep=sp.load(getActivity(),R.raw.sol,1);
        }
    }
    public void seleccio_d(){
        String nota = btn_d.getText().toString();
        if (nota.equals("D")){
            sonido_de_rep=sp.load(getActivity(),R.raw.re,1);
        }
    }
    public void seleccio_b(){
        String nota = btn_b.getText().toString();
        if (nota.equals("B")) {
            sonido_de_rep = sp.load(getActivity(), R.raw.si, 1);
        }
    }
    public void seleccio_e6(){
        String nota = btn_e6.getText().toString();
        if (nota.equals("E")){
            sonido_de_rep=sp.load(getActivity(),R.raw.mi,1);
        }
    }
    public void seleccio_e1(){
        String nota = btn_e1.getText().toString();
        if (nota.equals("E")){
            sonido_de_rep=sp.load(getActivity(),R.raw.la,1);
        }
    }

    public void rep(){
        sp.play(sonido_de_rep,1,1,1,0,0);
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

