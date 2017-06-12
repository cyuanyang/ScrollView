package com.cyy.mywidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.cyy.mywidget.scrollview.MyScrollView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected ScrollView scrollView;
    protected Button scrollBtn;
    protected LinearLayout scrollViewContainer;
    private MyScrollView myScrollView;
    protected LinearLayout myScrollViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

        int count = myScrollViewContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = myScrollViewContainer.getChildAt(i);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "ssss", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myScrollView = (MyScrollView) findViewById(R.id.myScrollView);
        myScrollViewContainer = (LinearLayout) findViewById(R.id.myScrollViewContainer);
        scrollBtn = (Button) findViewById(R.id.scrollBtn);
        scrollBtn.setOnClickListener(MainActivity.this);
        scrollViewContainer = (LinearLayout) findViewById(R.id.scrollViewContainer);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.scrollBtn) {
            myScrollView.smoothScrollBy(0 , 300);
        }
    }
}
