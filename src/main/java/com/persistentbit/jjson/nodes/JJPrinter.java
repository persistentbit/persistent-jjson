package com.persistentbit.jjson.nodes;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author Peter Muys
 * @since 22/10/2015
 */
public class JJPrinter
{
    private Writer out;
    private boolean pretty;
    private String indent = "";
    private boolean needIndent = true;

    private JJPrinter(Writer out, boolean pretty)
    {
        this.out = out;
        this.pretty = pretty;
    }

    static public void print(boolean pretty,JJNode node,Writer out){
        new JJPrinter(out,pretty).print(node);
    }
    static public String print(boolean pretty, JJNode node){
        StringWriter sw = new StringWriter();
        print(pretty,node,sw);
        return sw.toString();
    }
    private void println(String line){
        print(line);
        if(pretty)
        {
            print("\r\n");
        }
        needIndent = true;
    }
    private void print(String txt){
        try
        {
            if(pretty && needIndent && txt.length()>0)
            {
                out.write(indent);
                needIndent = false;
            }
            out.write(txt);

        }catch(Exception e){
            throw new RuntimeException("Exception in JJPrinter",e);
        }
    }
    private void indent() {
        indent += "\t";
    }
    private void outdent() {
        indent = indent.substring(1);
    }

    private void print(JJNode node){
        switch(node.getType()){
            case jsonArray: printArray((JJNodeArray) node);break;
            case jsonBoolean: printBoolean((JJNodeBoolean)node);break;
            case jsonNull: print("null");break;
            case jsonNumber: printNumber((JJNodeNumber)node);break;
            case jsonObject: printObject((JJNodeObject)node);break;
            case jsonString: print(jsonString(((JJNodeString)node).getValue()));break;
            default:
                throw new RuntimeException("Unknown:" + node.getType());
        }
    }
    private void printArray(JJNodeArray node){
        if(node.getValue().isEmpty()){
            print("[]");
            return;
        }
        println("[");
        indent();
        boolean first = true;
        for(JJNode element : node){
            if(first) { first = false; } else { println(",");}
            print(element);
        }
        println("");
        outdent();
        print("]");
    }
    private void printBoolean(JJNodeBoolean node){
        print(Boolean.toString(node.getValue()));
    }
    private void printNumber(JJNodeNumber node){
        print(node.toString());
    }
    private void printObject(JJNodeObject object){
        if(object.getValue().isEmpty()){
            print("{}");
            return;
        }
        println("{");
        indent();
        boolean first = true;
        for(Map.Entry<String,JJNode> entry : object.getValue().entrySet()){
            if(first) { first = false; } else { println(",");}
            print(jsonString(entry.getKey()));
            if(pretty)
            {
                print(": ");
            } else
            {
                print(":");
            }
            print(entry.getValue());
        }
        println("");
        outdent();
        print("}");
    }

    private String jsonString(String s){
        StringBuilder sb = new StringBuilder(s.length()+4);
        sb.append('\"');
        for(int t=0; t<s.length();t++){
            char c = s.charAt(t);
            if(c == '\t'){
                sb.append("\\t");
            } else if(c == '\n'){
                sb.append("\\n");
            } else if(c == '\r'){
                sb.append("\\r");
            } else if(c == '\\'){
                sb.append('\\');
            } else if (c == '\b'){
                sb.append("\\b");
            } else if(c == '\"'){
                sb.append("\\\"");
            } else {
                sb.append(c);
            }
        }
        sb.append('\"');
        return sb.toString();
    }

}
