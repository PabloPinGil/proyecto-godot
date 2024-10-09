package com.afundacion.fp.clips;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClipViewHolder extends RecyclerView.ViewHolder {
    private TextView clipTitle;

    public ClipViewHolder(@NonNull View itemView) {
        super(itemView);
        clipTitle = itemView.findViewById(R.id.textview);
    }
}
