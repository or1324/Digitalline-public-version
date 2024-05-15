package com.example.digitalline;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

import java.util.LinkedHashMap;

public class UserMessages {

    private static Toast toast = null;

    public static void showEmptyDialogMessage(String message, Activity activity) {
        BackgroundRunningHelper.runCodeOnUiThread(() -> new AlertDialog.Builder(activity).setMessage(message).show());
    }

    public static void showButtonDialogQuestion(String message, Activity activity, Runnable ifYes) {
        BackgroundRunningHelper.runCodeOnUiThread(() -> new AlertDialog.Builder(activity).setMessage(message).setPositiveButton("Yes", (dialog, which) -> ifYes.run()).show());
    }

    public static void showEmptyDialogMessage(String message, Activity activity, Runnable onDismiss) {
        BackgroundRunningHelper.runCodeOnUiThread(() -> new AlertDialog.Builder(activity).setMessage(message).setOnDismissListener(dialogInterface -> onDismiss.run()).show());
    }

    public static void showButtonsDialogOptions(String message, Activity activity, LinkedHashMap<String, Runnable> buttons) {
        BackgroundRunningHelper.runCodeOnUiThread(() -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(message);
            String[] buttonStrings = new String[buttons.size()];
            buttons.keySet().toArray(buttonStrings);
            alertDialogBuilder.setItems(buttonStrings, (dialog, which) -> {
                String clickedButtonText = buttonStrings[which];
                buttons.get(clickedButtonText).run();
            });
            alertDialogBuilder.create().show();
        });
    }

    public static void showToastMessage(String message, Activity activity) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
