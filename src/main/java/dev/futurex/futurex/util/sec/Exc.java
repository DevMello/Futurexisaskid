package dev.futurex.futurex.util.sec;

public class Exc extends RuntimeException {

    public Exc(String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
