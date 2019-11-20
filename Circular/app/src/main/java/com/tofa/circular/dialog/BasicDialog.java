package com.tofa.circular.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.customclass.DialogType;

public class BasicDialog {

    private Context context;
    private TextView txtTitle, txtSubtitle, txtDetail;
    private Button btnPositive, btnNegative;
    private Dialog alertDialog;
    private DialogType dialogType = DialogType.DIALOG_INFO;
    private View view;

    public BasicDialog(Context context) {
        this.context = context;
        alertDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        view = LayoutInflater.from(context).inflate(R.layout.basic_dialog, null);
        alertDialog.setContentView(view);
        txtTitle = view.findViewById(R.id.txtBasicDialogTitle);
        txtSubtitle = view.findViewById(R.id.txtBasicDialogSubtitle);
        txtDetail = view.findViewById(R.id.txtBasicDialogDetail);
        changeTitleBG();
        btnPositive = view.findViewById(R.id.btnBasicDialogOK);
        btnPositive.setOnClickListener(v -> alertDialog.dismiss());
        btnNegative = view.findViewById(R.id.btnBasicDialogBack);
        btnNegative.setOnClickListener(v -> alertDialog.dismiss());
    }

    public View getView() {
        return view;
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setSubTitle(String subTitle) {
        txtSubtitle.setText(subTitle);
    }

    public void setDetail(String detail) {
        txtDetail.setText(detail);
    }

    public void setDialogType(DialogType dialogType) {
        this.dialogType = dialogType;
        changeTitleBG();
    }

    public void setBtnPositive(String text, View.OnClickListener clickListener){
        btnPositive.setText(text);
        btnPositive.setOnClickListener(clickListener);
    }

    public void setBtnNegative(String text, View.OnClickListener clickListener){
        btnNegative.setText(text);
        btnNegative.setOnClickListener(clickListener);
    }

    public void show(){
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }

    private void changeTitleBG(){
        RelativeLayout titleBG = (RelativeLayout) txtTitle.getParent();
        switch (dialogType) {
            case DIALOG_WARNING:
                titleBG.setBackground(context.getResources().getDrawable(R.drawable.bg_dialog_title_warning));
                break;
            case DIALOG_INFO:
                titleBG.setBackground(context.getResources().getDrawable(R.drawable.bg_dialog_title_info));
                break;
        }
    }
}
