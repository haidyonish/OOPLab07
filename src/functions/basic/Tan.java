package functions.basic;

/**
 * Класс, представляющий тангенс функции.
 * Вычисляет значение tan(x) для заданного x.
 */
public class Tan extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        return Math.tan(x);
    }
}
