package org.slim3.json.test.issue_protobuf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

public class TestJsGenerator {
    public interface TestWriter{
        void writePropertyAssert(String propertyName, Object expected);
        void writePropertyAssertByReflection(Object model);
    }
    protected void writeTestCases(TestWriter writer, Object model)
    throws IOException{
        writer.writePropertyAssertByReflection(model);
    }

    public void generate(Object model) throws IOException{
        int maxDepth = 1;
        String name = model.getClass().getSimpleName();
        ModelMeta<?> meta = Datastore.getModelMeta(model.getClass());
        {
            final PrintWriter w = new PrintWriter("www/js/test" + name + ".js", "UTF-8");
            try{
                w.println("tests.push({");
                w.println("\tpath: \"data/" + name + ".bin\",");
                w.println("\tonload: function(){");
                w.println("\t\tvar model = " + name + "Meta.readModel(this.responseText);");
                w.println("\t\tappendTitle(\"" + name + "\");");
                final String fmt = "\t\tappendResult(\"%s\", %s, model.%1$s);";
                final String fmtString = "\t\tappendResult(\"%s\", \"%s\", model.%1$s);";
                final String fmtLong = "\t\tappendResult(\"%s\", \"%s\", model.%1$s);";
                final String fmtLongHex = "\t\tappendResult(\"%s(hex)\", \"0x%016x\", longToHexString(model.%1$s));";
                final String fmtModelRef = "\t\tappendNestedResult(\"%s\", %s, model.%1$s);";
                final String fmtInverseModelListRef = "\t\tappendModelArrayResult(\"%s\", %s, model.%1$s);";
                writeTestCases(new TestWriter(){
                    public void writePropertyAssert(String propertyName, Object expected) {
                        if(expected instanceof Collection){
                            StringBuilder b = new StringBuilder("new Array(");
                            boolean first = true;
                            for(Object o : (Collection<?>)expected){
                                if(first){
                                    first = false;
                                } else{
                                    b.append(", ");
                                }
                                b.append(o);
                            }
                            b.append(")");
                            w.println(String.format(fmt, propertyName, b));
                        } else if(
                                (expected instanceof Float)
                                || (expected instanceof Double)){
                            w.println(String.format(fmt, propertyName, String.format(
                                "%1.20g", expected).replaceAll("0+e", "e")
                                        .replaceAll("0+$", "")));
                        } else if(expected instanceof Long){
                            long v = (Long)expected;
                            long h = v & 0xffffffff00000000L;
                            long l = v & 0x00000000ffffffffL;
                            w.println(String.format(fmtLong, propertyName, expected));
                            w.println(String.format(fmtLongHex, propertyName + "_hi32", h));
                            w.println(String.format(fmtLongHex, propertyName + "_low32", l));
                        } else if(expected instanceof Number){
                            w.println(String.format(fmt, propertyName, expected));
                        } else if(expected instanceof Key){
                            w.println(String.format(fmtString, propertyName
                                , Datastore.keyToString((Key)expected)));
                        } else if(expected instanceof ModelRef<?>){
                            ModelRef<?> mr = (ModelRef<?>)expected;
                            Object m = mr.getModel();
                            if(m != null){
                                w.println(String.format(fmtModelRef
                                    , propertyName
                                    , Datastore.getModelMeta(m.getClass()).modelToJson(m, 1)
                                    ));
                            }
                        } else if(expected instanceof InverseModelListRef<?, ?>){
                            InverseModelListRef<?, ?> mr = (InverseModelListRef<?, ?>)expected;
                            List<?> list = mr.getModelList();
                            w.println(String.format(fmtInverseModelListRef
                                , propertyName
                                , Datastore.getModelMeta(mr.getModelClass()).modelsToJson(list.toArray(), 0)
                                ));
                        } else{
                            w.println(String.format(fmtString, propertyName, expected));
                        }
                    }
                    @Override
                    public void writePropertyAssertByReflection(Object model) {
                        for(Method m : model.getClass().getDeclaredMethods()){
                            String name = m.getName();
                            if(name.startsWith("get")
                                    && !m.getReturnType().equals(void.class)
                                    && m.getParameterTypes().length == 0){
                                String propName = name.substring(3, 4).toLowerCase()
                                        + name.substring(4);
                                try {
                                    Object v = m.invoke(model);
                                    if(v != null){
                                        writePropertyAssert(propName, m.invoke(model));
                                    }
                                } catch (IllegalArgumentException e) {
                                } catch (IllegalAccessException e) {
                                } catch (InvocationTargetException e) {
                                }
                            }
                        }
                    }
                }, model);
                w.println("\t}");
                w.println("});");
            } finally{
                w.close();
            }
        }
    
        OutputStream os = new FileOutputStream("www/data/" + name + ".bin");
        try{
            meta.modelToPb(model, os, maxDepth);
        } finally{
            os.close();
        }
        
        PrintWriter w = new PrintWriter("www/js/pb" + name + "Meta.js", "UTF-8");
        try{
            meta.writePbModelMetaJs(w, maxDepth);
        } finally{
            w.close();
        }        
    }
}
