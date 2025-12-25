package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;
    private final Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            synchronized (task) {
                double base = 1 + 9 * random.nextDouble();
                double leftX = 100 * random.nextDouble();
                double rightX = 100 + 100 * random.nextDouble();
                double step = random.nextDouble();
                if (step < 1e-6) step = 1e-6;

                task.setFunction(new Log(base));
                task.setLeftX(leftX);
                task.setRightX(rightX);
                task.setStep(step);

                System.out.printf("Source %.5f %.5f %.5f%n", leftX, rightX, step);
            }
            try { Thread.sleep(2); } catch (InterruptedException e) {}
        }
    }
}