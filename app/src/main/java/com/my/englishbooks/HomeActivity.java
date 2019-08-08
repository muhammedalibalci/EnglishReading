package com.my.englishbooks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {
    Button button;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button=findViewById(R.id.booksButon3);
        SQLiteDatabase sqLiteDatabase2=this.openOrCreateDatabase("contentDB",MODE_PRIVATE,null);

        SQLiteDatabase sqLiteDatabase;
    }

    public void ReadingFunc(View view){
        Intent intent =new Intent(getApplicationContext(),LevelActivity.class);
        startActivity(intent);
    }
    public void FavoriteFunc(View view){
        Intent intent =new Intent(getApplicationContext(),FavoriteActivity.class);
        startActivity(intent);
    }

    public void ContinueRead(View view){

        try{
            final SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("lastReadd",MODE_PRIVATE,null);
            String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS lastread ( " +

                    "title TEXT, " +
                    "gender TEXT, " +
                    "content TEXT," +
                    "lenght TEXT," +
                    "IsRead TEXT," +
                    "imageUrl TEXT )";
            sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);

            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM lastread",null);




            if(cursor!=null){
                int contentX=cursor.getColumnIndex("content");
                int titleX=cursor.getColumnIndex("title");
                int genderX=cursor.getColumnIndex("gender");
                int lenghtX=cursor.getColumnIndex("lenght");
                int isReadX=cursor.getColumnIndex("IsRead");
                int isImageX=cursor.getColumnIndex("imageUrl");

                Book book=new Book();
                int sayac=0;
                while (cursor.moveToNext()){


                    book.IsRead=cursor.getString(isReadX);
                    book.lenght=cursor.getString(lenghtX);
                    book.gender=cursor.getString(genderX);
                    book.title=cursor.getString(titleX);
                    book.content=cursor.getString(contentX);
                    book.imageurl=cursor.getString(isImageX);
                    sayac++;


                }
                cursor.close();
                if(sayac!=0 ){
                    Intent intent =new Intent(getApplicationContext(),BookDetailActivity.class);
                    intent.putExtra("content",book.content);
                    intent.putExtra("title",book.title);
                    intent.putExtra("gender",book.gender);
                    intent.putExtra("lenght",book.lenght);
                    intent.putExtra("IsRead",book.IsRead);
                    intent.putExtra("imagetextUrl",book.imageurl);
                    startActivity(intent);

                    if(sayac>3){
                        sqLiteDatabase.execSQL("DELETE FROM lastread");
                    }
                }

            }


        }catch (Exception e){

        }


    }

    public void ShareClick(View view){
        Intent myintent =new Intent(Intent.ACTION_SEND);
        myintent.setType("text/plain");
        String shareBody = "Body";
        String shareSub = "Sub";
        myintent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        myintent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(myintent,"Share using"));
    }
    public void GoShare(View view){
        Intent intent = new Intent(getApplicationContext(),ShareActivity.class);
        startActivity(intent);
    }


    public void resetMyFavorite(View view){

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage("Are you sure you want to delete the my favorites?");
        builder1.setTitle("Warning!");
        builder1.setCancelable(true);
        sqLiteDatabase=this.openOrCreateDatabase("Favoritbbook",MODE_PRIVATE,null);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            sqLiteDatabase.execSQL("DELETE FROM books");
                        }
                        catch (Exception e){

                        }





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




    }
}
