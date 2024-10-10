package com.afundacion.fp.clips;

import org.json.JSONException;
import org.json.JSONObject;

public class Clip {
    private int id;
    private String videoTitle;
    private String videoUrl;

    public String getVideoTitle() {
        return videoTitle;
    }

    public Clip(JSONObject json) throws JSONException {
        this.id = json.getInt("id");
        this.videoTitle = json.getString("title");
        this.videoUrl = json.getString("videoUrl");
    }
}
