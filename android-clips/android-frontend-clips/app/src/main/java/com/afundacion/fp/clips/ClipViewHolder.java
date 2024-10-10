package com.afundacion.fp.clips;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClipViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private TextView clipTitle;
    private Clip clip;


    public TextView getClipTitle() {
        return clipTitle;
    }

    public ClipViewHolder(@NonNull View itemView) {

        super(itemView);
        clipTitle = itemView.findViewById(R.id.textview);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clipId = clip.getId();
                Context context = view.getContext();
                Toast.makeText(context, "Touched cell with clipId: " + clipId, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void bindClip(Clip clip) {
        this.clipTitle.setText(clip.getVideoTitle());
        this.clip = clip;
    }

}
