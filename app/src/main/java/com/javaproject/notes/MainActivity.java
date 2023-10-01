package com.javaproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.javaproject.notes.Fragments.FragmentAdapter;
import com.javaproject.notes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageButton menubtn;
    Boolean clicked = false;
    Drawable whiteMenu,orangeMenu;
    TransitionDrawable crossfade1,crossfade2;
    PopupWindow popupWindow;
    TextView comnotes,signoutll;
    Animation popupAnimationOpen,popupAnimationClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        menubtn = findViewById(R.id.menuButton);


        whiteMenu = getResources().getDrawable(R.drawable.menu);
        orangeMenu = getResources().getDrawable(R.drawable.menu_orange);
        crossfade1 = new TransitionDrawable(new Drawable[]{whiteMenu, orangeMenu});
        crossfade1.setCrossFadeEnabled(true);
        crossfade2 = new TransitionDrawable(new Drawable[]{orangeMenu, whiteMenu});
        crossfade2.setCrossFadeEnabled(true);
        popupAnimationOpen = AnimationUtils.loadAnimation(this, R.anim.right_top_open);
        popupAnimationClose = AnimationUtils.loadAnimation(this,R.anim.right_top_close);


        View popupView = LayoutInflater.from(this).inflate(R.layout.menulayout,null);
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);

        comnotes = popupView.findViewById(R.id.ComNotes);
        comnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(MainActivity.this, Community_Notes.class);
                startActivity(intent);
            }
        });
        signoutll = popupView.findViewById(R.id.SignOut);
        signoutll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                finish();
            }
        });

        menubtn.setImageDrawable(crossfade1);

        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    popupWindow.showAtLocation(v,Gravity.END,40,-725);
                    menubtn.setImageDrawable(crossfade1);
                    crossfade1.startTransition(500);
                    popupView.startAnimation(popupAnimationOpen);
                    clicked = true;
                }
                else {
                    popupView.startAnimation(popupAnimationClose);
                    popupView.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            popupWindow.dismiss();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    menubtn.setImageDrawable(crossfade2);
                    crossfade2.startTransition(500);
                    clicked = false;
                }
            }
        });
    }
}