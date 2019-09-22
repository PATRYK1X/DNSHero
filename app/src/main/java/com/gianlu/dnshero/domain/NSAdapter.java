package com.gianlu.dnshero.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.misc.SuperTextView;
import com.gianlu.dnshero.GlueView;
import com.gianlu.dnshero.R;
import com.gianlu.dnshero.SourceView;
import com.gianlu.dnshero.Utils;
import com.gianlu.dnshero.api.DNSRecord;
import com.gianlu.dnshero.api.Domain;

import java.util.List;

public class NSAdapter extends RecyclerView.Adapter<NSAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private final List<Domain.NS> authoritative;

    NSAdapter(Context context, List<Domain.NS> authoritative) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.authoritative = authoritative;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Domain.NS authoritative = this.authoritative.get(position);

        holder.source.setText(authoritative.source);
        holder.rtt.setHtml(R.string.rtt, Utils.formatRTT(authoritative.rtt));
        holder.glue.setGlue(authoritative.glue);

        if (authoritative.a.records.isEmpty()) {
            holder.a.setHtml(R.string.aRecord, context.getString(R.string.absentLowercase));
            holder.aTtl.setVisibility(View.GONE);
        } else {
            DNSRecord.AEntry entry = authoritative.a.records.get(0);
            holder.a.setHtml(R.string.aRecord, entry.address);
            Utils.clickToCopy(holder.a, entry.address);

            holder.aTtl.setVisibility(View.VISIBLE);
            holder.aTtl.setHtml(R.string.aTtl, entry.ttl);
        }

        if (authoritative.aaaa.records.isEmpty()) {
            holder.aaaa.setHtml(R.string.aaaaRecord, context.getString(R.string.absentLowercase));
            holder.aaaaTtl.setVisibility(View.GONE);
        } else {
            DNSRecord.AEntry entry = authoritative.aaaa.records.get(0);
            holder.aaaa.setHtml(R.string.aaaaRecord, entry.address);
            Utils.clickToCopy(holder.aaaa, entry.address);

            holder.aaaaTtl.setVisibility(View.VISIBLE);
            holder.aaaaTtl.setHtml(R.string.aaaaTtl, entry.ttl);
        }

        holder.aSource.setSource(authoritative.a, false);
        holder.aaaaSource.setSource(authoritative.aaaa, false);

        holder.toggle.setOnClickListener(view -> CommonUtils.handleCollapseClick(holder.toggle, holder.details));

        CommonUtils.setRecyclerViewTopMargin(holder);
    }

    @Override
    public int getItemCount() {
        return authoritative.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView source;
        final SuperTextView rtt;
        final SuperTextView a;
        final SuperTextView aTtl;
        final SuperTextView aaaa;
        final SuperTextView aaaaTtl;
        final GlueView glue;
        final ImageButton toggle;
        final LinearLayout details;
        final SourceView aSource;
        final SourceView aaaaSource;

        ViewHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.card_ns, parent, false));

            source = itemView.findViewById(R.id.nsItem_source);
            rtt = itemView.findViewById(R.id.nsItem_rtt);
            a = itemView.findViewById(R.id.nsItem_a);
            aTtl = itemView.findViewById(R.id.nsItem_aTtl);
            aaaa = itemView.findViewById(R.id.nsItem_aaaa);
            aaaaTtl = itemView.findViewById(R.id.nsItem_aaaaTtl);
            glue = itemView.findViewById(R.id.nsItem_glue);
            toggle = itemView.findViewById(R.id.nsItem_toggle);
            details = itemView.findViewById(R.id.nsItem_details);
            aSource = itemView.findViewById(R.id.nsItem_aSource);
            aaaaSource = itemView.findViewById(R.id.nsItem_aaaaSource);
        }
    }
}
