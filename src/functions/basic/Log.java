package functions.basic;

import functions.Function;

/**
 * Класс реализует логарифмическую функцию {@code f(x) = log_base(x)}.
 *
 * Область определения функции: {@code (0, +∞)}.
 * Область значений: {@code (-∞, +∞)}.
 */
public class Log implements Function {
    private final double base;

    /**
     * Создаёт логарифмическую функцию с заданным основанием.
     *
     * @param base основание логарифма (не 0 и не 1)
     * @throws IllegalArgumentException если основание равно 0 или 1
     */
    public Log(double base) {
        if (base < 1e-10 || Math.abs(base - 1) < 1e-10) {
            throw new IllegalArgumentException("Основание логарифма не должно равняться нулю или единице");
        }
        this.base = base;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        return Math.log(x) / Math.log(base);
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}
