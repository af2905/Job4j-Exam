package ru.job4j.exam.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteDialogFragment extends DialogFragment {
    private ConfirmDeleteDialogListener callback;

    public interface ConfirmDeleteDialogListener {
        void onPositiveDialogClick(DialogFragment dialog);

        void onNegativeDialogClick(DialogFragment dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setMessage("Delete everything?")
                .setPositiveButton(android.R.string.ok,
                        (dialog1, which) ->
                                callback.onPositiveDialogClick(ConfirmDeleteDialogFragment.this))
                .setNegativeButton(android.R.string.cancel,
                        (dialog2, which) ->
                                callback.onNegativeDialogClick(ConfirmDeleteDialogFragment.this))
                .create();
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (ConfirmDeleteDialogListener) context;
        } catch (ClassCastException e) {
            String.format(
                    "%s must implements ConfirmDeleteDialogListener", context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
