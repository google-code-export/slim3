package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.json.Expanded;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class InverseModelListRefModel {
    public int getIntValue() {
        return intValue;
    }
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public ModelRef<InverseModelListRefModel> getParent() {
        return parent;
    }
    public InverseModelListRef<InverseModelListRefModel, InverseModelListRefModel> getChildren() {
        return children;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private int intValue;
    @Json(coder=Expanded.class)
    private ModelRef<InverseModelListRefModel> parent
        = new ModelRef<InverseModelListRefModel>(InverseModelListRefModel.class);
    @Attribute(persistent=false)
    @Json(coder=Expanded.class)
    private InverseModelListRef<InverseModelListRefModel, InverseModelListRefModel> children
        = new InverseModelListRef<InverseModelListRefModel, InverseModelListRefModel>(
                InverseModelListRefModel.class, "parent", this);
}
