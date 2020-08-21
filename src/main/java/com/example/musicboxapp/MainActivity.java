package com.example.musicboxapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private ImageView artistImage;
    private TextView leftTime;
    private TextView rightTime;
    private SeekBar seekBar;
    private Button prevButton;
    private Button playButton;
    private Button nextButton;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI(); // here we declare the class setUPUI that we created in downwords

       seekBar.setMax(mediaPlayer.getDuration());
       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if (fromUser) {
                   mediaPlayer.seekTo(progress);
               }

               SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss"); // data class is created
               int currentPos = mediaPlayer.getCurrentPosition(); // CurrentPos int to get the position
               int duration = mediaPlayer.getDuration(); //duration int to get the duration

               leftTime.setText(dateFormat.format(new Date(currentPos)));  // new obj Date which convert the position into mm:ss formate
               rightTime.setText(dateFormat.format(new Date(duration - currentPos))); // same here but duration is minus form currentPos
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }

       });
    }


    public void setUpUI() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.tunepocket);

        artistImage = (ImageView) findViewById(R.id.imageView);
        leftTime = (TextView) findViewById(R.id.timeId);
        rightTime = (TextView) findViewById(R.id.time2Id);
        seekBar = (SeekBar) findViewById(R.id.seekBarId);
        prevButton = (Button) findViewById(R.id.prevbutton);
        playButton = (Button) findViewById(R.id.playbutton);
        nextButton = (Button) findViewById(R.id.nextbutton);

        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.prevbutton:
                // code
                BackMusic();  // class that prev the music that we created downwords
                break;

            case R.id.playbutton:
                //code
                if (mediaPlayer.isPlaying()){
                    pauseMusic();
                }else {
                    startMusic();
                }
                break;

            case R.id.nextbutton:
                //code

                NextMusic(); // Class that next music that we created downwords
                break;
        }


    }

    //PauseMusic
    public void pauseMusic(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
//            playButton.setBackgroundResource(R.drawable.);
        }
    }

    // startMusic
    public void startMusic(){
        if (mediaPlayer != null){
            mediaPlayer.start();
            updateThread();
//            playButton.setBackgroundResource(R.drawable.);
        }
    }
    // BackMusic

    public void BackMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(0);
        }
    }

    // NextMusic

    public void NextMusic(){
        if (mediaPlayer.isPlaying()){

            mediaPlayer.seekTo(mediaPlayer.getDuration() - 1000);
        }
    }





    // Update the music second as we move further automatically
    public void updateThread(){
         thread = new Thread() {

             @Override
             public void run() {
                 try {
                     while (mediaPlayer != null && mediaPlayer.isPlaying()) {


                             Thread.sleep(50);
                             runOnUiThread(new Runnable() {
                             @Override
                             public void run() {

                              int newPosition = mediaPlayer.getCurrentPosition();
                              int newMax = mediaPlayer.getDuration();
                              seekBar.setMax(newMax);
                              seekBar.setProgress(newPosition);

                              //update the text
                                 leftTime.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                                 .format(new Date(mediaPlayer.getCurrentPosition()))));

                                 rightTime.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                                         .format(new Date(mediaPlayer.getDuration()))));


                             }
                         });
                     }
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }

         };
         thread.start();

    }


//// On destroy MediaPlayer and Thread
    @Override
    protected void onDestroy() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()){

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
          thread.interrupt();
          thread = null;

        super.onDestroy();
    }
}


