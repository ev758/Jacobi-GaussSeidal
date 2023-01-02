import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class JacobiGaussSeidal {

    public static void main(String[] args) {
        //declarations
        int equation = 0;
        int option = 0;
        double equations[][];
        Scanner input = new Scanner(System.in);
        Scanner textFileInput = new Scanner(System.in);

        //get user input
        System.out.print("Enter the number of equations: ");
        equation = input.nextInt();
        System.out.println("\n");

        if (equation < 0 || equation > 10) {
            System.out.println("The number of equations need to be from 0 to 10.");
            System.out.print("Exiting");
            System.exit(0);
        }

        equations = new double[equation][4];

        while (true) {
            //selects an option
            System.out.println("1) Enter coefficients");
            System.out.println("2) Enter text file");
            System.out.print("Select an option: ");
            option = input.nextInt();
            System.out.println("\n");

            if (option == 1) {

                for (int i = 0; i < equation; i++) {

                    System.out.print("Enter the coefficient of x of equation " + (i + 1) + ": ");
                    equations[i][0] = input.nextDouble();
                    System.out.println("\n");

                    System.out.print("Enter the coefficient of y of equation " + (i + 1) + ": ");
                    equations[i][1] = input.nextDouble();
                    System.out.println("\n");

                    System.out.print("Enter the coefficient of z of equation " + (i + 1) + ": ");
                    equations[i][2] = input.nextDouble();
                    System.out.println("\n");

                    System.out.print("Enter the b value of equation " + (i + 1) + ": ");
                    equations[i][3] = input.nextDouble();
                    System.out.println("\n");

                }

                break;

            }
            else if (option == 2) {
                int i = 0;

                try {
                    System.out.print("Enter text file: ");
                    String file = textFileInput.nextLine();
                    File textFile = new File(file);
                    textFileInput = new Scanner(textFile);

                    while (textFileInput.hasNextLine()) {
                        String coefficients[] = textFileInput.nextLine().split(" ");

                        equations[i][0] = Double.parseDouble(coefficients[0]);
                        equations[i][1] = Double.parseDouble(coefficients[1]);
                        equations[i][2] = Double.parseDouble(coefficients[2]);
                        equations[i][3] = Double.parseDouble(coefficients[3]);

                        i++;
                    }

                    textFileInput.close();

                    break;
                }
                catch (FileNotFoundException fileError) {
                    System.out.print("File not found");
                    System.exit(0);
                }
            }
        }

        //selects an option
        System.out.println("1) Jacobi iterative");
        System.out.println("2) Gauss-Seidal");
        System.out.print("Select an option: ");
        option = input.nextInt();
        System.out.println("\n");

        //output
        if (option == 1) {
            jacobiIterative(equations, input);
        }
        else if (option == 2) {
            gaussSeidal(equations, input);
        }
        else {
            System.out.print("Exiting");
            System.exit(0);
        }

        input.close();
    }

    public static void diagonalDominant(double[][] equationsPar) {
        for (int i = 0; i < equationsPar.length; i++) {
            double count = 0.0;

            for (int j = 0; j < equationsPar[0].length; j++) {

                if (equationsPar[i][i] == equationsPar[i][j]) {
                    continue;
                }
                else {
                    count = count + Math.abs(equationsPar[i][j]);
                }

            }

            if (equationsPar[i][i] < count) {
                System.out.println("Not diagonal dominant");
                return;
            }

        }

        System.out.println("Diagonal dominant");
        System.out.print("\n");
    }

    //calculates linear equations with Jacobi Iterative
    public static void jacobiIterative(double[][] equationsPar, Scanner inputPar) {
        //declarations
        double vals[] = new double[3];
        double xVal = 0.0;
        double yVal = 0.0;
        double zVal = 0.0;
        double error = 0.0;
        double l2Norm = 0.0;

        //if linear equations are diagonal dominant
        diagonalDominant(equationsPar);

        //get user input
        System.out.print("Enter the desired stopping error: ");
        error = inputPar.nextDouble();
        System.out.println("\n");

        System.out.println("Enter the starting solution: ");
        xVal = inputPar.nextDouble();
        yVal = inputPar.nextDouble();
        zVal = inputPar.nextDouble();

        for (int i = 0; i < 50; i++) {

            //calculates x, y, and z
            vals[0] = (equationsPar[0][3] - ((equationsPar[0][1] * yVal) +
                    (equationsPar[0][2] * zVal))) / equationsPar[0][0];
            vals[1] = (equationsPar[1][3] - ((equationsPar[1][0] * xVal) +
                    (equationsPar[1][2] * zVal))) / equationsPar[1][1];
            vals[2] = (equationsPar[2][3] - ((equationsPar[2][0] * xVal) +
                    (equationsPar[2][1] * yVal))) / equationsPar[2][2];
            System.out.printf("x column: %.4f %.4f %.4f\n", xVal, yVal, zVal);

            //calculates L2 norm
            l2Norm = Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal, 2) + Math.pow(zVal, 2));

            if (l2Norm < error) {
                System.out.println("L2 value is less than error");
                System.out.print("Exiting");
                return;
            }

            xVal = vals[0];
            yVal = vals[1];
            zVal = vals[2];

            double equation1 = (equationsPar[0][0] * xVal) + (equationsPar[0][1] * yVal)
                    + (equationsPar[0][2] * zVal);
            double equation2 = (equationsPar[1][0] * xVal) + (equationsPar[1][1] * yVal)
                    + (equationsPar[1][2] * zVal);
            double equation3 = (equationsPar[2][0] * xVal) + (equationsPar[2][1] * yVal)
                    + (equationsPar[2][2] * zVal);

            equation1 = Math.round(equation1);
            equation2 = Math.round(equation2);
            equation3 = Math.round(equation3);

            if ((equation1 == equationsPar[0][3]) && (equation2 == equationsPar[1][3])
            && (equation3 == equationsPar[2][3])) {
                //gets x, y, and z
                System.out.printf((i + 1) + ": " + "[%.4f %.4f %.4f]", xVal, yVal, zVal);
                System.out.println("\n");
                return;
            }
            else {
                System.out.printf((i + 1) + ": " + "[%.4f %.4f %.4f]", xVal, yVal, zVal);
                System.out.println("\n");
            }

        }

        inputPar.close();

        //50th iteration of x, y, and z
        System.out.println("Error not reached");
        System.out.printf("50: [%.4f %.4f %.4f]", xVal, yVal, zVal);
    }

    //calculates linear equations with Gauss-Seidal
    public static void gaussSeidal(double[][] equationsPar, Scanner inputPar) {
        //declarations
        double vals[] = new double[3];
        double xVal = 0.0;
        double yVal = 0.0;
        double zVal = 0.0;
        double error = 0.0;
        double l2Norm = 0.0;

        //if linear equations are diagonal dominant
        diagonalDominant(equationsPar);

        //get user input
        System.out.print("Enter the desired stopping error: ");
        error = inputPar.nextDouble();
        System.out.println("\n");

        System.out.println("Enter the starting solution: ");
        xVal = inputPar.nextDouble();
        yVal = inputPar.nextDouble();
        zVal = inputPar.nextDouble();

        for (int i = 0; i < 50; i++) {

            //calculates x, y, and z
            System.out.printf("x column: %.4f %.4f %.4f\n", xVal, yVal, zVal);
            vals[0] = (equationsPar[0][3] - ((equationsPar[0][1] * yVal) +
                    (equationsPar[0][2] * zVal))) / equationsPar[0][0];
            xVal = vals[0];

            vals[1] = (equationsPar[1][3] - ((equationsPar[1][0] * xVal) +
                    (equationsPar[1][2] * zVal))) / equationsPar[1][1];
            yVal = vals[1];

            vals[2] = (equationsPar[2][3] - ((equationsPar[2][0] * xVal) +
                    (equationsPar[2][1] * yVal))) / equationsPar[2][2];
            zVal = vals[2];

            //calculates L2 norm
            l2Norm = Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal, 2) + Math.pow(zVal, 2));

            if (l2Norm < error) {
                System.out.println("L2 value is less than error");
                System.out.print("Exiting");
                return;
            }

            double equation1 = (equationsPar[0][0] * xVal) + (equationsPar[0][1] * yVal)
                    + (equationsPar[0][2] * zVal);
            double equation2 = (equationsPar[1][0] * xVal) + (equationsPar[1][1] * yVal)
                    + (equationsPar[1][2] * zVal);
            double equation3 = (equationsPar[2][0] * xVal) + (equationsPar[2][1] * yVal)
                    + (equationsPar[2][2] * zVal);

            equation1 = Math.round(equation1);
            equation2 = Math.round(equation2);
            equation3 = Math.round(equation3);

            if ((equation1 == equationsPar[0][3]) && (equation2 == equationsPar[1][3])
                    && (equation3 == equationsPar[2][3])) {
                //gets x, y, and z
                System.out.printf((i + 1) + ": " + "[%.4f %.4f %.4f]", xVal, yVal, zVal);
                System.out.println("\n");
                return;
            }
            else {
                System.out.printf((i + 1) + ": " + "[%.4f %.4f %.4f]", xVal, yVal, zVal);
                System.out.println("\n");
            }

        }

        inputPar.close();

        //50th iteration of x, y, and z
        System.out.println("Error not reached");
        System.out.printf("50: [%.4f %.4f %.4f]", xVal, yVal, zVal);
    }
}
