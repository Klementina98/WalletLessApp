package com.example.crudgraduation.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crudgraduation.DbBitmapUtility;
import com.example.crudgraduation.Model.Card;
import com.example.crudgraduation.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private onCardListener onCardListener;
    private ArrayList<Card> list;


    //v2
    public CustomAdapter(Context context, ArrayList<Card> list, onCardListener onCardListener){
        this.context = context;
        this.list = list;
        this.onCardListener = onCardListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.scanned_card_item, parent,false);
        return new MyViewHolder(view, onCardListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.card_id_text.setText(String.valueOf(list.get(position).getCardId()));
        holder.card_name_txt.setText(String.valueOf(list.get(position).getCardName()));
        holder.card_barcode_txt.setText(String.valueOf(list.get(position).getBarCode()));
        Bitmap bitmap1 = DbBitmapUtility.getImage(list.get(position).getLocalImage());
        holder.card_image.setImageBitmap(bitmap1);

        //we need to read the id from our database and pass it to our activity
        holder.itemView.setTag(list.get(position).getCardId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView card_id_text, card_name_txt, card_barcode_txt;
        ImageView card_image;
        onCardListener onCardListener;

        public MyViewHolder(@NonNull View itemView, onCardListener onCardListener) {
            super(itemView);
            card_id_text = itemView.findViewById(R.id.card_id);
            card_name_txt = itemView.findViewById(R.id.card_name);
            card_barcode_txt = itemView.findViewById(R.id.card_barcode);
            card_image = itemView.findViewById(R.id.card_image);

            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onCardListener.onCardClick(getAdapterPosition());
        }


    }
    public interface onCardListener{
        void onCardClick(int position);
    }
}
