package org.slim3.json.test.issue_protobuf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

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
                        } else if(expected instanceof Float){
                            w.println(String.format(fmt, propertyName, String.format(
                                "\"%1.20g", expected).replaceAll("0+e", "e")
                                        .replaceAll("0+$", "") + "\""));
                        } else{
                            w.println(String.format(fmt, propertyName, "\"" + expected + "\""));
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
            meta.modelToPb(model, os);
        } finally{
            os.close();
        }
        
        PrintWriter w = new PrintWriter("www/js/pb" + name + "Meta.js", "UTF-8");
        try{
            meta.writePbJs(w);
        } finally{
            w.close();
        }        
    }
}
