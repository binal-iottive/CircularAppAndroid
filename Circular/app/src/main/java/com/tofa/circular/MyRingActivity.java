package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramijemli.percentagechartview.PercentageChartView;
import com.tofa.circular.dialog.BasicDialog;
import com.tofa.circular.customclass.DialogType;

public class MyRingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }

        setContentView(R.layout.activity_my_ring);

        Toolbar mToolbar = findViewById(R.id.toolbarMain);
        ImageView btnBack = mToolbar.findViewById(R.id.btnNewAlarmBack);
        btnBack.setOnClickListener(view -> finish());

        PercentageChartView pg = findViewById(R.id.pbMyRing);
        TextView lblProgress = findViewById(R.id.lblMyRingProgress);

        pg.setTextFormatter(progress -> "");

        lblProgress.setText(String.format("%s%%", (int) pg.getProgress()));

        pg.setOnProgressChangeListener(progress -> lblProgress.setText(String.format("%s%%", (int) progress)));

        RelativeLayout lytSoft = findViewById(R.id.lytSoftReset);
        lytSoft.setOnClickListener(view -> {
            final BasicDialog basicDialog = new BasicDialog(this);
            basicDialog.setTitle(getResources().getString(R.string.q_confirm));
            basicDialog.setSubTitle(getResources().getString(R.string.q_softreset));
            basicDialog.setDetail(getResources().getString(R.string.d_softreset));
            basicDialog.setDialogType(DialogType.DIALOG_WARNING);
            basicDialog.setBtnPositive(getResources().getString(R.string.ok), vw -> basicDialog.dismiss());
            basicDialog.setBtnNegative(getResources().getString(R.string.back), vw -> basicDialog.dismiss());
            basicDialog.show();
        });

        RelativeLayout lyyHard = findViewById(R.id.lytHardReset);
        lyyHard.setOnClickListener(view -> {
            final BasicDialog basicDialog = new BasicDialog(MyRingActivity.this);
            basicDialog.setTitle(getResources().getString(R.string.q_confirm));
            basicDialog.setSubTitle(getResources().getString(R.string.q_hardreset));
            basicDialog.setDetail(getResources().getString(R.string.d_hardreset));
            basicDialog.setDialogType(DialogType.DIALOG_WARNING);
            basicDialog.setBtnPositive(getResources().getString(R.string.ok), vw -> basicDialog.dismiss());
            basicDialog.setBtnNegative(getResources().getString(R.string.back), vw -> basicDialog.dismiss());
            basicDialog.show();
        });
    }
}
