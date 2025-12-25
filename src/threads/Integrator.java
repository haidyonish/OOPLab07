package threads;

import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount() && task.isRunning(); i++) {
                semaphore.startRead();
                if (Thread.interrupted()) {
                    task.stopRunning();
                    return;
                }
                double result = Functions.integrate(
                        task.getFunction(),
                        task.getLeftX(),
                        task.getRightX(),
                        task.getStep()
                );
                System.out.printf("Integrator: Result %.5f %.5f %.5f %.10f%n",
                        task.getLeftX(), task.getRightX(), task.getStep(), result);
                semaphore.endRead();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    task.stopRunning();
                    return;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Integrator был прерван");
            task.stopRunning();
        }
    }
}