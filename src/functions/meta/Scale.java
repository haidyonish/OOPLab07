package functions.meta;

import functions.Function;

/**
 * Класс для представления функции, полученной масштабированием исходной функции вдоль осей координат.
 * Масштабирование по X влияет на аргумент функции, масштабирование по Y — на значение функции.
 * Отрицательный scaleX отражает функцию относительно оси Y.
 * scaleX не может быть равен нулю, иначе выбрасывается IllegalArgumentException.
 */
public class Scale implements Function {
    private final Function function;
    private final double scaleX;
    private final double scaleY;

    /**
     * Создаёт объект функции, полученной масштабированием исходной функции.
     *
     * @param function исходная функция
     * @param scaleX коэффициент масштабирования по оси X (не равен нулю)
     * @param scaleY коэффициент масштабирования по оси Y
     * @throws IllegalArgumentException если scaleX равен нулю
     */
    public Scale(Function function, double scaleX, double scaleY) {
        if (scaleX == 0) {
            throw new IllegalArgumentException("scaleX не может равняться нулю");
        }
        this.function = function;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return function.getFunctionValue(x / scaleX) * scaleY;
    }

    @Override
    public double getLeftDomainBorder() {
        if (scaleX < 0) {
            return function.getRightDomainBorder() * scaleX;
        }
        return function.getLeftDomainBorder() * scaleX;
    }

    @Override
    public double getRightDomainBorder() {
        if (scaleX < 0) {
            return function.getLeftDomainBorder() * scaleX;
        }
        return function.getRightDomainBorder() * scaleX;
    }
}
