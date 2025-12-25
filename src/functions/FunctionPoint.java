package functions;

/**
 * Класс, представляющий точку на плоскости с координатами (x, y).
 * Используется для описания точек табулированной функции.
 *
 * @author haidyonish
 * @version 1.0
 * @see TabulatedFunction
 */
public class FunctionPoint {
    private double x, y;

    /**
     * Создает точку с заданными координатами.
     *
     * @param x координата по оси абсцисс
     * @param y координата по оси ординат
     */
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Создает копию существующей точки.
     * Конструктор копирования обеспечивает создание независимой копии объекта.
     *
     * @param point точка для копирования (не должна быть null)
     */
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    /**
     * Создает точку в начале координат (0, 0).
     * Конструктор по умолчанию для удобства создания точек с нулевыми координатами.
     */
    public FunctionPoint() {
        x = 0;
        y = 0;
    }

    /**
     * Возвращает координату X точки.
     *
     * @return координата по оси абсцисс
     */
    public double getX() {
        return x;
    }

    /**
     * Возвращает координату Y точки.
     *
     * @return координата по оси ординат
     */
    public double getY() {
        return y;
    }

    /**
     * Устанавливает новое значение координаты X.
     *
     * @param x новое значение координаты по оси абсцисс
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Устанавливает новое значение координаты Y.
     *
     * @param y новое значение координаты по оси ординат
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление точки в виде "(x; y)".
     *
     * @return строковое представление точки
     */
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    /**
     * Сравнивает текущую точку с другим объектом.
     * Точки считаются равными, если разность их координат
     * по модулю меньше заданного эпсилона.
     *
     * @param o объект для сравнения
     * @return {@code true}, если объекты равны, иначе {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionPoint point)) {
            return false;
        }

        return Math.abs(x - point.x) < 1e-10 && Math.abs(y - point.y) < 1e-10;
    }

    /**
     * Возвращает хэш-код точки.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int hash = Long.hashCode(xBits);
        hash ^= Long.hashCode(yBits);

        return hash;
    }

    /**
     * Создаёт и возвращает копию точки.
     *
     * @return клон объекта точки
     */
    @Override
    public Object clone() {
        return new FunctionPoint(this);
    }

}