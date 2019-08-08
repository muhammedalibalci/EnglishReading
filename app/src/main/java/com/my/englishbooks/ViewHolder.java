package com.my.englishbooks;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewHolder extends RecyclerView.ViewHolder  {

    View mView;
    public static ArrayList<String> arrayList=new ArrayList<String>();


    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }


    //set details to recycler view row
    public void setDetails(Context ctx, String title, String image,String gender,String lenght){
              //Views

        TextView mGenderV = mView.findViewById(R.id.genreView);
        TextView mlenghtV = mView.findViewById(R.id.lenghtView);
        ImageView imageView =mView.findViewById(R.id.imgView);
        TextView titleV =mView.findViewById(R.id.titleView);
        TextView mImageText =mView.findViewById(R.id.imagetextView);
        ;

        //set data to views

        mImageText.setText(image);
        mGenderV.setText("Gender: "+gender);
        mlenghtV.setText("Lenght: "+lenght);
        titleV.setText(title);

        Picasso.get().load(image).into(imageView);

    }

    private ViewHolder.ClickListener mClickListener;

    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
    private ViewHolder.ItemClickListener mitemlickListener;

    //interface to send callbacks
    public interface ItemClickListener{
      void butonClick(View view, int pos);
      void checkBoxClicl(View view, int pos);
    }

    public void setOnClickListener(ViewHolder.ItemClickListener clickListener){
        mitemlickListener = clickListener;
    }


}