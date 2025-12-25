package functions;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * Класс содержит вспомогательные статические методы для работы с табулированными функциями.
 * <p>
 * В классе предоставлены методы для:
 * <ul>
 *   <li>Создания табулированного аналога функции на заданном отрезке {@link #tabulate(Function, double, double, int)}</li>
 *   <li>Сохранения и чтения табулированной функции в/из байтового потока
 *       {@link #outputTabulatedFunction(TabulatedFunction, OutputStream)},
 *       {@link #inputTabulatedFunction(InputStream)}</li>
 *   <li>Сохранения и чтения табулированной функции в/из текстового потока
 *       {@link #writeTabulatedFunction(TabulatedFunction, Writer)},
 *       {@link #readTabulatedFunction(Reader)}</li>
 * </ul>
 * <p>
 */
public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {};

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<?> functionClass, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс должен реализовывать интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<?> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс должен реализовывать интерфлекс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс должен реализовывать интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    /**
     * Создаёт табулированный аналог заданной функции на указанном отрезке.
     *
     * @param function   функция, которую необходимо табулировать
     * @param leftX      левая граница отрезка табулирования
     * @param rightX     правая граница отрезка табулирования
     * @param pointsCount количество точек табулирования (не меньше 2)
     * @return объект {@code TabulatedFunction}, представляющий табулированную функцию
     * @throws IllegalArgumentException если:
     *         <ul>
     *           <li>{@code leftX >= rightX}</li>
     *           <li>{@code pointsCount < 2}</li>
     *           <li>указанные границы выходят за область определения функции</li>
     *         </ul>
     */
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница области определения leftX должна быть строго меньше правой границы rightX");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Количество точек pointsCount должно быть не меньше двух");
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException("Указанные границы для табулирования выходят за область определения функции");
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount - 1; i++) {
            points[i] = new FunctionPoint(leftX + i * step, function.getFunctionValue(leftX + i * step));
        }
        points[pointsCount - 1] = new FunctionPoint(rightX, function.getFunctionValue(rightX));
        return factory.createTabulatedFunction(points);
    }

    public static TabulatedFunction tabulate(
            Class<?> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс должен реализовывать интерфейс TabulatedFunction");
        }

        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница области определения leftX должна быть строго меньше правой границы rightX");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Количество точек pointsCount должно быть не меньше двух");
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException("Указанные границы для табулирования выходят за область определения функции");

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount - 1; i++) {
            points[i] = new FunctionPoint(leftX + i * step, function.getFunctionValue(leftX + i * step));
        }
        points[pointsCount - 1] = new FunctionPoint(rightX, function.getFunctionValue(rightX));

        try {
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    /**
     * Сохраняет табулированную функцию в байтовый поток.
     *
     * @param function функция для сохранения
     * @param out поток для записи
     * @throws IOException при ошибках ввода-вывода
     */
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(out);

        dataOutputStream.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            dataOutputStream.writeDouble(point.getX());
            dataOutputStream.writeDouble(point.getY());
        }

        dataOutputStream.flush();
    }

    /**
     * Считывает табулированную функцию из байтового потока.
     *
     * @param in поток для чтения
     * @return объект {@code TabulatedFunction}, восстановленный из потока
     * @throws IOException при ошибках ввода-вывода
     */
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(in);

        int pointsCount = dataInputStream.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = dataInputStream.readDouble();
            double y = dataInputStream.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return factory.createTabulatedFunction(points);
    }

    /**
     * Записывает табулированную функцию в текстовый поток.
     *
     * @param function функция для записи
     * @param out поток для записи
     */
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        int pointsCount = function.getPointsCount();
        bufferedWriter.write(pointsCount + "\n");
        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = function.getPoint(i);
            bufferedWriter.write(point.getX() + " " + point.getY() + "\n");
        }
        bufferedWriter.flush();
    }

    /**
     * Считывает табулированную функцию из текстового потока.
     *
     * @param in поток для чтения
     * @return объект {@code TabulatedFunction}, восстановленный из потока
     * @throws IOException при ошибках ввода-вывода
     */
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return factory.createTabulatedFunction(points);
    }
}
