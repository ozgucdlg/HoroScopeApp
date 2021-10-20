package com.example.horoscopeapp;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SecondActivity extends AppCompatActivity{

    TextView date_range, today_date, description, compatibility, mood, color, lucky_number, lucky_time;
    Button btn2, btn3;
    HoroscopeClass horoscopetxt;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        date_range = findViewById(R.id.date_range);
        today_date = findViewById(R.id.today_date);
        description = findViewById(R.id.description);
        compatibility = findViewById(R.id.compatibility);
        mood = findViewById(R.id.mood);
        color = findViewById(R.id.color);
        lucky_number = findViewById(R.id.lucky_number);
        lucky_time = findViewById(R.id.lucky_time);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);


        Intent intent = getIntent();
        String sign_choosen = intent.getExtras().getString("Zodiac Sign");
        String day_choosen = intent.getExtras().getString("Day");

        //Array to store the sign names
        String [] signs_array = {getResources().getResourceName(R.drawable.aquarius).substring(34),
                getResources().getResourceName(R.drawable.aries).substring(34),
                getResources().getResourceName(R.drawable.cancer).substring(34),
                getResources().getResourceName(R.drawable.capricorn).substring(34),
                getResources().getResourceName(R.drawable.gemini).substring(34),
                getResources().getResourceName(R.drawable.leo).substring(34),
                getResources().getResourceName(R.drawable.libra).substring(34),
                getResources().getResourceName(R.drawable.virgo).substring(34),
                getResources().getResourceName(R.drawable.scorpio).substring(34),
                getResources().getResourceName(R.drawable.taurus).substring(34),
                getResources().getResourceName(R.drawable.sagittarius).substring(34),
                getResources().getResourceName(R.drawable.pisces).substring(34)
        };

        //Array to store the images for each sign
        int [] id_array = {R.drawable.aquarius,
                R.drawable.aries,
                R.drawable.cancer,
                R.drawable.capricorn,
                R.drawable.gemini,
                R.drawable.leo,
                R.drawable.libra,
                R.drawable.virgo,
                R.drawable.scorpio,
                R.drawable.taurus,
                R.drawable.sagittarius,
                R.drawable.pisces
        };

        int pos = 0;
        String x = null;
        //With this loop we look for the selected sign on the array of names and this position is stored to look on the array of images
        for (int i=0; i<signs_array.length;i++){
            if(signs_array[i].equals(sign_choosen)){
                pos = i;
                x= signs_array[i];
                break;
            }
        }

        ImageView zodiac_sign= findViewById(R.id.image_sign);
        //We look on the array of images and display it on the view
        zodiac_sign.setImageResource(id_array[pos]);

        //We call the api to access to our daily horoscope
        searchHoroscopeOnApi(sign_choosen,day_choosen);


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
                //HoroscopeDAO horoscopeDAO = new HoroscopeDAO(SecondActivity.this);
                String your_sign =  horoscopetxt.getSign().substring(0, 1).toUpperCase() + horoscopetxt.getSign().substring(1);
                String fileBody="Horoscope 2021"+"\n"+"Sign: "+your_sign+"\n"+"Date Range: "+horoscopetxt.getRange()+"\n"+
                        "Current Date: "+horoscopetxt.getCurrent_date()+"\n"+"Description: "+horoscopetxt.getDescription()+"\n"+
                        "Compatibility: "+horoscopetxt.getCompatibility()+"\n"+
                        "Mood: "+horoscopetxt.getMood()+"\n"+
                        "Color: "+horoscopetxt.getColor()+"\n"+
                        "Lucky Number: "+horoscopetxt.getLucky_number()+"\n"+
                        "Lucky Time: "+horoscopetxt.getLucky_time();
                Log.d("File", fileBody);
                generateNoteOnPhoneStorage(SecondActivity.this,  "Horoscope"+fileName, fileBody);
            }
        });

        }

    private void searchHoroscopeOnApi(String sign, String day) {
        //Here we connect with the api
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(APIClass.endPoint)
                .build();

        APIClass service = retrofit.create(APIClass.class);

        Call<HoroscopeClass> call = service.horoscope(sign, day);

        call.enqueue(new Callback<HoroscopeClass>() {
            @Override
            public void onResponse(Call<HoroscopeClass> call, Response<HoroscopeClass> response) {
                if (response.isSuccessful()) {
                    HoroscopeClass horoscope = response.body();
                    horoscopetxt =  response.body();
                    horoscope.setSign(sign);
                    //HoroscopeDAO horoscopeDAO = new HoroscopeDAO(SecondActivity.this);
                    //We store our info on the db
                    //horoscopeDAO.addRecord(horoscope);
                    //We send the info and display it on the view
                    setValuesToView(horoscope);
                } else {
                    //If there is an error connecting to the api a message will be displayed
                    Toast.makeText(
                            SecondActivity.this,
                            getString(R.string.error_horoscope)
                            , Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            //If there is an error connecting to the api a message will be displayed
            public void onFailure(Call<HoroscopeClass> call, Throwable t) {
                Toast.makeText(
                        SecondActivity.this
                        , getString(R.string.error_horoscope)
                        , Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void setValuesToView(HoroscopeClass horoscope) {

        today_date.setText(horoscope.getCurrent_date());
        date_range.setText(horoscope.getRange());
        description.setText(horoscope.getDescription());
        compatibility.setText(horoscope.getCompatibility());
        mood.setText(horoscope.getMood());
        color.setText(horoscope.getColor());
        lucky_number.setText(String.valueOf(horoscope.getLucky_number()));
        lucky_time.setText(horoscope.getLucky_time());
    }

    public void generateNoteOnPhoneStorage(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Horoscope");
            File myDir = new File(root + "/horoscope_app");
            myDir.mkdirs();
            File f = new File(myDir, sFileName);
            FileWriter writer = new FileWriter(f);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
