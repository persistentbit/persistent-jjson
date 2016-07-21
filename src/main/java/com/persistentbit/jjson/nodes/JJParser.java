package com.persistentbit.jjson.nodes;

import com.persistentbit.core.collections.POrderedMap;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peter Muys
 * @since 22/10/2015
 */
public final class JJParser
{
    private final Reader r;
    private int c;
    private int col;
    private int row;
    private JJParser(Reader r){
        this.r = r;
        next();
        row = 1;
        col = 1;
    }




    private JJNode parse() {
        skipSpace();
        switch(current()){
            case '{':   return parseObject();
            case '[':   return parseArray();
            case '\"':  return parseString();
            case 't': return parseTrue();
            case 'f': return parseFalse();
            case 'n': return parseNull();
            default:    return parseNumber();
        }
    }

    static public JJNode parse(Reader r){
        return new JJParser(r).parse();
    }
    static public JJNode parse(String str){
        return new JJParser(new StringReader(str)).parse();
    }

    private String pos() {
        return "(column:" + col + ", row:" + row + ")";
    }

    private JJNodeObject parseObject(){
        POrderedMap<String,JJNode> elements = POrderedMap.empty();
        next(); //skip {
        skipSpace();
        while( current() != '}')
        {
            skipSpace();
            String name = readString();
            skipSpace();
            if (current() != ':')
            {
                throw new RuntimeException("Expected ':' at " + pos());
            }
            next();//skip ":"
            elements = elements.put(name,parse());
            skipSpace();
            if(current() == ','){
                next();//skipt ,
            }
        }
        next(); //Skip }
        return new JJNodeObject(elements);
    }


    private JJNodeArray parseArray() {
        List<JJNode> elements = new ArrayList<>();
        next(); //skip [
        skipSpace();
        while(current() != ']'){
            elements.add(parse());
            skipSpace();
            if(current() == ','){
                next(); //skip ,
            }
            skipSpace();
        }
        next(); //skip ']'
        return new JJNodeArray(elements);
    }

    private JJNodeString parseString() {
        return new JJNodeString(readString());
    }

    private JJNodeBoolean parseTrue() {
        next('t');next('r');next('u');next('e');
        return JJNodeBoolean.True;
    }
    private JJNodeBoolean parseFalse() {
        next('f');next('a');next('l');next('s');next('e');
        return JJNodeBoolean.False;
    }
    private JJNodeNull parseNull() {
        next('n');next('u');next('l');next('l');
        return JJNodeNull.Null;
    }

    private JJNodeNumber parseNumber() {
        StringBuilder sb = new StringBuilder(10);
        char c = current();
        if(c == '-'){ sb.append('-'); c=next(); }
        if(c == '0'){
            sb.append(c);
            c = next();
        } else {
            while(Character.isDigit(c)){
                sb.append(c);
                c = next();
            }
        }
        if(c == '.'){
            sb.append(c);
            c = next();
        }
        while(Character.isDigit(c)){
            sb.append(c);
            c = next();
        }
        if(c == 'e' || c == 'E'){
            sb.append(c);
            c = next();
            if(c == '+' || c == '-'){
                sb.append(c);
                c = next();
            }
            while(Character.isDigit(c)){
                sb.append(c);
                c = next();
            }
        }
        try
        {
            return new JJNodeNumber(new BigDecimal(sb.toString()));
        }catch(NumberFormatException nfe){
            throw new RuntimeException("Expected a number, not '" + sb.toString() +"' at " + pos());
        }
    }

    private String readString() {
        StringBuilder sb = new StringBuilder(10);
        if(current() != '\"'){
            throw new RuntimeException("Expected a string at " + pos() + " not '" + current() + "'");
        }
        char c = next();//skip "
        while(c != '"'){
            if(c == '\\'){
                c = next();//skip \
                switch(c){
                    case '\\': c=next(); sb.append('\\');break;
                    case '\"': c=next(); sb.append('\"');break;
                    case 'b': c=next(); sb.append('\b');break;
                    case 'r': c=next(); sb.append('\r');break;
                    case 'n': c=next(); sb.append('\n');break;
                    case 't': c=next(); sb.append('\t');break;
                    case '/': c=next(); sb.append('/');break;
                    case 'u':
                        c = next();//skip u
                        String hn = Character.toString(c);
                        c = next();
                        hn += c;
                        c = next();
                        hn += c;
                        c = next();
                        hn += c;
                        c = next();
                        sb.append(Character.toChars(Integer.parseInt(hn,16)));
                        break;
                    default:
                        throw new RuntimeException("Invalid escape sequence: \\" + c + " at " + pos());
                }
            } else {

                sb.append(c);
                c = next();
            }
        }
        next(); //skip "
        return sb.toString();
    }

    private void skipSpace() {
        while(eof() == false){
            switch(current()){
                case '\n':
                case '\r':
                case '\t':
                case ' ':
                    next();
                    break;
                default:
                    return;
            }
        }

    }

    private char next(char checkCurrentCharValue){
        if(c != checkCurrentCharValue){
            throw new RuntimeException("Expected '" + checkCurrentCharValue + "', not '" + c + "' at " + pos());
        }
        return next();
    }

    private char next(){
        if(c == -1){
            throw new RuntimeException("Unexpected EOF");
        }
        try
        {
            c = r.read();
            col += 1;
            if(c == '\n'){
                row += 1; col=1;
            }
            if(c == '\r'){
                col = 1;
            }
            if(c == -1){
                return (char)0;
            }
            return (char)c;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error parsing json",e);
        }

    }
    private boolean eof() {
        return c == -1;
    }
    private char current() {
        return (char)c ;
    }
}
