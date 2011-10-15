package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class StringModel {
    public static StringModel createModel(){
        StringModel m = new StringModel();
        m.setHello("hello");
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public String getHello() {
        return hello;
    }
    public void setHello(String stringValue) {
        this.hello = stringValue;
    }
    public String getSurrogate() {
        return surrogate;
    }
    public void setSurrogate(String surrogate) {
        this.surrogate = surrogate;
    }
    public String getJapanese() {
        return japanese;
    }
    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }
    public String getJapanese_hiragana() {
        return japanese_hiragana;
    }
    public void setJapanese_hiragana(String japanese_hiragana) {
        this.japanese_hiragana = japanese_hiragana;
    }
    public String getJapanese_katakana() {
        return japanese_katakana;
    }
    public void setJapanese_katakana(String japanese_katakana) {
        this.japanese_katakana = japanese_katakana;
    }

    @Attribute(primaryKey=true)
    private Key key;
    private String hello = "Hello, World.";
    private String japanese = "お早うございます。行ってらっしゃい。";
    private String japanese_hiragana = "あいうえおかきくけこわおん";
    private String japanese_katakana = "アイウエオカキクケコヤユヨ";
    private String surrogate = "叱";
}
