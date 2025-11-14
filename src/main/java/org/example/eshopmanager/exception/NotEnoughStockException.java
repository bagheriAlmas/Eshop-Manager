package org.example.eshopmanager.exception;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(int requested, int available) {
        super("Requested=" + requested + " but available=" + available);
    }
}
