package com.tofa.circular.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.adapter.ListCheckAdapter;
import com.tofa.circular.customclass.DialogType;

import java.util.List;

public class CheckListDialog {

    private Context context;
    private TextView txtTitle, txtSubtitle, txtDetail;
    private LinearLayout lytTitle;
    private Button btnPositive, btnNegative;
    private Dialog alertDialog;
    private ListCheckAdapter listCheckAdapter;
    private DialogType dialogType = DialogType.DIALOG_INFO;
    private int[] separatorList;


    public CheckListDialog(Context context, List<String> list) {
        init(context, list, -1);
    }

    public CheckListDialog(Context context, List<String> list, int checkedDefault) {
        init(context, list, checkedDefault);
    }

    private void init(Context context, List<String> list, int checkedDefault){
        this.context = context;
        alertDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.checklist_dialog, null);
        alertDialog.setContentView(view);
        txtTitle = view.findViewById(R.id.txtChecklistDialogTitle);
        lytTitle = view.findViewById(R.id.lytChecklistDialogTitle);
        txtSubtitle = view.findViewById(R.id.txtChecklistDialogSubtitle);
        txtDetail = view.findViewById(R.id.txtChecklistDialogDetail);

        changeTitleBG();

        listCheckAdapter = new ListCheckAdapter((Activity) context, list, true, checkedDefault);
        ListView listView = view.findViewById(R.id.listviewChecklistDialog);
        listView.setAdapter(listCheckAdapter);

        btnPositive = view.findViewById(R.id.btnChecklistDialogOK);
        btnPositive.setOnClickListener(v -> alertDialog.dismiss());
        btnNegative = view.findViewById(R.id.btnChecklistDialogBack);
        btnNegative.setOnClickListener(v -> alertDialog.dismiss());
    }

    public void setSeparatorList(int[] separatorList){
        listCheckAdapter.setSeparatorList(separatorList);
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

    public void setMultiSelect(Boolean multiSelect) {
        listCheckAdapter.setMultiSelect(multiSelect);
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

    public int getSelectedPosition(){
        return listCheckAdapter.getSelectedPosition();
    }

    public List<Boolean> getSelectedList() {
        return listCheckAdapter.getSelectedList();
    }
    public void setSelectedList(List<Boolean> list) {
        for ( int i=0; i<listCheckAdapter.getSelectedList().size(); i++ )
        {
            listCheckAdapter.setSelectedList(i,list.get(i));
        }
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
