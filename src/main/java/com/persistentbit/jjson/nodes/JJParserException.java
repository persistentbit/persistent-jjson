package com.persistentbit.jjson.nodes;

/**
 * Represents an exception that occured while parsing a json file
 * @author Peter Muys
 * @since 29/08/2016
 * @see JJParser
 */
public class JJParserException extends RuntimeException{
    private final int row;
    private final int col;

    public JJParserException(int row, int col,String message){
        super("Parsing error at row " + row + " col " + col + ": " + message);
        this.row = row;
        this.col = col;
    }
    public JJParserException(int row, int col,String message,Throwable cause){
        super("Parsing error at row " + row + " col " + col + ": " + message,cause);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
