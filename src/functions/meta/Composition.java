package functions.meta;

import functions.Function;

/**
 * Класс для представления композиции двух функций.
 * Результат вычисления функции f(g(x)), где function1 = f, function2 = g.
 */
public class Composition implements Function {
    private final Function function1;
    private final Function function2;

    /**
     * Создаёт объект композиции двух функций.
     *
     * @param function1 внешняя функция f
     * @param function2 внутренняя функция g
     */
    public Composition(Function function1, Function function2) {
        this.function1 = function1;
        this.function2 = function2;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < function2.getLeftDomainBorder() || x > function2.getRightDomainBorder()) {
            return Double.NaN;
        }
        double arg = function2.getFunctionValue(x);
        if (arg < getLeftDomainBorder() || arg > getRightDomainBorder()) {
            return Double.NaN;
        }
        return function1.getFunctionValue(arg);
    }

    @Override
    public double getLeftDomainBorder() {
        return function1.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return function1.getRightDomainBorder();
    }
}
