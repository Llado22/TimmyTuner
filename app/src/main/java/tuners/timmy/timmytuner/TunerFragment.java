package tuners.timmy.timmytuner;

import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.tv.TvRecordingClient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class TunerFragment extends Fragment {

    boolean ContinueRecording = true;
    private AudioRecord ar = null;
    private Button btn_record;
    public TextView note;
    private Button btn_stop;
    private int bufferSize;
    private int SAMPLE_RATE = 16000;
    private String LOG_TAG = "Tuner";
    private RecordingThread mRecordingThread;


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

        mRecordingThread = new RecordingThread(new RecordingThread.Listener() {
            @Override
            public void onAudioDataReceived(float max) {
                final String amp = String.valueOf(max);
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

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRecordingThread.recording()) {
                    mRecordingThread.startRecording();
                } else {
                    mRecordingThread.stopRecording();
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStopRecording();
            }
        });


        Toast.makeText(getActivity(), "Estem en l'on create", Toast.LENGTH_SHORT).show();
        return view;
    }


    public void onStopRecording() {
        note.setText("STOP");
    }


}

