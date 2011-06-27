package org.slim3.json.test;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.slim3.datastore.json.Default;
import org.slim3.datastore.json.JsonReader;
import org.slim3.datastore.json.JsonWriter;

public class CustomAttributesModelCoder extends Default{
    public void encode(JsonWriter writer, Point value) {
        writer.beginObject();
        writer.writeValueProperty("x", value.x);
        writer.writeValueProperty("y", value.y);
        writer.endObject();
    }

    public void encode(JsonWriter writer, Point2D value) {
        writer.beginObject();
        writer.writeValueProperty("x", value.getX());
        writer.writeValueProperty("y", value.getY());
        writer.endObject();
    }

    public Point2D decode(JsonReader reader, Point2D defaultValue, Class<Point2D> clazz){
        try{
            float x = Float.parseFloat(reader.readProperty("x"));
            float y = Float.parseFloat(reader.readProperty("y"));
            return new Point2D.Float(x, y);
        } catch(NumberFormatException e){
        }
        return defaultValue;
    }

    public Point decode(JsonReader reader, Point defaultValue, Class<Point> clazz){
        try{
            int x = Integer.parseInt(reader.readProperty("x"));
            int y = Integer.parseInt(reader.readProperty("y"));
            return new Point(x, y);
        } catch(NumberFormatException e){
        }
        return defaultValue;
    }
}
