package tuners.timmy.timmytuner;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;


public class TunerFragment extends Fragment implements View.OnClickListener {

    private String sNote;
    boolean ContinueRecording = true;
    private AudioRecord ar = null;
    public TextView note;
    private int bufferSize;
    private int SAMPLE_RATE = 4000;
    private int btn;
    private String LOG_TAG = "Tuner";
    private String nota;
    private String NotaPop;
    private static final int EDIT_NAME = 3;

    private float hsemitone = (float) Math.pow(2.0d,(float)(1d/24d));
    //buttons
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    //View afinador
    TunerView tunerView;
    //media player
    private MediaPlayer mp;

    RecordingThread recordingThread = new RecordingThread(new RecordingThread.Listener() {
        @Override
        public void onAudioDataReceived(final float pitch) {
            final String sPitch = String.format("%.2f", pitch);
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //note.setText(sPitch);
                        processPitch(pitch);
                        tunerView.setPitch(pitch);
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

    @Override
    public void onStop() {
        super.onStop();
        Log.e("ACTION", "Crida al onStop() tuner");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("ACTION", "Crida al onPause() tuner");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ACTION", "Crida al onResume() tuner");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tuner, container, false);

        tunerView = (TunerView) view.findViewById(R.id.tunerView);
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

        btn_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), Pop.class);
                intent.putExtra("btn_name", btn_1.getText().toString());
                startActivityForResult(intent,EDIT_NAME);
                btn=1;
                return false;
            }
        });
        btn_2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), Pop.class);
                intent.putExtra("btn_name", btn_2.getText().toString());
                startActivityForResult(intent,EDIT_NAME);
                btn=2;
                return false;
            }
        });
        btn_3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), Pop.class);
                intent.putExtra("btn_name", btn_3.getText().toString());
                startActivityForResult(intent,EDIT_NAME);
                btn=3;
                return false;
            }
        });
        btn_4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), Pop.class);
                intent.putExtra("btn_name", btn_4.getText().toString());
                startActivityForResult(intent,EDIT_NAME);
                btn=4;
                return false;
            }
        });
        btn_5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), Pop.class);
                intent.putExtra("btn_name", btn_5.getText().toString());
                startActivityForResult(intent,EDIT_NAME);
                btn=5;
                return false;
            }
        });
        btn_6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), Pop.class);
                intent.putExtra("btn_name", btn_6.getText().toString());
                startActivityForResult(intent,EDIT_NAME);
                btn=6;
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case EDIT_NAME:
                if(resultCode==-1 && btn==1){
                    btn_1.setText(data.getStringExtra("btn_result"));
                }
                else if(resultCode==-1 && btn==2){
                    btn_2.setText(data.getStringExtra("btn_result"));
                }
                else if(resultCode==-1 && btn==3){
                    btn_3.setText(data.getStringExtra("btn_result"));
                }
                else if(resultCode==-1 && btn==4){
                    btn_4.setText(data.getStringExtra("btn_result"));
                }
                else if(resultCode==-1 && btn==5){
                    btn_5.setText(data.getStringExtra("btn_result"));
                }
                else if(resultCode==-1 && btn==6){
                    btn_6.setText(data.getStringExtra("btn_result"));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
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
        tunerView.setSelected(noteToPitch(nota.toUpperCase()));
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


    //conversió nota a pitch
    public float noteToPitch(String note){
        String[][] notes = { {"C"}, {"Db", "C#"}, {"D"}, {"Eb", "D#"}, {"E"},
                {"F"}, {"Gb", "F#"}, {"G"}, {"Ab", "G#"}, {"A"}, {"Bb", "A#"}, {"B"} };
        char[] splitNote = note.toCharArray();
        String sym = "";
        int oct = 0;
        float pitch = 0;

        if (splitNote.length == 2) {
            sym += splitNote[0];
            oct = Character.getNumericValue(splitNote[1]);
        } else if (splitNote.length == 3) {
            sym += Character.toString(splitNote[0]);
            sym += Character.toString(splitNote[1]);
            oct = Character.getNumericValue(splitNote[2]);
        }

        //relació amb la 2a octava
        int octrelation = (oct-2)*2;
        if (oct==2){
            octrelation = 1;
        }
        // Busca la nota corresponent a l'array, la pos = num de semitons de distancia amb C2 (65,41Hz)
        for (int i = 0; i < notes.length; i++) {
            for (int j = 0; j < notes[i].length; j++) {
                if (notes[i][j].equals(sym)) {
                    float semitones = (float) Math.pow(2d, (double)i / 12d);
                    pitch = ((65.41f * semitones) * (float)octrelation);
                    return pitch;
                }
            }
        }
        return -1;
    }

    //conversió pitch a nota
    public void processPitch(float pitchInHz) {
        int octave = (int) Math.floor(Math.log(pitchInHz/65.41)/Math.log(2));
        if(octave>=1){
            pitchInHz=pitchInHz/(octave*2);
        }
        if(pitchInHz >= (65.41/hsemitone) && pitchInHz < (65.41*hsemitone)) {
            sNote = "C";
        }
        else if(pitchInHz >= (69.30/hsemitone) && pitchInHz < (69.30*hsemitone)) {
            sNote = "C#";
        }
        else if(pitchInHz >= (73.42/hsemitone) && pitchInHz < (73.42*hsemitone)) {
            sNote = "D";
        }
        else if(pitchInHz >= (77.78/hsemitone) && pitchInHz < (77.78*hsemitone)) {
            sNote = "D#";
        }
        else if(pitchInHz >= (82.41/hsemitone) && pitchInHz < (82.41*hsemitone)) {
            sNote = "E";
        }
        else if(pitchInHz >= (87.307/hsemitone) && pitchInHz < (87.307*hsemitone)) {
            sNote = "F";
        }
        else if(pitchInHz >= (92.499/hsemitone) && pitchInHz < (92.499*hsemitone)) {
            sNote = "F#";
        }
        else if(pitchInHz >= (97.999/hsemitone) && pitchInHz < (97.999*hsemitone)) {
            sNote = "G";
        }
        else if(pitchInHz >= (103.826/hsemitone) && pitchInHz < (103.826*hsemitone)) {
            sNote = "G#";
        }
        else if(pitchInHz >= (110/hsemitone) && pitchInHz < (110*hsemitone)) {
            sNote = "A";
        }
        else if(pitchInHz >= (123.47/hsemitone) && pitchInHz < (123.47*hsemitone)) {
            sNote = "B";
        }
        else {
            sNote = "";
        }

        //octava a la que pertany
        if(octave==0){
            octave = 2;
        } else {octave = octave*2;}

        if(!sNote.equals("")){
            note.setText(sNote+String.valueOf(octave));
        } else{
            note.setText("");
        }

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

