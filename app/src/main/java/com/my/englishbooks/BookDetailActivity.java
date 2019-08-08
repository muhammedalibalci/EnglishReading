package com.my.englishbooks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tx1;
    int current;
    ScrollView scrollView;
    int start;
    int end;
    String dicText;
    Book book;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_detail);
        book=new Book();
        ActionBar actionBar = getSupportActionBar();
        imageView=findViewById(R.id.starButon);
        imageView.setEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tx1 = findViewById(R.id.textView);
        scrollView = findViewById(R.id.scrollView);

        String content = getIntent().getStringExtra("content").replace("\\n","\n");
        book.title = getIntent().getStringExtra("title");
        book.gender = getIntent().getStringExtra("gender");
        book.lenght = getIntent().getStringExtra("lenght");
        book.imageurl = getIntent().getStringExtra("imagetextUrl");
        book.content=content;
        //System.out.println(book.imageurl);
        actionBar.setTitle(book.title);
        tx1.setText(content);

        Spinner spinner = findViewById(R.id.dictionarySpinner);
        ArrayAdapter<CharSequence> adapter=
                ArrayAdapter.createFromResource(this,R.array.country,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        tx1.setOnTouchListener(new View.OnTouchListener() {
            int mOffset;

            @Override
            public boolean onTouch(View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tx1.setOnClickListener(new View.OnClickListener() {
                        float motionX = motionEvent.getX();
                        float motionY = motionEvent.getY();

                        @Override
                        public void onClick(View view) {
                            mOffset = tx1.getOffsetForPosition(motionX, motionY);
                            //  mTxtOffset.setText("" + mOffset);
                            String value = findWordForRightHanded(tx1.getText().toString(), mOffset);
                            if (!value.equals("")) {
                                ScrollView scrollView2=findViewById(R.id.scrollView2);
                                scrollView2.setVisibility(View.VISIBLE);
                                SpannableString spannableString = new SpannableString(tx1.getText().toString());
                                ForegroundColorSpan green = new ForegroundColorSpan(Color.GREEN);

                                spannableString.setSpan(green, start, end, 0);

                                tx1.setText(spannableString);
                                System.out.println(value);



                                DownloadData downloadData = new DownloadData();
                                try {
                                    StringBuilder total = new StringBuilder();
                                    if(dicText.equals("Turkish")){
                                        String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20190723T075559Z.8f5b85d142dd9291.b65705fc3cb011117f1a24b08eb45d44ca888139&lang=en-tr&text=";
                                        total.append(url);
                                        total.append(value);


                                        downloadData.execute(total.toString());
                                    }
                                    if(dicText.equals("French")){
                                        String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20190723T075559Z.8f5b85d142dd9291.b65705fc3cb011117f1a24b08eb45d44ca888139&lang=en-fr&text=" + value;

                                        downloadData.execute(url);
                                    }
                                    if(dicText.equals("Italian")){
                                        String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20190723T075559Z.8f5b85d142dd9291.b65705fc3cb011117f1a24b08eb45d44ca888139&lang=en-it&text=" + value;

                                        downloadData.execute(url);
                                    }
                                    if(dicText.equals("German")){
                                        String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20190723T075559Z.8f5b85d142dd9291.b65705fc3cb011117f1a24b08eb45d44ca888139&lang=en-de&text=" + value;

                                        downloadData.execute(url);
                                    }
                                    if(dicText.equals("Spanish")){
                                        String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20190723T075559Z.8f5b85d142dd9291.b65705fc3cb011117f1a24b08eb45d44ca888139&lang=en-es&text=" + value;

                                        downloadData.execute(url);
                                    }



                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),"HATA",Toast.LENGTH_LONG).show();
                                    System.out.println(e);
                                }

                            }
                        }
                    });


                }
                return false;
            }
        });

    }
    private String findWordForRightHanded(String str, int offset) {
        try {
            if (!str.equals("")) {// when you touch ' ', this method returns left word.
                if (str.length() == offset) {
                    offset--; // without this code, you will get exception when touching end of the text
                }

                if (str.charAt(offset) == ' ') {
                    offset--;
                }
                int startIndex = offset;
                int endIndex = offset;

                try {
                    while (str.charAt(startIndex) != ' ' && str.charAt(startIndex) != '\n') {
                        startIndex--;
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    startIndex = 0;
                }

                try {
                    while (str.charAt(endIndex) != ' ' && str.charAt(endIndex) != '\n') {
                        endIndex++;
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    endIndex = str.length();
                }

                // without this code, you will get 'here!' instead of 'here'
                // if you use only english, just check whether this is alphabet,
                // but 'I' use korean, so i use below algorithm to get clean word.


                char last = str.charAt(endIndex - 1);

                if (last == ',' || last == '\u002E' ||
                        last == '!' || last == '?' ||
                        last == ':' || last == ';') {
                    endIndex--;

                }

                char pre = str.charAt(startIndex + 1);
                if (pre == ',' || pre == '.' ||
                        pre == '!' || pre == '\'' ||
                        pre == ':' || pre == ';') {

                    startIndex++;
                }


                start = startIndex + 1;
                end = endIndex;
                String word = str.substring(start, end);


                int lenght = word.length();
                if (word.charAt(lenght - 1) == '\'') {

                    start = startIndex + 1;
                    end = endIndex - 2;
                    String worda = str.substring(start, end);
                    return worda;
                }

                return word;

            }
            return "";
        }catch (Exception e){
            return "";
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        dicText =adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void MakeZoom(View view) {


        tx1.setTextSize(18);


    }
    public void MakeOutZoom(View view){
        tx1.setTextSize(15);
    }

    public void MakeBlack(View view) {
        current++;
        if (current % 2 != 0) {
            scrollView.setBackgroundColor(Color.parseColor("#2E4053"));
            tx1.setTextColor(Color.WHITE);
        } else {
            scrollView.setBackgroundColor(Color.WHITE);
            tx1.setTextColor(Color.BLACK);
        }

    }

    public void AddFavorite(View view){



        try {

            final String DATABASE_NAME = "books";
            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("Favoritbbook",MODE_PRIVATE,null);
            String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS books ( " +

                    "title TEXT, " +
                    "gender TEXT, " +
                    "content TEXT," +
                    "lenght TEXT," +
                    "IsRead TEXT," +
                    "imageUrl TEXT )";
            sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
            SQLiteDatabase sqLiteDatabase2=this.openOrCreateDatabase("Favoritbbook",MODE_PRIVATE,null);

            sqLiteDatabase2.execSQL(CREATE_BOOK_TABLE);
            Cursor cursor=sqLiteDatabase2.rawQuery("SELECT * FROM books",null);
            int contentX=cursor.getColumnIndex("title");
            Book booka=new Book();
            int i=0;
            while (cursor.moveToNext()){

                booka.title=cursor.getString(contentX);
                if(booka.title.equals(book.title)){
                    i=1;
                    System.out.println("asdasdzxc");
                }

                System.out.println(book.title);
                System.out.println(booka.title);
            }
            System.out.println(i);
            if(i!=1){
                String sqlString = "INSERT INTO books (title,gender,content,lenght,IsRead,imageUrl) VALUES (?,?,?,?,?,?)";
                SQLiteStatement sqLiteStatement=sqLiteDatabase.compileStatement(sqlString);


                sqLiteStatement.bindString(1,book.title);
                sqLiteStatement.bindString(2,book.gender);
                sqLiteStatement.bindString(3,book.content);
                sqLiteStatement.bindString(4,book.lenght);
                book.IsRead="false";
                sqLiteStatement.bindString(5,book.IsRead);
                sqLiteStatement.bindString(6,book.imageurl);

                sqLiteStatement.execute();

                Toast.makeText(this,"Added to Favorite",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Already  Added",Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e){
            System.out.println(e);
        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class DownloadData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0) {

                    char character = (char) data;
                    result += character;

                    data = inputStreamReader.read();

                }


                return result;

            } catch (Exception e) {
                System.out.println("asdasdas");
                return "";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!s.equals("{\"head\":{},\"def\":[]}")) {


                List<Dictonary> dictonaries = new ArrayList<Dictonary>();
                Dictonary dictonary;
                TextView titleView = findViewById(R.id.titleViewD);
                TextView descView = findViewById(R.id.decsViewD);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String def = jsonObject.getString("def");
                    JSONArray jsonObject1 = new JSONArray(def);


                    for (int i = 0; i < jsonObject1.length(); i++) {
                        String def2 = jsonObject1.get(i).toString();

                        JSONObject jsonObject2 = new JSONObject(def2);
                        String baslik = jsonObject2.getString("pos");//noun vs.
                        String text = jsonObject2.optString("text");//searced

                        titleView.setText(text);

                        String tr = jsonObject2.getString("tr");

                        JSONArray jsonObject3 = new JSONArray(tr);


                        if (jsonObject3.length() != 0) {
                            for (int j = 0; j < jsonObject3.length(); j++) {
                                String tr2 = jsonObject3.get(j).toString();
                                JSONObject jsonObject4 = new JSONObject(tr2);
                                String mean = jsonObject4.getString("text");

                                dictonary = new Dictonary();
                                dictonary.mean = mean;
                                dictonaries.add(dictonary);
                            }
                            StringBuilder total = new StringBuilder();
                            for (Dictonary item : dictonaries) {
                                total.append(item.mean + ", ");
                            }
                            descView.setText(total);
                        }


                    }

                } catch (Exception e) {
                    System.out.println(e);
                }


            }
        }
    }
}
