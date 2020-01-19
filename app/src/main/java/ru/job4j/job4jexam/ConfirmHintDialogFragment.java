package ru.job4j.job4jexam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmHintDialogFragment extends DialogFragment {
    private ConfirmHintDialogListener callBack;

    public interface ConfirmHintDialogListener {
        void onPositiveDialogClick(DialogFragment dialog);

        void onNegativeDialogClick(DialogFragment dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage("Показать подсказку?")
                .setPositiveButton(android.R.string.ok, (dialog1, which) ->
                        callBack.onPositiveDialogClick(ConfirmHintDialogFragment.this))
                .setNegativeButton(android.R.string.cancel, (dialog2, which) ->
                        callBack.onNegativeDialogClick(ConfirmHintDialogFragment.this))
                .create();
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callBack = (ConfirmHintDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    String.format(
                            "%s must implements ConfirmHintDialogListener", context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }
}
