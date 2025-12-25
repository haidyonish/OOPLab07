package functions;

import java.io.*;
import java.util.Iterator;

/**
 * Класс для работы с табулированными функциями одной переменной.
 * Функция задаётся таблицей точек, упорядоченных по координате X.
 * Для создания точек используется класс FunctionPoint
 */
public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }

    private FunctionPoint[] points;
    private int size;

    // Конструктор без параметров для сериализации.
    public ArrayTabulatedFunction() {}

    /**
     * Создаёт табулированную функцию с равномерной сеткой и нулевыми значениями.
     *
     * @param leftX       левая граница области определения (меньше правой)
     * @param rightX      правая граница области определения
     * @param pointsCount количество точек табулирования (не менее двух)
     * @throws IllegalArgumentException если левая граница >= правой или точек < 2
     */
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница области определения leftX должна быть строго меньше правой границы rightX");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Количество точек pointsCount должно быть не меньше двух");

        size = pointsCount;
        points = new FunctionPoint[size];
        double step = (rightX - leftX) / (size - 1);
        for (int i = 0; i < size - 1; i++) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }
        points[size - 1] = new FunctionPoint(rightX, 0);
    }

    /**
     * Создаёт табулированную функцию с равномерной сеткой и заданными значениями.
     *
     * @param leftX  левая граница области определения (меньше правой)
     * @param rightX правая граница области определения
     * @param values значения функции в точках табулирования (не менее двух)
     * @throws IllegalArgumentException если границы некорректны или значений < 2
     */
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница области определения leftX должна быть строго меньше правой границы rightX");
        if (values.length < 2)
            throw new IllegalArgumentException("Количество значений функции в массиве values должно быть не меньше двух");

        size = values.length;
        points = new FunctionPoint[size];
        double step = (rightX - leftX) / (size - 1);
        for (int i = 0; i < size - 1; i++) {
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
        points[size - 1] = new FunctionPoint(rightX, values[size - 1]);
    }

    /**
     * Создаёт табулированную функцию по массиву точек {@code points}.
     *
     * @param points массив точек функции (длина не менее двух, X строго возрастают)
     * @throws IllegalArgumentException если массив содержит меньше двух точек или точки не упорядочены по координате X
     */
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2)
            throw new IllegalArgumentException("Количество точек pointsCount должно быть не меньше двух");
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() > points[i+1].getX() - 1e-10) {
                throw new IllegalArgumentException("Точки в массиве должны быть упорядочены по координате X");
            }
        }

        size = points.length;
        this.points = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        return points[size - 1].getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            for (int i = 1; i < size; i++) {
                if (x <= points[i].getX() + 1e-10) {
                    if (Math.abs(x - points[i].getX()) < 1e-10) {
                        return points[i].getY();
                    }
                    double y1 = points[i - 1].getY();
                    double y2 = points[i].getY();
                    double x1 = points[i - 1].getX();
                    double x2 = points[i].getX();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount() {
        return size;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        checkIndexBounds(index);
        return new FunctionPoint(points[index]);
    }

    @Override
    public double getPointX(int index) {
        checkIndexBounds(index);
        return points[index].getX();
    }

    @Override
    public double getPointY(int index) {
        checkIndexBounds(index);
        return points[index].getY();
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndexBounds(index);
        checkPointOrder(index, point.getX());
        points[index] = new FunctionPoint(point);
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndexBounds(index);
        checkPointOrder(index, x);
        points[index].setX(x);
    }

    @Override
    public void setPointY(int index, double y) {
        checkIndexBounds(index);
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) {
        checkIndexBounds(index);
        if (size < 3) {
            throw new IllegalStateException("Нельзя удалить точку из функции, содержащей меньше трёх точек.");
        }
        System.arraycopy(points, index + 1, points, index, size - index - 1);
        size--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double pointX = point.getX();

        for (int i = 0; i < size; i++) {
            if (Math.abs(points[i].getX() - pointX) < 1e-10) {
                throw new InappropriateFunctionPointException(
                        String.format("Абсциссы точек не могут совпадать. (Точка с координатой X = %.2f уже есть в функции)", pointX)
                );
            }
        }

        if (size == points.length) {
            FunctionPoint[] tempPoints = new FunctionPoint[size * 2];
            System.arraycopy(points, 0, tempPoints, 0, size);
            points = tempPoints;
        }

        if (point.getX() > getRightDomainBorder()) {
            points[size] = new FunctionPoint(point);
        } else {
            for (int i = 0; i < size; i++) {
                if (point.getX() < points[i].getX()) {
                    System.arraycopy(points, i, points, i + 1, size - i);
                    points[i] = new FunctionPoint(point);
                    break;
                }
            }
        }
        size++;
    }

    // ───────────────────────────────
    // Итератор
    // ───────────────────────────────

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }
            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException("Нет следующего элемента");
                }
                return new FunctionPoint(points[index++]);
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается");
            }
        };
    }

    // ───────────────────────────────
    // Переопределение методов Object
    // ───────────────────────────────

    /**
     * Возвращает строковое представление табулированной функции.
     *
     * @return строковое представление функции
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (int i = 0; i < size; i++) {
            sb.append(points[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Сравнивает текущую функцию с другим объектом.
     * Функции считаются равными, если совпадает количество точек
     * и все соответствующие точки равны.
     *
     * @param o объект для сравнения
     * @return {@code true}, если функции равны, иначе {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabulatedFunction other)) {
            return false;
        }

        if (size != other.getPointsCount()) {
            return false;
        }

        if (o instanceof ArrayTabulatedFunction arrayFunc) {
            for (int i = 0; i < size; i++) {
                if (!points[i].equals(arrayFunc.points[i])) {
                    return false;
                }
            }
            return true;
        }

        for (int i = 0; i < size; i++) {
            if (!points[i].equals(other.getPoint(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Возвращает хэш-код табулированной функции.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        int hash = size;

        for (int i = 0; i < size; i++) {
            hash ^= points[i].hashCode();
        }

        return hash;
    }

    /**
     * Создаёт и возвращает глубокую копию табулированной функции.
     *
     * @return клон объекта функции
     */
    @Override
    public Object clone() {
        FunctionPoint[] pointsCopy = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            pointsCopy[i] = (FunctionPoint) points[i].clone();
        }
        return new ArrayTabulatedFunction(pointsCopy);
    }

    // ────────────────────────────
    // Методы для сериализации
    // ────────────────────────────

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        size = in.readInt();
        points = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    // ────────────────────────────
    // Вспомогательные методы
    // ────────────────────────────

    // Метод для проверки корректности индекса.
    // (0 <= index < size)
    private void checkIndexBounds(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(
                    String.format("Индекс точки должен быть не меньше нуля и меньше количества точек в функции. (На данный момент количество точек в функции - %d)", size)
            );
        }
    }

    // Метод для проверки корректности координаты X точки массива с индексом index при попытке её изменения.
    // (новая координата X не должна выходить за границы координат X соседних точек)
    private void checkPointOrder(int index, double pointX) throws InappropriateFunctionPointException {
        if (index == 0) {
            if (pointX >= getPointX(1) - 1e-10) {
                throw new InappropriateFunctionPointException(
                        String.format("Координата x должна быть меньше %.2f (при индексе %d)", getPointX(index + 1), index)
                );
            }
        } else if (index == size - 1) {
            if (pointX <= getPointX(index - 1) + 1e-10) {
                throw new InappropriateFunctionPointException(
                        String.format("Координата x должна быть больше %.2f (при индексе %d)", getPointX(index - 1), index)
                );
            }
        } else if (pointX <= getPointX(index - 1) + 1e-10 || pointX >= getPointX(index + 1) - 1e-10) {
            throw new InappropriateFunctionPointException(
                    String.format("Координата x должна лежать между %.2f и %.2f (при индексе %d)", getPointX(index - 1), getPointX(index + 1), index)
            );
        }
    }
}
