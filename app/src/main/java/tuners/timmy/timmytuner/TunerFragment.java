package tuners.timmy.timmytuner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class TunerFragment extends Fragment {

    public SoundMeter sm;

    private Button btn_record;
    private TextView note;
    private Button btn_stop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuner, container, false);
        note = view.findViewById(R.id.textView);

        btn_record = view.findViewById(R.id.button);
        btn_stop = view.findViewById(R.id.button2);

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecord();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStopRecording();
            }
        });

        sm = new SoundMeter();
        sm.start();
        return view;
    }

    public void onRecord() {
        Double amplitude = sm.getAmplitude();
        String amp = amplitude.toString();
        note.setText(amp);

    }

    public void onStopRecording() {
        sm.stop();
    }




}

