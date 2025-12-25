package threads;

public class Semaphore {
    private boolean canWrite = true;

    public synchronized void startWrite() throws InterruptedException {
        while (!canWrite) {
            wait();
        }
    }

    public synchronized void endWrite() {
        canWrite = false;
        notifyAll();
    }

    public synchronized void startRead() throws InterruptedException {
        while (canWrite) {
            wait();
        }
    }

    public synchronized void endRead() {
        canWrite = true;
        notifyAll();
    }
}