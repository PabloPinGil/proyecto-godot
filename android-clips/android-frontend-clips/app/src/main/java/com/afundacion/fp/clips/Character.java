package com.afundacion.fp.clips;

import org.json.JSONException;
import org.json.JSONObject;

public class Character {
    private String name;
    private String surname;
    private String characterDescription;
    private String urlImage;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public Character(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.surname = json.getString("surname");
        this.characterDescription = json.getString("characterDescription");
        this.urlImage = json.getString("urlImage");
    }
}
