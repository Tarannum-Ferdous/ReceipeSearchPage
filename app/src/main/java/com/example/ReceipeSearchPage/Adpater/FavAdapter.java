package com.example.ReceipeSearchPage.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReceipeSearchPage.Helper.DatabaseHelper;
import com.example.ReceipeSearchPage.Helper.DatabaseHelper2;
import com.example.ReceipeSearchPage.MainActivity;
import com.example.ReceipeSearchPage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    ArrayList<String> arrayList2;
    Context context;

    public FavAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList2 = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout1,parent,false);
        return new FavAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] strings=arrayList2.get(position).split(",");
        holder.title.setText(strings[0]);
        Picasso.get().load(strings[1]).placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
        holder.Ingredients.setText(strings[3]);
        holder.link.setText(strings[2]);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatabaseHelper2(context).deleteHistory(arrayList2.get(position).replace("'",""));
                MainActivity.adapter1.refreshEvents(new DatabaseHelper2(context).getAllHistory());
                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                if (arrayList2.size()==0){
                    MainActivity.noItem.setVisibility(View.VISIBLE);
                }else {
                    MainActivity.noItem.setVisibility(View.GONE);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,Ingredients,link;
        LinearLayout parent;
        ImageView imageView;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            Ingredients=itemView.findViewById(R.id.Ingredients);
            link=itemView.findViewById(R.id.link);
            imageView=itemView.findViewById(R.id.image);
            parent=itemView.findViewById(R.id.parent);
            delete=itemView.findViewById(R.id.delete);

        }
    }
    public void refreshEvents(ArrayList<String> arrayList) {
        this.arrayList2.clear();
        this.arrayList2.addAll(arrayList);
        notifyDataSetChanged();

    }
}
