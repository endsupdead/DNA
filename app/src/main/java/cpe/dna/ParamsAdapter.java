package cpe.dna;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anthony on 2/21/18.
 */

public class ParamsAdapter extends RecyclerView.Adapter<ParamsAdapter.ParamsViewHolder>{

    private Context context;
    private List<Params> paramsList;

    public ParamsAdapter(Context context, List<Params> paramsList) {
        this.context = context;
        this.paramsList = paramsList;
    }

    @Override
    public ParamsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.paramlist_layout,parent,false);
        ParamsViewHolder holder = new ParamsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ParamsViewHolder holder, int position) {
        Params params = paramsList.get(position);
         holder.paramsTextView.setText(params.getParams());
         holder.readingsTextView.setText(params.getReadings());
    }

    @Override
    public int getItemCount() {
        return paramsList.size();
    }

    class ParamsViewHolder extends RecyclerView.ViewHolder{

        TextView paramsTextView;
        TextView readingsTextView;

        public ParamsViewHolder(View itemView){
            super(itemView);

            paramsTextView = itemView.findViewById(R.id.paramsTextView);
            readingsTextView = itemView.findViewById(R.id.readingsTextView);
        }
    }
}
