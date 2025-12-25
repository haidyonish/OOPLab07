package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            double leftX, rightX, step, result;

            synchronized (task) {
                leftX = task.getLeftX();
                rightX = task.getRightX();
                step = task.getStep();
                result = Functions.integrate(
                        task.getFunction(), leftX, rightX, step
                );
            }

            System.out.printf("Result %.5f %.5f %.5f %.10f%n", leftX, rightX, step, result);
            try { Thread.sleep(2); } catch (InterruptedException e) {}
        }
    }
}