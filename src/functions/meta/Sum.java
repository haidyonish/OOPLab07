package functions.meta;

import functions.Function;

/**
 * Класс для представления суммы двух функций.
 */
public class Sum implements Function {
    private final Function function1;
    private final Function function2;

    /**
     * Создаёт объект функции, являющейся суммой двух функций.
     *
     * @param function1 первая функция
     * @param function2 вторая функция
     */
    public Sum(Function function1, Function function2) {
        this.function1 = function1;
        this.function2 = function2;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return function1.getFunctionValue(x) + function2.getFunctionValue(x);
    }

    @Override
    public double getLeftDomainBorder() {
        return Math.max(function1.getLeftDomainBorder(), function2.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Math.min(function1.getRightDomainBorder(), function2.getRightDomainBorder());
    }
}
