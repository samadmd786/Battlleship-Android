package edu.msu.leemyou1.project1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TurnResult extends DialogFragment {

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final AlertDialog dlg = builder.create();
        return dlg;
    }
}
