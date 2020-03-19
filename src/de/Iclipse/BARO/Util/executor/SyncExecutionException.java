package de.Iclipse.BARO.Util.executor;

public class SyncExecutionException extends Exception {
    private final String info;

    public SyncExecutionException(String info) {
        this.info = info;
    }

    public void printInfo() {
        //TODO
        System.out.println("[AtomicsWorld] Reporting... " + info + " Stack Trace is next");
        printStackTrace();
        System.out.println("[AtomicsWorld] Finished reporting");
    }
}
