package functions.basic;

/**
 * Класс, представляющий синус функции.
 * Вычисляет значение sin(x) для заданного x.
 */
public class Sin extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}
