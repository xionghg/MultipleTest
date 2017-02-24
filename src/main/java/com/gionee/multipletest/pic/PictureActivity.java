package com.gionee.multipletest.pic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gionee.multipletest.R;

public class PictureActivity extends AppCompatActivity {

    public static final String PICTURE_NAME = "picture_name";

    public static final String PICTURE_URL = "picture_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intend = getIntent();
        String pictureName = intend.getStringExtra(PICTURE_NAME);
        String pictureUrl = intend.getStringExtra(PICTURE_URL);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView pictureImageView = (ImageView) findViewById(R.id.picture_image_view);
        TextView pictureContentText = (TextView) findViewById(R.id.picture_content_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(pictureName);
        Glide.with(this).load(pictureUrl).into(pictureImageView);
        String pictureContent = generateFruitContent(pictureName);
        pictureContentText.setText(pictureContent);
    }

    private String generateFruitContent(String pictureName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append(pictureName);
        }
        return sb.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
