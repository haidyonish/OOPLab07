package functions.basic;

import functions.Function;

/**
 * Абстрактный класс для тригонометрических функций одной переменной.
 * Задаёт общие границы области определения для всех функций семейства.
 * Конкретные функции должны реализовать метод {@link #getFunctionValue(double)}.
 */
public abstract class TrigonometricFunction implements Function {
    @Override
    public abstract double getFunctionValue(double x);

    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}
