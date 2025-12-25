package functions.basic;

/**
 * Класс, представляющий косинус функции.
 * Вычисляет значение cos(x) для заданного x.
 */
public class Cos extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        return Math.cos(x);
    }
}
