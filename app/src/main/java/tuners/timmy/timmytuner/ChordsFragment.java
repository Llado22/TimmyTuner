package tuners.timmy.timmytuner;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;
import java.util.List;


public class ChordsFragment extends Fragment {

    private List<Chord> notes;
    private List<Chord> variations;
    private RecyclerView recyclerView_note;
    private RecyclerView recyclerView_variation;
    private MAdapter adapter;
    private MAdapter2 adapter2;
    private String chord = "C";
    private String add = "Maj";
    private ImageView imageView_chord;
    private String name = "cmaj";
    private int selectedPosition=-1;
    private int selectedPosition2=-1;

    // new since Glide v4
    @GlideModule
    public final class MyAppGlideModule extends AppGlideModule {
        // leave empty for now
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chords, container, false);

        notes = new ArrayList<>();
        notes.add(new Chord("Ab"));
        notes.add(new Chord("A"));
        notes.add(new Chord("A#"));
        notes.add(new Chord("Bb"));
        notes.add(new Chord("B"));
        notes.add(new Chord("C"));
        notes.add(new Chord("C#"));
        notes.add(new Chord("Db"));
        notes.add(new Chord("D"));
        notes.add(new Chord("D#"));
        notes.add(new Chord("Eb"));
        notes.add(new Chord("E"));
        notes.add(new Chord("F"));
        notes.add(new Chord("F#"));
        notes.add(new Chord("Gb"));
        notes.add(new Chord("G"));
        notes.add(new Chord("G#"));

        variations = new ArrayList<>();
        variations.add(new Chord("Maj"));
        variations.add(new Chord("Min"));
        variations.add(new Chord("7"));
        variations.add(new Chord("9"));
        variations.add(new Chord("13"));

        recyclerView_note = view.findViewById(R.id.recyclerView_note);
        recyclerView_variation = view.findViewById(R.id.recyclerView_variation);
        imageView_chord = view.findViewById(R.id.imageView_chord);

        recyclerView_note.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_variation.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MAdapter();
        adapter2 = new MAdapter2();
        recyclerView_note.setAdapter(adapter);
        recyclerView_variation.setAdapter(adapter2);

        Glide.with(getActivity())
                .load("file:///android_asset/cmaj.gif")
                .into(imageView_chord);

        return view;
    }

    public void printImage(String chord, String add) {

        switch(chord) {
            case "C":
                if (add.equals("Maj")) {
                    name = "cmaj";
                }
                break;
            case "C#":
                if (add.equals("Maj")) {
                    name = "csosmaj";
                }
                break;
            case "D":
                if (add.equals("Maj")) {
                    name = "dmaj";
                }
                break;
            case "G":
                if (add.equals("Maj")) {
                    name = "gmaj";
                }
                break;
        }

        Glide.with(getActivity())
                .load("file:///android_asset/" + name + ".gif")
                .into(imageView_chord);
    }

    public void OnNoteClick(int position) {
        Chord item = notes.get(position);
        chord = item.getText();
        printImage(chord, add);
        selectedPosition=position;
        adapter.notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            this.text = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    OnNoteClick(pos);
                }
            });
        }
    }

    public void OnVariationClick(int position) {
        Chord item = variations.get(position);
        add = item.getText();
        printImage(chord, add);
        selectedPosition2=position;
        adapter2.notifyDataSetChanged();
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView text;

        public ViewHolder2(View itemView) {
            super(itemView);
            this.text = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    OnVariationClick(pos);
                }
            });
        }
    }


    class MAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(
                    R.layout.activity_chord_view, parent, false
            );
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            Chord note = notes.get(position);
            holder.text.setText(note.getText());
            if (selectedPosition==position){
                holder.itemView.setBackgroundColor(Color.parseColor("#FF555555"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FF7E7E7E"));
            }
        }

        @Override
        public int getItemCount() { return notes.size();}

    }


    class MAdapter2 extends RecyclerView.Adapter<ViewHolder2> {

        @NonNull
        @Override
        public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(
                    R.layout.activity_chord_view, parent, false
            );
            return new ViewHolder2(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
            Chord variation = variations.get(position);
            holder.text.setText(variation.getText());
            if (selectedPosition2==position){
                holder.itemView.setBackgroundColor(Color.parseColor("#FF555555"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FF7E7E7E"));
            }
        }

        @Override
        public int getItemCount() { return variations.size();}

    }


}
