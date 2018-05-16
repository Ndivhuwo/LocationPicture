package com.smartalgorithms.locationpictures.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartalgorithms.locationpictures.R;

import javax.inject.Inject;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class DialogHelper {
    private Toast toast;

    @Inject
    public DialogHelper(Toast toast) {
        this.toast = toast;
    }

    public void showCustomAlertDialog(Context context, String title, String message, View.OnClickListener ok_listener, View.OnClickListener no_listener, boolean has_cancel, String positiveText, String negativeText){

        AlertDialog customDialog = new AlertDialog.Builder(context).create();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View consentDialogView = layoutInflater.inflate(R.layout.dialog_custom, null);
        customDialog.setView(consentDialogView);

        TextView negative_text = consentDialogView.findViewById(R.id.tv_negative_feedback);
        TextView positive_text = consentDialogView.findViewById(R.id.tv_positive_feedback);
        TextView title_text = consentDialogView.findViewById(R.id.tv_title);
        TextView message_text = consentDialogView.findViewById(R.id.tv_message);

        title_text.setText(title);
        message_text.setText(message);
        positive_text.setText(positiveText);

        if(has_cancel) {
            if(negativeText != null && negativeText.length() > 7)
                negative_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            negative_text.setText(negativeText);
            negative_text.setVisibility(View.VISIBLE);
            consentDialogView.findViewById(R.id.tv_negative_feedback).setOnClickListener(view -> {
                if (no_listener != null)
                    no_listener.onClick(view);
                customDialog.dismiss();
            });
        }else {
            negative_text.setVisibility(View.GONE);
        }
        consentDialogView.findViewById(R.id.tv_positive_feedback).setOnClickListener(view ->{
            if(ok_listener != null)
                ok_listener.onClick(view);
            customDialog.dismiss();
        });
        if(customDialog.isShowing())
            customDialog.dismiss();
        else
            customDialog.show();
    }

    public void showToast(String message) {
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
