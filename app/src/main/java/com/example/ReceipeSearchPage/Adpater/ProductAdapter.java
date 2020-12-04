package com.example.ReceipeSearchPage.Adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ReceipeSearchPage.Helper.DatabaseHelper;
import com.example.ReceipeSearchPage.Helper.DatabaseHelper2;
import com.example.ReceipeSearchPage.Model.ProductModel;
import com.example.ReceipeSearchPage.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    ArrayList<ProductModel> items;
    Context context;
    private ArrayList<ProductModel> arraylist;


    public ProductAdapter(ArrayList<ProductModel> items, Context context) {
        this.items = items;
        this.context = context;
        this.arraylist = new ArrayList<ProductModel>();
        this.arraylist.addAll(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        Picasso.get().load(arraylist.get(position).getThumbnail()).into(holder.imageView);
        holder.Ingredients.setText(arraylist.get(position).getIngredients());
        holder.link.setText(arraylist.get(position).getHref());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(context).inflate(R.layout.full_view_page,null);

                ImageView favorite=view.findViewById(R.id.favorite);

                String string= new DatabaseHelper2(context).getAllHistory().toString();
                Log.e("data",string);
                if (string.contains(arraylist.get(position).getTitle().replace("'",""))){
                    favorite.setImageResource(R.drawable.ic_baseline_star_24);
                }

                if (string.equals(arraylist.get(position).getTitle()+","+arraylist.get(position).getThumbnail()+","+arraylist.get(position).getHref()+","+arraylist.get(position).getIngredients())){
                    favorite.setImageResource(R.drawable.ic_baseline_star_24);
                }
                new DatabaseHelper(context).addHistory(arraylist.get(position).getTitle());
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setView(view);
                ImageView imageView=view.findViewById(R.id.image);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatabaseHelper2(context).addHistory(arraylist.get(position).getTitle().replace("'","")+","+arraylist.get(position).getThumbnail()+","+arraylist.get(position).getHref()+","+arraylist.get(position).getIngredients());
                        if (string.equals(arraylist.get(position).getTitle()+","+arraylist.get(position).getThumbnail()+","+arraylist.get(position).getHref()+","+arraylist.get(position).getIngredients())){

                        }
                        favorite.setImageResource(R.drawable.ic_baseline_star_24);

                    }
                });
                TextView title=view.findViewById(R.id.title);
                 TextView Ingredients=view.findViewById(R.id.Ingredients);
                 TextView link=view.findViewById(R.id.link);
                 link.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getHref()));
                         context.startActivity(browserIntent);
                     }
                 });
                 title.setText(items.get(position).getTitle());
                Ingredients.setText(items.get(position).getIngredients());
                link.setText(items.get(position).getHref());
                Picasso.get().load(items.get(position).getThumbnail()).into(imageView);
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,Ingredients,link;
        LinearLayout parent;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            Ingredients=itemView.findViewById(R.id.Ingredients);
            link=itemView.findViewById(R.id.link);
            imageView=itemView.findViewById(R.id.image);
            parent=itemView.findViewById(R.id.parent);
        }
    }
    public void filter(String id) {

        id = id.toLowerCase(Locale.getDefault());
        items.clear();
        if (id.length() == 0) {
            items.addAll(arraylist);
        } else {
            for (ProductModel wp : arraylist) {
                if (wp.getIngredients().toLowerCase(Locale.getDefault()).contains(id)) {
                    items.add(wp);
                }
                else if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(id)){
                    items.add(wp);
                }
                else if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(id)) {
                    items.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }

}
