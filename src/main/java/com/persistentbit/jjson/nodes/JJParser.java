package com.persistentbit.jjson.nodes;

import com.persistentbit.core.collections.POrderedMap;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse a JSON stream into a {@link JJNode} structure
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


    /**
     * Read and parse json from the provided file
     * @param file The file to read
     * @return The {@link JJNode} representing the json from the file
     */
    static public JJNode parse(File file){
        try(FileReader fr = new FileReader(file)){
            return parse(fr);
        }catch (IOException e){
            throw new JJParserException(1,1,"IO exception",e);
        }
    }

    /**
     * Read and parse json from the provided reader.<br>
     * <strong>Warning: This does not close the Reader</strong>
     * @param r The {@link Reader}
     * @return The {@link JJNode} read from the json stream;
     */
    static public JJNode parse(Reader r){
        return new JJParser(r).parse();
    }
    /**
     * Read and parse json from the provided string.<br>
     * @param str The json string to read
     * @return The {@link JJNode} read from the json stream;
     */
    static public JJNode parse(String str){
        return new JJParser(new StringReader(str)).parse();
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
                throw new JJParserException(row,col,"Expected ':' while parsing json object property '" + name + "'");
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
            throw new JJParserException(row,col,"Expected a number, not '" + sb.toString() +"'");
        }
    }

    private String readString() {
        StringBuilder sb = new StringBuilder(10);
        if(current() != '\"'){
            throw new JJParserException(row,col,"Expected a string, not '" + current() + "'");
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
                        throw new JJParserException(row,col,"Invalid escape sequence: \\" + c );
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
            throw new JJParserException(row,col, "Expected '" + checkCurrentCharValue + "', not '" + c + "'");
        }
        return next();
    }

    private char next(){
        if(c == -1){
            throw new JJParserException(row,col,"Unexpected end-of-stream");
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
            throw new JJParserException(row,col,"Error parsing json",e);
        }

    }
    private boolean eof() {
        return c == -1;
    }
    private char current() {
        return (char)c ;
    }
}
