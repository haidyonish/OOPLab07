package functions;

public interface Function {
    /**
     * Возвращает значение функции в заданной точке {@code x}.
     * Если {@code x} выходит за пределы области определения, возвращает {@code Double.NaN}
     *
     * @param x значение аргумента
     * @return значение функции в точке {@code x}. {@code Double.NaN}
     */
    double getFunctionValue(double x);

    /**
     * Возвращает левую границу области определения
     *
     * @return минимальный X
     */
    double getLeftDomainBorder();

    /**
     * Возвращает правую границу области определения
     *
     * @return максимальный X
     */
    double getRightDomainBorder();
}
