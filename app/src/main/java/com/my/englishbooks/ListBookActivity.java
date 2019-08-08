package com.my.englishbooks;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ListBookActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Book book;
    List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        Intent intent = getIntent();
        String level =intent.getStringExtra("level");
        book=new Book();

        recyclerView=findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        books=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase=FirebaseDatabase.getInstance();

        if(level.equals("elementary")){
            reference=firebaseDatabase.getReference().child("Book").child("Elemantary");
        }
        if(level.equals("preinter")){
            reference=firebaseDatabase.getReference().child("Book").child("PreIntermediate");
        }
        if(level.equals("inter")){
            reference=firebaseDatabase.getReference().child("Book").child("Intermediate");
        }
        if(level.equals("upinter")){
            reference=firebaseDatabase.getReference().child("Book").child("UpIntermediate");
        }
        if(level.equals("advance")){
            reference=firebaseDatabase.getReference().child("Book").child("Advance");
        }

        System.out.println(book.title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("lastReadd",MODE_PRIVATE,null);
       final SQLiteDatabase sqLiteDatabase2=this.openOrCreateDatabase("contentDbs",MODE_PRIVATE,null);

        FirebaseRecyclerAdapter<Book, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Book, ViewHolder>(
                        Book.class,
                        R.layout.row,
                        ViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Book model, int position) {


                        String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS contents ( " +
                                "title TEXT, " +
                                "content TEXT )";
                        sqLiteDatabase2.execSQL(CREATE_BOOK_TABLE);

                        String sqlString = "INSERT INTO contents (title,content) VALUES (?,?)";
                        SQLiteStatement sqLiteStatement=sqLiteDatabase2.compileStatement(sqlString);

                        sqLiteStatement.bindString(1,model.getTitle());
                        sqLiteStatement.bindString(2,model.getContent());

                        sqLiteStatement.execute();


                        viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getImageurl(),
                                model.getGender(),model.getLenght());

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {





                                //Views

                                TextView mtitleView= view.findViewById(R.id.titleView);
                                TextView mgenderView= view.findViewById(R.id.genreView);
                                TextView lenghtView= view.findViewById(R.id.lenghtView);
                                TextView imagetextView= view.findViewById(R.id.imagetextView);



                                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();


                                //get data from views

                                String mTitle = mtitleView.getText().toString();
                                String mGender = mgenderView.getText().toString();
                                String mLenght = lenghtView.getText().toString();
                                String mİmageText = imagetextView.getText().toString();


                                Intent intent = new Intent(view.getContext(), BookDetailActivity.class);
                                Cursor cursor=sqLiteDatabase2.rawQuery("SELECT * FROM contents",null);

                                int contentX = cursor.getColumnIndex("content");
                                int titleX = cursor.getColumnIndex("title");

                                String contentmy="";
                                while (cursor.moveToNext()){
                                        if(mTitle.equals(cursor.getString(titleX))){
                                            contentmy=cursor.getString(contentX);
                                            intent.putExtra("content", cursor.getString(contentX));
                                        }
                                }

                                //put bitmap image as array of bytes
                                 // put title
                                intent.putExtra("title",mTitle);
                                intent.putExtra("gender",mGender);
                                intent.putExtra("lenght",mLenght);
                                intent.putExtra("imagetextUrl",mİmageText);

                                startActivity(intent); //start activity

                                String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS lastread ( " +

                                        "title TEXT, " +
                                        "gender TEXT, " +
                                        "content TEXT," +
                                        "lenght TEXT," +
                                        "IsRead TEXT," +
                                        "imageUrl TEXT )";
                                sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
                                String sqlString = "INSERT INTO lastread (title,gender,content,lenght,imageUrl,IsRead) VALUES (?,?,?,?,?,?)";
                                SQLiteStatement sqLiteStatement=sqLiteDatabase.compileStatement(sqlString);

                                sqLiteStatement.bindString(1,mTitle);
                                sqLiteStatement.bindString(2,mGender);
                                sqLiteStatement.bindString(3,contentmy);
                                sqLiteStatement.bindString(4,mLenght);
                                sqLiteStatement.bindString(5,mİmageText);

                                sqLiteStatement.execute();
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return viewHolder;
                    }

                };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
