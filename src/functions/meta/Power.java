package functions.meta;

import functions.Function;

/**
 * Класс для представления функции, возведённой в степень
 */
public class Power implements Function {
    private final Function function;
    private final double power;

    /**
     * Создаёт объект функции, являющейся исходной функцией,
     * возведённой в степень {@code power}.
     *
     * @param function исходная функция
     * @param power    показатель степени
     */
    public Power(Function function, double power) {
        this.function = function;
        this.power = power;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return Math.pow(function.getFunctionValue(x), power);
    }

    @Override
    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return function.getRightDomainBorder();
    }
}
