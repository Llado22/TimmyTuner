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


public class TunerFragment extends Fragment {

    boolean ContinueRecording = true;
    private AudioRecord ar = null;
    private Button btn_record;
    public TextView note;
    private Button btn_stop;
    private int bufferSize;
    private int SAMPLE_RATE = 16000;
    private String LOG_TAG = "Tuner";
    private int max;
    private RecordingThread mRecordingThread;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuner, container, false);

        note = view.findViewById(R.id.textView);
        btn_record = view.findViewById(R.id.button);
        btn_stop = view.findViewById(R.id.button2);

        mRecordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(short[] data) {
                showAmplitude(data);
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

        return view;
    }

    public void showAmplitude(short[] data) {
        max = 0;
        for (short s : data)
        {
            if (Math.abs(s) > max)
            {
                max = Math.abs(s);
            }
        }
        String amp = String.valueOf(max);
        try {
            note.setText(amp);
        } catch (Exception e) {
            Log.e("ERROR", "En setText");
        }
    }

    public void onRecord() {
        /*ar.startRecording();
        short[] audioBuffer = new short[bufferSize / 2];
        long shortsRead = 0;
        while (ContinueRecording) {

            int numberOfShort = ar.read(audioBuffer, 0, audioBuffer.length);
            shortsRead += numberOfShort;

            // Do something with the audioBuffer
            int max = 0;
            for (short s : audioBuffer) {
                if (Math.abs(s) > max)
                {
                    max = Math.abs(s);
                }
            }
            String amp = String.valueOf(max);
            note.setText(amp);
        }
        */

    }

    public void onStopRecording() {
        note.setText("STOP");
    }


}

