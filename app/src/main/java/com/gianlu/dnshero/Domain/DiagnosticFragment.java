package com.gianlu.dnshero.Domain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gianlu.commonutils.CasualViews.RecyclerMessageView;
import com.gianlu.commonutils.Dialogs.DialogUtils;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.ArrayList;

public class DiagnosticFragment extends Fragment {

    @NonNull
    public static DiagnosticFragment getInstance(@NonNull Context context, @NonNull Domain domain) {
        DiagnosticFragment fragment = new DiagnosticFragment();
        fragment.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.diagnostic));
        args.putSerializable("diagnostics", domain.diagnostics);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.diagnostic, menu);
    }

    private void showHelpDialog() {
        if (getContext() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.help)
                .setView(R.layout.dialog_diagnostic_help)
                .setPositiveButton(android.R.string.ok, null);

        DialogUtils.showDialog(getActivity(), builder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.diagnostic_help:
                showHelpDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerMessageView layout = new RecyclerMessageView(requireContext());
        layout.disableSwipeRefresh();

        Bundle args = getArguments();
        ArrayList<Domain.Diagnostic> diagnostics;
        if (args == null || (diagnostics = (ArrayList<Domain.Diagnostic>) args.getSerializable("diagnostics")) == null) {
            layout.showError(R.string.failedLoading);
            return layout;
        }

        layout.linearLayoutManager(RecyclerView.VERTICAL, false);
        layout.loadListData(new DiagnosticsAdapter(requireContext(), diagnostics));

        return layout;
    }
}
