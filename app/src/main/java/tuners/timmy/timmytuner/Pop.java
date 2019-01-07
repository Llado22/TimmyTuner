package tuners.timmy.timmytuner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class Pop extends AppCompatActivity {

    private RecyclerView list;
    private HorizontalAdapter adapter;
    String[] items;
    private Button mas;
    private Button menos;
    private TextView octava;
    private TextView selected_note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupnotes);

        adapter= new HorizontalAdapter(this,new String[]{"C","C#", "D", "D#", "E","F","F#","G","G#","A","A#","B"});

        list=findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(adapter);


        mas = findViewById(R.id.btn_mas);
        menos = findViewById(R.id.btn_menos);
        octava = findViewById(R.id.oct_view);
        selected_note = findViewById(R.id.selected_note_view);

        //octava.setText("2");
        //selected_note.setText("A#" + "2");
        actualitza();

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oct = Integer.parseInt(octava.getText().toString());
                if (oct<4){
                    oct++;
                }
                octava.setText(String.format("%d",oct));
                actualitza();
            }
        });

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oct = Integer.parseInt(octava.getText().toString());
                if (oct>2){
                    oct--;
                }
                octava.setText(String.format("%d",oct));
                actualitza();
            }
        });



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

    }
    public void actualitza(){
        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            String nota =extras.getString("note");
            selected_note.setText( nota + octava.getText());
        }
    }
}
