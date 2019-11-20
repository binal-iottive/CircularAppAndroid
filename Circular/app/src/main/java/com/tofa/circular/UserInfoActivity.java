package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import in.goodiebag.carouselpicker.CarouselPicker.CarouselViewAdapter;
import in.goodiebag.carouselpicker.CarouselPicker.PickerItem;
import in.goodiebag.carouselpicker.CarouselPicker.TextItem;

import static android.view.View.MEASURED_STATE_MASK;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        CarouselPicker weightPicker = findViewById(R.id.carouselUserInfoWeight);
        List<PickerItem> itemWeights = new ArrayList<>();
        for (int i = 30; i < 150; i++) {
            itemWeights.add(new TextItem(Integer.toString(i), 10));
        }
        CarouselViewAdapter adapterWeight = new CarouselViewAdapter(this, itemWeights, 0);
        adapterWeight.setTextColor(MEASURED_STATE_MASK);
        weightPicker.setAdapter(adapterWeight);
        weightPicker.setCurrentItem(50);
        CarouselPicker heightPicker = findViewById(R.id.carouselUserInfoHeight);
        List<PickerItem> itemHeights = new ArrayList<>();
        for (int i2 = 100; i2 < 200; i2++) {
            itemHeights.add(new TextItem(Integer.toString(i2), 10));
        }
        CarouselViewAdapter adapterHeight = new CarouselViewAdapter(this, itemHeights, 0);
        adapterHeight.setTextColor(MEASURED_STATE_MASK);
        heightPicker.setAdapter(adapterHeight);
        heightPicker.setCurrentItem(60);

        findViewById(R.id.btnUserInfoNext).setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
