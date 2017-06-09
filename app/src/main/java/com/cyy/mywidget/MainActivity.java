package com.cyy.mywidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cyy.mywidget.scrollview.MyScrollView;

public class MainActivity extends AppCompatActivity {

    protected MyScrollView scrollView;
    protected LinearLayout scrollViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

        int count = scrollViewContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = scrollViewContainer.getChildAt(i);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "ssss", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void initView() {
        scrollView = (MyScrollView) findViewById(R.id.scrollView);
        scrollViewContainer = (LinearLayout) findViewById(R.id.scrollViewContainer);
    }
}
