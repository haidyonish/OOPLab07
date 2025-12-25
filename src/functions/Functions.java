package functions;

import functions.meta.*;

public class Functions {

    private Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    /**
     * Вычисляет определённый интеграл функции методом трапеций.
     *
     * @param f функция для интегрирования
     * @param leftX левая граница интегрирования
     * @param rightX правая граница интегрирования
     * @param step шаг интегрирования
     * @return приближённое значение интеграла
     * @throws IllegalArgumentException если границы интегрирования заданы некорректно
     *                                  или выходят за область определения функции
     */
    public static double integrate(Function f, double leftX, double rightX, double step) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException(
                    String.format(
                            "Левая граница интегрирования должна быть меньше правой. (leftX = %.5f, rightX = %.5f)",
                            leftX, rightX
                    )
            );
        }
        double domainLeft = f.getLeftDomainBorder();
        double domainRight = f.getRightDomainBorder();

        if (leftX < domainLeft || rightX > domainRight) {
            throw new IllegalArgumentException(
                    String.format(
                            "Интервал интегрирования [%.5f; %.5f] выходит за область определения функции [%.5f; %.5f]",
                            leftX, rightX, domainLeft, domainRight
                    )
            );
        }
        double integralValue = 0.0;
        double x = leftX;
        while (x + step < rightX) {
            double yLeft = f.getFunctionValue(x);
            double yRight = f.getFunctionValue(x + step);

            integralValue += (yLeft + yRight) * step / 2.0;
            x += step;
        }
        if (x < rightX) {
            double lastStep = rightX - x;
            double yLeft = f.getFunctionValue(x);
            double yRight = f.getFunctionValue(rightX);

            integralValue += (yLeft + yRight) * lastStep / 2.0;
        }
        return integralValue;
    }


}
