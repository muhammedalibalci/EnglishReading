package com.my.englishbooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteHolder extends  RecyclerView.Adapter<FavoriteHolder.MyViewHolder> {
    ArrayList<Book> mBookList;
    LayoutInflater inflater;


    public FavoriteHolder(Context context, ArrayList<Book> mBookList) {
        inflater = LayoutInflater.from(context);
        this.mBookList = mBookList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_favorite,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book selectedProduct = mBookList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,content,gender,lenght;
        CheckBox isRead;
        Button button;
        TextView imageTextView;
        ImageView ımageView;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titleView);
            content=itemView.findViewById(R.id.contentView);
            gender=itemView.findViewById(R.id.genreView);
            lenght=itemView.findViewById(R.id.lenghtView);
            isRead=itemView.findViewById(R.id.readCheck);
            button= itemView.findViewById(R.id.deleteButton);
            imageTextView = itemView.findViewById(R.id.imagetextView);
            ımageView = itemView.findViewById(R.id.imgView);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.butonClick(view,getAdapterPosition());
                }
            });
            isRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.checkBoxClicl(view,getAdapterPosition());
                }
            });
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

        public void setData(Book selectedProduct, int position) {


            this.title.setText(selectedProduct.getTitle());
            this.content.setText(selectedProduct.getContent());
            this.gender.setText(selectedProduct.getGender());
            this.lenght.setText(selectedProduct.getLenght());
            this.isRead.setChecked(Boolean.parseBoolean(selectedProduct.getIsRead()));
            String picassoUri =selectedProduct.getImageurl();
            System.out.println(picassoUri);
            Picasso.get().load(selectedProduct.getImageurl()).into(ımageView);

        }

    }

    private ViewHolder.ClickListener mClickListener;

    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void ClickButon(View view, int pos);
        void onItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
    ///////////////////////
    private ViewHolder.ItemClickListener itemClickListener;

    //interface to send callbacks
    public interface ItemClickListener{

        void ClickButon(View view, int pos);


    }

    public void itemOnClickListener(ViewHolder.ItemClickListener clickListener){
        itemClickListener = clickListener;
    }




}


