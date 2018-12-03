package tuners.timmy.timmytuner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.widget.TextView;


public class TunerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tuner, container, false);
        
        //sm.start();
        //Double ampl= sm.getAmplitude();
        //String amp = ampl.toString();
        //String amp = "hola";
        //note.setText(amp);
    }


}

