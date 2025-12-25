package functions.basic;

import functions.Function;

/**
 * Класс реализует функцию экспоненты {@code f(x) = e^x}.
 *
 * Область определения функции: {@code (-∞, +∞)}.
 * Область значений: {@code (0, +∞)}.
 */
public class Exp implements Function {

    @Override
    public double getFunctionValue(double x) {
        return Math.exp(x);
    }

    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}