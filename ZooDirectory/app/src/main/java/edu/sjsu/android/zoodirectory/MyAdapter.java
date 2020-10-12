package edu.sjsu.android.zoodirectory;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private List<Animal> animals;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView animalImage;
        public TextView animalName;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            animalImage = (ImageView) v.findViewById(R.id.animalIcon);
            animalName = (TextView) v.findViewById(R.id.animalIconName);
        }
    }

    public void add(int position, Animal animal) {
        animals.add(position, animal);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        animals.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context mContext, List<Animal> animals) {
        this.mContext = mContext;
        this.animals = animals;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Animal animal = animals.get(position);
        final int imageResource = animal.getImageResource();
        final String name = animal.getName();

        holder.animalImage.setImageResource(imageResource);
        holder.animalName.setText(name);

        holder.animalName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, AnimalDetailActivity.class);
                myIntent.putExtra("animalName", name);
                myIntent.putExtra("animalImageResource", imageResource);
                myIntent.putExtra("animalDescription", animal.getDescription());
                if (position == animals.size() - 1) {
                    LastAnimalDialogFragment dialogFragment = new LastAnimalDialogFragment(mContext, myIntent);
                    dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "question");
                } else {
                    mContext.startActivity(myIntent);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return animals.size();
    }
}