package com.deepesh.snake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deepesh.snake.databinding.ActivityMainBinding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private class Box{
        int id,x,y;
        Box(int x,int y,int id){
            this.x=x;
            this.y=y;
            this.id=id;
            CardView cardView = new CardView(MainActivity.this);
            cardView.setCardElevation(8);
            cardView.setRadius(8);
            cardView.setId(id);
            Log.d("hiids",cardView.getId()+"");
            cardView.setBackgroundResource(R.drawable.box_style);
            cardView.setX(x);
            cardView.setY(y);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(s,s);
            cardView.setLayoutParams(layoutParams);
            binding.getRoot().addView(cardView);
        }
        Box(int x,int y,int id, int background){
            this.x=x;
            this.y=y;
            this.id=id;
            CardView cardView = new CardView(MainActivity.this);
            cardView.setCardElevation(8);
            cardView.setRadius(8);
            cardView.setId(id);
            Log.d("hiids",cardView.getId()+"");
            cardView.setBackgroundResource(background);
            cardView.setX(x);
            cardView.setY(y);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(s,s);
            cardView.setLayoutParams(layoutParams);
            binding.getRoot().addView(cardView);

        }
    }
    private  class Food{
        int x,y,id;
        Food(){
            Random rand = new Random();
            x=rand.nextInt(l-s);
            x=x-x%s;
            y=2*s+rand.nextInt(w-5*s);
            y=y+s-y%s;


            CardView cardView = new CardView(MainActivity.this);
            cardView.setCardElevation(8);
            cardView.setRadius(8);
            cardView.setId(0);
            cardView.setBackgroundResource(R.drawable.food_style);
            cardView.setX(x);
            cardView.setY(y);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(s,s);
            cardView.setLayoutParams(layoutParams);
            binding.getRoot().addView(cardView);
        }

    }

    int l,w,s,x1=0,y1=0,id=1,move=0,cond,score=0;
    boolean left=false,right=true,up=false,down=false,endCondition=false;
    ActivityMainBinding binding;
    CountDownTimer countDownTimer;
    int interval;
    Food food;
    Box box;
    Deque<Box> boxes;
    CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        interval=300;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightOfMobile = displayMetrics.heightPixels;
        int widthOfMobile = displayMetrics.widthPixels;
        l = widthOfMobile;
        w = heightOfMobile;
        int minLen = Integer.min(l,w);
        s = minLen / 15;
        move = s/5;

        for(int i=2;i<w/s;i++){
            for(int j=0;j<l/s;j++){
                Log.d("hideepesh","one"+i+" "+j);
                box = new Box(j*s,i*s,-1,R.drawable.background);
            }
        }
        gameStart();

    }
    protected void gameStart() {

        boxes = new ArrayDeque<>();
        id=1;


        y1=2*s;



        food = new Food();
        box = new Box(x1,y1,id);
        boxes.add(box);
        id++;
        x1=x1+s;
        box = new Box(x1,y1,id);
        boxes.add(box);
        id++;
        x1=x1+s;
        box = new Box(x1,y1,id);
        boxes.add(box);
        id++;


        binding.scoreView.setText("score: "+score);
        binding.scoreContainer.setX(l/2-s);
        binding.scoreContainer.setY(s/4);







        countDownTimer = new CountDownTimer(1000000000,150){
            @Override
            public void onTick(long millisUntilFinished) {

                if(up)
                    y1=y1-s;
                else if(left)
                    x1=x1-s;
                else if(down)
                    y1=y1+s;
                else
                    x1=x1+s;





                if(x1>l-s){
                    x1=0;
                }else if(x1<0){
                    x1=(l-s)-(l-s)%s;
                }else if(y1>w-s){
                    y1=2*s;
                }else if(y1<2*s){
                    y1=w-s-(w-s)%s;
                }


                if(food.x==x1&&food.y==y1){
                    CardView card1 = findViewById(0);
                    binding.getRoot().removeView(card1);
                    food = new Food();
                    box = new Box(x1,y1,id);
                    boxes.add(box);
                    score++;
                    binding.scoreView.setText("score: "+score);
                    id++;
                }else{


                    box = boxes.getFirst();
                    boxes.removeFirst();
                    card = findViewById(box.id);


                    box.x = x1;
                    box.y = y1;
                    card.setX(box.x);
                    card.setY(box.y);
                    boxes.addLast(box);

                }
                int kill=0;
                for(Box b : boxes){
                    if(kill==1){
                        countDownTimer.cancel();
                        endGame();

                    }
                    if(b.x==x1&&b.y==y1){
                        kill++;
                    }
                }
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if (up || down) {
                up = false;
                down = false;
                if (event.getX() > x1) {
                    right = true;
                } else {
                    left = true;
                }
            } else if (left || right) {
                left = false;
                right = false;
                if (event.getY() > y1) {
                    down = true;
                } else {
                    up = true;
                }
            }
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            if(endCondition){
                endCondition = false;
               // boxes.clear();
              //  binding.getRoot().removeAllViews();
                recreate();
            }
        }
        return super.onTouchEvent(event);
    }

    private void endGame(){
        for(Box b :boxes){
            CardView c2 = findViewById(b.id);
            c2.setBackgroundResource(R.drawable.end_style);
        }
        endCondition = true;

        }}