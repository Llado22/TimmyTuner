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
        TextView note = view.findViewById(R.id.note);
        /*
        FICAR AQUÍ EL START RECORDING
        SoundMeter sm = new SoundMeter();
        sm.start();
        Double ampl= sm.getAmplitude();
        String amp = ampl.toString();
        note.setText(amp);
        */
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

