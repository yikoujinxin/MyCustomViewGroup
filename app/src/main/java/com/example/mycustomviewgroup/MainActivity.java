package com.example.mycustomviewgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // layout 实例
    ImageLayout imageLayout;

    // 自定义viewGroup
    ImageLayout my_custom_image_view_layout;

    // 图片管理类
    ImageMgr imageMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        my_custom_image_view_layout = findViewById(R.id.my_custom_image_view_layout);

        imageLayout = new ImageLayout(MainActivity.this);
        imageLayout.setImageLayout(my_custom_image_view_layout, getWindowManager().getDefaultDisplay().getWidth());
        imageMgr = new ImageMgr();
        imageLayout.setImageMgr(imageMgr);

        findViewById(R.id.add_pic_one_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic1, "pic1_uuid");
            }
        });
        findViewById(R.id.add_pic_two_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic2, "pic2_uuid");
            }
        });
        findViewById(R.id.add_pic_three_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic3, "pic3_uuid");
            }
        });
        findViewById(R.id.add_pic_four_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic4, "pic4_uuid");
            }
        });
        findViewById(R.id.add_pic_five_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic5, "pic5_uuid");
            }
        });
        findViewById(R.id.add_pic_six_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic6, "pic6_uuid");
            }
        });
        findViewById(R.id.add_pic_eleven_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic7, "pic7_uuid");
            }
        });
        findViewById(R.id.add_pic_eight_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.addImage(R.drawable.pic8, "pic8_uuid");
            }
        });

        findViewById(R.id.delete_pic_one_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.removeImage("pic1_uuid");
            }
        });
        findViewById(R.id.delete_pic_two_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.removeImage("pic2_uuid");
            }
        });
        findViewById(R.id.delete_pic_three_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.removeImage("pic3_uuid");
            }
        });
        findViewById(R.id.delete_pic_four_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.removeImage("pic4_uuid");
            }
        });

    }
}
