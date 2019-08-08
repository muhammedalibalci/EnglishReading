package com.my.englishbooks;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    Book book;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("My Favorites");
        recyclerView=findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        book=new Book();
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FavoriteHolder holder = new FavoriteHolder(this,getData());
        recyclerView.setAdapter(holder);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("Favoritbbook",MODE_PRIVATE,null);
        holder.itemOnClickListener(new ViewHolder.ItemClickListener() {

            @Override
            public void butonClick(View view, int pos) {
                try{
                    builder1.setMessage("Are you sure you want to delete this item?");
                    builder1.setTitle("Warning!");
                    builder1.setCancelable(true);
                    book= getData().get(pos);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    sqLiteDatabase.delete("books","title=?",new String[]{book.title});

                                    sqLiteDatabase.close();
                                    finish();
                                    startActivity(getIntent());


                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }catch(Exception e){

                }


            }

            @Override
            public void checkBoxClicl(View view, int pos) {
                CheckBox checkBox = view.findViewById(R.id.readCheck);
                System.out.println(checkBox.isChecked());
                boolean value = checkBox.isChecked();
                book= getData().get(pos);
                System.out.println(book.title);


                if(value==true){
                    book.IsRead="true";
                    ContentValues newValues = new ContentValues();
                    newValues.put("IsRead", book.IsRead);

                    sqLiteDatabase.update("books", newValues, "title=?", new String[]{book.title});
                }
                else {
                    book.IsRead="false";
                    ContentValues newValues = new ContentValues();
                    newValues.put("IsRead", book.IsRead);

                    sqLiteDatabase.update("books", newValues, "title=?", new String[]{book.title});
                }
            }
        });

        holder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView mContentView = view.findViewById(R.id.contentView);
                TextView mtitleView= view.findViewById(R.id.titleView);
                TextView mgenderView= view.findViewById(R.id.genreView);
                TextView lenghtView= view.findViewById(R.id.lenghtView);
                //ImageView imageView= view.findViewById(R.id.imgView);

                //get data from views
                String mContent = mContentView.getText().toString();
                String mTitle = mtitleView.getText().toString();
                String mGender = mgenderView.getText().toString();
                String mLenght = lenghtView.getText().toString();

                Intent intent = new Intent(view.getContext(), BookDetailActivity.class);

                //put bitmap image as array of bytes
                intent.putExtra("content", mContent); // put title
                intent.putExtra("title",mTitle);
                intent.putExtra("gender",mGender);
                intent.putExtra("lenght",mLenght);
                //intent.putExtra("image",mImage);
                //put description
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    public ArrayList<Book> getData() {

        try {
            ArrayList<Book> books=new ArrayList<Book>();

            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("Favoritbbook",MODE_PRIVATE,null);
            String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS books ( " +
                    "title TEXT, " +
                    "gender TEXT, " +
                    "content TEXT," +
                    "lenght TEXT," +
                    "IsRead TEXT," +
                    "imageUrl TEXT  )";
            sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM books",null);
            int contentX=cursor.getColumnIndex("content");
            int titleX=cursor.getColumnIndex("title");
            int genderX=cursor.getColumnIndex("gender");
            int lenghtX=cursor.getColumnIndex("lenght");
            int isReadX=cursor.getColumnIndex("IsRead");
            int isImageX=cursor.getColumnIndex("imageUrl");

            while (cursor.moveToNext()){
                Book book=new Book();

                book.IsRead=cursor.getString(isReadX);
                book.lenght=cursor.getString(lenghtX);
                book.gender=cursor.getString(genderX);
                book.title=cursor.getString(titleX);
                book.content=cursor.getString(contentX);
                book.imageurl=cursor.getString(isImageX);

                books.add(book);

            }
            cursor.close();

            return books;
        }catch (Exception e){

            return null;
        }
    }


}
