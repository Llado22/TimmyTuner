package tuners.timmy.timmytuner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>{

    String[] items;
    private OnClickListener listener;
    private Context mContext;

    public HorizontalAdapter(Context context, String[] items) {
        this.items = items;
        mContext=context;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lista_layout, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, final int position) {
        holder.txt.setText(items[position]);

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext,Pop.class);
                intent.putExtra("note",items[position]);
                mContext.startActivity(intent);
                Toast.makeText(mContext, items[position],Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public HorizontalViewHolder(View itemView) {
            super(itemView);
            txt=itemView.findViewById(R.id.nota_view);
        }
    }
}
