package com.example.crudgraduation.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.crudgraduation.Activity.ScanActivity;
import com.example.crudgraduation.Model.Card;
import com.example.crudgraduation.R;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;
    private List<Card> list;

    public MainAdapter(ArrayList<Card> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.cardName.setText(list.get(position).getCardName());
        Glide.with(holder.cardName.getContext())
                .load(list.get(position).getCardImage())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.cardImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent
                        intent = new Intent(view.getContext(), ScanActivity.class);
                intent.putExtra("cardName",list.get(position).getCardName());
                intent.putExtra("cardImage",list.get(position).getCardImage());
                intent.putExtra("locations",list.get(position).getTest());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void FilteredList(ArrayList<Card> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView cardImage;
        TextView cardName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
            cardName = (TextView) itemView.findViewById(R.id.card_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(itemClickListener!=null){
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

}
