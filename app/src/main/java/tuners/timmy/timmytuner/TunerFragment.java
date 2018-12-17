package tuners.timmy.timmytuner;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class TunerFragment extends Fragment {
    public SoundPool sp;
    public SoundMeter sm;

    private Button btn_a;
    private Button btn_g;
    private Button btn_d;
    private Button btn_b;
    private Button btn_e6;
    private Button btn_e1;
    int sonido_de_rep;

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
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);

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

    public void onRecord() {
        Double amplitude = sm.getAmplitude();
        String amp = amplitude.toString();
        note.setText(amp);

    }

    public void onStopRecording() {
        sm.stop();
    }
}

