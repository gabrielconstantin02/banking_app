package com.example.banking_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.banking_app.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<String> localDataSet;
    private ArrayList<String> localDataSet2;
    private ArrayList<String> localDataSet3;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textAmount;
        private final TextView textSent;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            textAmount = (TextView) view.findViewById(R.id.tvamount);
            textSent = (TextView) view.findViewById(R.id.textSent);


        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getAmountView(){
            return textAmount;

        }
        public TextView getSentView(){
            return  textSent;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     * @param dataSet2
     */
    public RecyclerAdapter(ArrayList<String> dataSet, ArrayList<String> dataSet2, ArrayList<String> dataSet3) {
        localDataSet = dataSet;
        localDataSet2 = dataSet2;
        localDataSet3 = dataSet3;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAmountView().setText(localDataSet.get(position));
        viewHolder.getTextView().setText(localDataSet2.get(position));
        viewHolder.getSentView().setText(localDataSet3.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
