package com.tofa.circular.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.customclass.DialogType;

import java.util.Objects;

public class InputDialog {

    private Context context;
    private TextView txtTitle, txtSubtitle, txtDetail;
    private LinearLayout lytTitle;
    private EditText edtInput;
    private Button btnPositive, btnNegative;
    private Dialog alertDialog;
    private DialogType dialogType = DialogType.DIALOG_INFO;

    public InputDialog(Context context) {
        this.context = context;
        alertDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        alertDialog.setContentView(view);
        txtTitle = view.findViewById(R.id.txtInputDialogTitle);
        lytTitle = view.findViewById(R.id.lytInputDialogTitle);
        txtSubtitle = view.findViewById(R.id.txtInputDialogSubtitle);
        txtDetail = view.findViewById(R.id.txtInputDialogDetail);
        edtInput = view.findViewById(R.id.edtInputDialogInput);
        changeTitleBG();
        btnPositive = view.findViewById(R.id.btnInputDialogOK);
        btnPositive.setOnClickListener(v -> alertDialog.dismiss());
        btnNegative = view.findViewById(R.id.btnInputDialogBack);
        btnNegative.setOnClickListener(v -> alertDialog.dismiss());
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setShowSubtitle(Boolean showSubtitle) {
        if(showSubtitle) {
            lytTitle.setVisibility(View.VISIBLE);
        } else {
            lytTitle.setVisibility(View.GONE);
        }
    }

    public void setSubTitle(String subTitle) {
        txtSubtitle.setText(subTitle);
    }

    public void setDetail(String detail) {
        txtDetail.setText(detail);
    }

    public void setText(String text) {
        edtInput.setText(text);
    }

    public String getText() {
        return edtInput.getText().toString();
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
        edtInput.requestFocus();
        edtInput.selectAll();
        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
