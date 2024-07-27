package expression;

import expression.generic.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (args.length < 2) {
            System.err.println("Not enough arguments");
            return;
        }
        GenericTabulator thisTabulator = new GenericTabulator();
        Object[][][] result = switch (args[0]) {
            case "-i" -> thisTabulator.tabulate("i", args[1], -2, 2, -2, 2, -2, 2);
            case "-d" -> thisTabulator.tabulate("d", args[1], -2, 2, -2, 2, -2, 2);
            case "-bi" -> thisTabulator.tabulate("bi", args[1], -2, 2, -2, 2, -2, 2);
            case "u" -> thisTabulator.tabulate("u", args[1], -2, 2, -2, 2, -2, 2);
            case "b" -> thisTabulator.tabulate("b", args[1], -2, 2, -2, 2, -2, 2);
            case "bool" ->
                    thisTabulator.tabulate("bool", args[1], -2, 2, -2, 2, -2, 2);
            default -> new Object[0][][];
        };
        System.out.println(Arrays.deepToString(result));
    }
}