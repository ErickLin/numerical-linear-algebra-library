import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GNUserInterface {
    /**
     * The runner for gn_qua, gn_exp, gn_log, and gn_rat
     * @param funcType the function type
     * @throws IOException 
     */
    public static void prompt(FunctionType funcType) throws IOException {
        //initialize all quantities
        Scanner userSc = new Scanner(System.in);
        ArrayList<Pair> points = new ArrayList<>();
        double[] beta0 = new double[3];
        int N = 0;
        boolean valid = false;
        
        //prompt the user for the name of the file containing the coordinates
        while (!valid) {
            try {
                System.out.print("Enter name of text file, including file extension, from which to read coordinates: ");
                String filename = userSc.nextLine();
                Scanner fileSc = new Scanner(new File(filename));
                //use commas and line breaks as delimiters
                fileSc.useDelimiter(",|\n");
                //read all the ordered pairs from the text file
                while (fileSc.hasNext()) {
                    double first = Double.parseDouble(fileSc.next());
                    if (!fileSc.hasNext()) {
                        throw new NumberFormatException();
                    }
                    double second = Double.parseDouble(fileSc.next());
                    points.add(new Pair(first, second));
                }
                valid = true;
            } catch (FileNotFoundException e) {
                System.out.println("File not found, or invalid text file.");
            } catch (NumberFormatException e) {
                System.out.println("Something went wrong in parsing. Check the file formatting and try again.");
            }
        }
        //prompt the user for initial parameters
        for (int i = 0; i < 3; i++) {
            valid = false;
            while (!valid) {
                try {
                    char parameter = 'a';
                    if (i == 1) {
                        parameter = 'b';
                    } else if (i == 2) {
                        parameter = 'c';
                    }
                    System.out.print("Enter an initial guess for " + parameter + ": ");
                    beta0[i] = Double.parseDouble(userSc.next());
                    valid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
        }
        //prompt the user for the number of iterations N
        valid = false;
        while (!valid) {
            try {
                System.out.print("Enter the number of iterations: ");
                N = Integer.parseInt(userSc.next());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
        
        Matrix beta = GaussNewton.gn(points, beta0, N, funcType);
        System.out.println("Curve approximation: " + "a = " + beta.numberAt(0, 0)
                + ", b = " + beta.numberAt(1, 0) + ", c = " + beta.numberAt(2, 0));
    }
}