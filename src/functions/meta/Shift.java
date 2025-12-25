package functions.meta;

import functions.Function;

/**
 * Класс для представления функции, полученной сдвигом исходной функции вдоль осей координат.
 * shiftX сдвигает функцию вдоль оси X,
 * shiftY сдвигает функцию вдоль оси Y.
 */
public class Shift implements Function {
    private final Function function;
    private final double shiftX;
    private final double shiftY;

    /**
     * Создаёт объект функции, полученной сдвигом исходной функции.
     *
     * @param function исходная функция
     * @param shiftX сдвиг по оси X
     * @param shiftY сдвиг по оси Y
     */
    public Shift(Function function, double shiftX, double shiftY) {
        this.function = function;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return function.getFunctionValue(x - shiftX) + shiftY;
    }

    @Override
    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder() - shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        return function.getRightDomainBorder() - shiftX;
    }
}
