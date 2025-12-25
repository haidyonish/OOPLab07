import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        task3();
    }

    public static void task3() {
        System.out.println("\n=== Проверка рефлексии ===");

        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("Создан через рефлексию: " + f.getClass().getSimpleName());
        System.out.println("Точки: " + f);

        f = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(5, 25),
                        new FunctionPoint(10, 100)
                }
        );
        System.out.println("\nСоздан через рефлексию: " + f.getClass().getSimpleName());
        System.out.println("Точки: " + f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 5);
        System.out.println("\nТабулирован через рефлексию: " + f.getClass().getSimpleName());
        System.out.println("Точки: " + f);
    }

    public static void task2() {
        Function f = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("По умолчанию: " + tf.getClass().getSimpleName());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("После установки LinkedList фабрики: " + tf.getClass().getSimpleName());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("После установки Array фабрики: " + tf.getClass().getSimpleName());
    }

    public static void task1() {
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, new double[] {0, 1, 4, 9, 16});
        System.out.println("ArrayTabulatedFunction:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println(p);
        }

        TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 10, new double[] {0, 1, 4, 9, 16});
        System.out.println("\nLinkedListTabulatedFunction:");
        for (FunctionPoint p : listFunc) {
            System.out.println(p);
        }
    }
}