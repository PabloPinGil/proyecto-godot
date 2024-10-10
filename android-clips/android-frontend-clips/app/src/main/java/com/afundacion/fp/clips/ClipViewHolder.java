package com.afundacion.fp.clips;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClipViewHolder extends RecyclerView.ViewHolder {
    private TextView clipTitle;

    public TextView getClipTitle() {
        return clipTitle;
    }

    public ClipViewHolder(@NonNull View itemView) {
        super(itemView);
        clipTitle = itemView.findViewById(R.id.textview);
    }

    public void bindClip(Clip clip) {
        this.clipTitle.setText(clip.getVideoTitle());
    }

}
