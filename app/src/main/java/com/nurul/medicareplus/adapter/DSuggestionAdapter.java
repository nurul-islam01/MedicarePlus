package com.nurul.medicareplus.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.pojos.DoctorSuggestions;

import java.util.ArrayList;

/**
 * This is Created by Nurul Islam Tipu on 12/6/2018
 */
public class DSuggestionAdapter extends RecyclerView.Adapter<DSuggestionAdapter.ViewHolder> {

    private static final String TAG = DSuggestionAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<DoctorSuggestions> suggestions;

    public DSuggestionAdapter(Context context, ArrayList<DoctorSuggestions> suggestions) {
        this.context = context;
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(com.nurul.medicareplus.R.layout.doctor_suggestion_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            viewHolder.numberTV.setText(String.valueOf(i+1));
            viewHolder.textTV.setText(suggestions.get(i).getSuggetion());
        }catch (Exception e){
            Log.d(TAG, "onBindViewHolder: "+e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView numberTV, textTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTV = itemView.findViewById(R.id.serialTV);
            textTV = itemView.findViewById(R.id.textTV);

        }
    }
}
