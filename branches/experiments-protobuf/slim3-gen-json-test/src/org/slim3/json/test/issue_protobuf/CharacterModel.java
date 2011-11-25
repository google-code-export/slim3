package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class CharacterModel {
    public Character getCharacterValue() {
        return characterValue;
    }
    public void setCharacterValue(Character characterValue) {
        this.characterValue = characterValue;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    @Attribute(primaryKey=true)
    private Key key;
    @Attribute(lob=true)
    private Character characterValue;
}
