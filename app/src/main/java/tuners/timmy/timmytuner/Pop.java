package tuners.timmy.timmytuner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pop extends AppCompatActivity {

    private RecyclerView list;
    private HorizontalAdapter adapter;
    private List<String> items;
    private Button mas;
    private Button menos;
    private Button save;
    private TextView octava;
    private TextView selected_note;
    private String Nota_entrada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupnotes);

        Intent intent = getIntent();
        if (intent!=null){
            Nota_entrada=intent.getStringExtra("btn_name");
        }

        adapter = new HorizontalAdapter();

        items = new ArrayList<>();
        items.add(new String("C"));
        items.add(new String("C#"));
        items.add(new String("D"));
        items.add(new String("D#"));
        items.add(new String("E"));
        items.add(new String("F"));
        items.add(new String("F#"));
        items.add(new String("G"));
        items.add(new String("G#"));
        items.add(new String("A"));
        items.add(new String("A#"));
        items.add(new String("B"));

        list=findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(adapter);

        mas = findViewById(R.id.btn_mas);
        menos = findViewById(R.id.btn_menos);
        save = findViewById(R.id.btn_save);
        octava = findViewById(R.id.oct_view);
        selected_note = findViewById(R.id.selected_note_view);

        actualitza();

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oct = Integer.parseInt(octava.getText().toString());
                if (oct<4){
                    oct++;
                }
                octava.setText(String.format("%d",oct));
                selected_note.setText( method(selected_note.getText().toString()) + octava.getText().toString());
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
                selected_note.setText( method(selected_note.getText().toString()) + octava.getText().toString());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("btn_result", selected_note.getText().toString());
                setResult(RESULT_OK,data);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));
    }

    class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder> {


        @NonNull
        @Override
        public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.lista_layout, parent, false);
            return new HorizontalViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull HorizontalViewHolder holder, final int position) {
            String item = items.get(position);
            holder.txt.setText(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public HorizontalViewHolder(View itemView) {
                super(itemView);
                this.txt=itemView.findViewById(R.id.nota_view);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        String nota = items.get(pos);
                        selected_note.setText(nota + octava.getText());
                        Nota_entrada=nota;

                    }
                });

        }
    }

    public void actualitza(){
        String str=method(Nota_entrada);
        selected_note.setText( str + octava.getText().toString());

    }

    public String method (String str){
        if (str!=null && str.length()>0){
            str=str.substring(0,str.length()-1);
        }
        return str;
    }

}
