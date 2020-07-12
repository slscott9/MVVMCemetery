package hfad.com.architectureexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
    7. Created @Entity Note class that creates a database table, DAO, repository, view model. Now we need to create the adapter for the recycler view.
 */

public class MyAdapter extends ListAdapter<Cemeteries, MyAdapter.Holder> {

    private onItemClickListener listener;

    public MyAdapter() { //The parameters define our comparison logic between our two lists
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Cemeteries> DIFF_CALLBACK = new DiffUtil.ItemCallback<Cemeteries>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cemeteries oldItem, @NonNull Cemeteries newItem) {

            return oldItem.getId() == newItem.getId(); //if oldItem's id is the same as newItem's id we know the lists are the same. Checks if the positions of the cemeteries are the same
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cemeteries oldItem, @NonNull Cemeteries newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getLocation().equals(newItem.getLocation()); //Checks if the contents of the cemeteries are the same.
        }
    };


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) { //getItem will access the list we passedto the super class
        Cemeteries currentCemetery = getItem(position);
        holder.tvName.setText(currentCemetery.getName());
        holder.tvLocation.setText(currentCemetery.getLocation());
    }

    //We delete getItemCount because listAdapter will take care of this for us


    class Holder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvLocation;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvCemeteryName);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            itemView.setOnClickListener(new View.OnClickListener() { //listens for a click event on the itemView which is the card view itself
                @Override
                public void onClick(View view) { //When a click on the itemView which is the cardview happens we get the adapter position and set the listener object to the cemetery object at that position
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){ //to avoid a crash we need to check if the listener is null. The case could be where the itemView was not clicked.
                        listener.onItemClick(getItem(position));   //NO_POSITION is just a constant for -1

                    }
                }
            });
        }
    }

    public Cemeteries getCemeteryAt(int position){ //this method allows use to get a cemetery to the outside
        return getItem(position);
    }

    public interface onItemClickListener{ //interface for when user clicks on a cemetery to update it. We are still going to use add cemetery activity
        void onItemClick(Cemeteries cemeteries);
    }

    public void setOnItemClickListener(onItemClickListener listener){ //make sure the type for this parameter has our package name because this is the interface for our own adpater listener
        this.listener = listener;
    }
}
