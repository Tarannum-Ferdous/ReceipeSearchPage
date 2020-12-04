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
import com.example.ReceipeSearchPage.MainActivity;
import com.example.ReceipeSearchPage.R;
import java.util.ArrayList;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {


    Context ctx;
    ArrayList<String> arrayList;

    public AddressAdapter(Context ctx, ArrayList<String> arrayList) {
        this.ctx = ctx;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, final int position) {

        holder.title.setText(arrayList.get(position).split(",")[0]);
        holder.desc.setText(arrayList.get(position) );
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatabaseHelper(ctx).deleteHistory(arrayList.get(position).split(",")[0]);
                MainActivity.addressAdapter.refreshEvents(new DatabaseHelper(ctx).getAllHistory());
                Toast.makeText(ctx, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,desc;
        LinearLayout layout_address;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            layout_address=itemView.findViewById(R.id.layout_address);
            delete=itemView.findViewById(R.id.delete);
        }
    }
    public void refreshEvents(ArrayList<String> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
