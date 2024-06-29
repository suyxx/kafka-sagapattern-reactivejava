package org.suyash.inventory.common.exception;

public class OutOfStockException extends RuntimeException{
    private static final String Message = "Out Of Stock";

    public OutOfStockException(){
        super(Message);
    }
}
