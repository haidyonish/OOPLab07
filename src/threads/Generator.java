package threads;

import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;
    private final Random random = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount() && task.isRunning(); i++) {
                semaphore.startWrite();
                if (Thread.interrupted()) {
                    task.stopRunning();
                    return;
                }
                double base = 1 + 9 * random.nextDouble();
                double leftX = 100 * random.nextDouble();
                double rightX = 100 + 100 * random.nextDouble();
                double step = random.nextDouble();
                if (step < 1e-6) step = 1e-6;

                task.setFunction(new Log(base));
                task.setLeftX(leftX);
                task.setRightX(rightX);
                task.setStep(step);
                System.out.printf("Generator: Source %.5f %.5f %.5f%n", leftX, rightX, step);
                semaphore.endWrite();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    task.stopRunning();
                    return;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Generator был прерван");
            task.stopRunning();
        }
    }
}