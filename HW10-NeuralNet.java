import java.util.*;
import java.io.*;
import java.lang.Math;

public class NeuralNet {
    private static double z1, z2, z3, a1, a2, a3, delta1, delta2, delta3;
    private static double[] arg = new double[18];
    private static double[] w_der = new double[13];

    private static List<Double> train_x1 = new ArrayList<>();
    private static List<Double> train_x2 = new ArrayList<>();
    private static List<Double> train_x3 = new ArrayList<>();
    private static List<Double> train_x4 = new ArrayList<>();
    private static List<Integer> train_y = new ArrayList<>();

    private static List<Double> eval_x1 = new ArrayList<>();
    private static List<Double> eval_x2 = new ArrayList<>();
    private static List<Double> eval_x3 = new ArrayList<>();
    private static List<Double> eval_x4 = new ArrayList<>();
    private static List<Integer> eval_y = new ArrayList<>();

    private static List<Double> test_x1 = new ArrayList<>();
    private static List<Double> test_x2 = new ArrayList<>();
    private static List<Double> test_x3 = new ArrayList<>();
    private static List<Double> test_x4 = new ArrayList<>();
    private static List<Integer> test_y = new ArrayList<>();

    static private void readcsv(String path, List<Double> x1, List<Double> x2, List<Double> x3, List<Double> x4, List<Integer> y){
        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                try {
                    x1.add(Double.parseDouble(temp[0]));
                    x2.add(Double.parseDouble(temp[1]));
                    x3.add(Double.parseDouble(temp[2]));
                    x4.add(Double.parseDouble(temp[3]));
                    y.add(Integer.parseInt(temp[4]));
                }
                catch (Exception ignored){}
            }
            br.close();
        }
        catch(Exception ex)
        {
            System.out.println("File Not Found.");
        }
    }

    static private void calculate_output(double x1, double x2, double x3, double x4){
        z1 = arg[0] + arg[1]*x1 + arg[2]*x2 + arg[3]*x3 + arg[4]*x4;
        z2 = arg[5] + arg[6]*x1 + arg[7]*x2 + arg[8]*x3 + arg[9]*x4;
        z3 = arg[10];

        a1 = 1 / (1 + Math.exp(-z1));
        a2 = 1 / (1 + Math.exp(-z2));

        z3 += (arg[11]*a1 + arg[12]*a2);
        a3 = 1 / (1 + Math.exp(-z3));
    }

    static private void calculate_delta(double val){
        delta3 = (a3 - val) * a3 * (1 - a3);
        delta1 = delta3 * arg[11] * a1 * (1 - a1);
        delta2 = delta3 * arg[12] * a2 * (1 - a2);
    }

    static private void calculate_w_derivitive(double x1, double x2, double x3, double x4){
        w_der[10] = delta3;
        w_der[11] = delta3 * a1;
        w_der[12] = delta3 * a2;
        w_der[5] = delta2;
        w_der[0] = delta1;

        w_der[1] = delta1 * x1;
        w_der[2] = delta1 * x2;
        w_der[3] = delta1 * x3;
        w_der[4] = delta1 * x4;

        w_der[6] = delta2 * x1;
        w_der[7] = delta2 * x2;
        w_der[8] = delta2 * x3;
        w_der[9] = delta2 * x4;
    }

    static private void calculate_w_update(double lr){
        int i;
        for(i=0; i<13; i++){
            arg[i] -= lr*w_der[i];
        }
    }

    static public void main(String[] args) {
        int flag = Integer.valueOf(args[0]), i;
        try {
            if(flag <= 400) {
                for (i = 0; i < 17; i++) {
                    arg[i] = Double.valueOf(args[i + 1]);
                }
                calculate_output(arg[13], arg[14], arg[15], arg[16]);
            }
            else if (flag == 500 || flag == 600){
                for (i = 0; i < 14; i++) {
                    arg[i] = Double.valueOf(args[i + 1]);
                }
            }

            if(flag != 100 && flag != 500 && flag != 600){
                arg[17] = Integer.valueOf(args[18]); // parameter y
                calculate_delta(arg[17]);
            }
        }
        catch (Exception ex){
            System.out.println("Pleae input proper inputs");
            return;
        }

        if(flag == 100){
            System.out.printf("%.5f %.5f\n", a1, a2);
            System.out.printf("%.5f\n", a3);
        }

        else if(flag == 200){
            System.out.printf("%.5f\n", delta3);
        }

        else if(flag == 300){
            System.out.printf("%.5f %.5f\n", delta1, delta2);
        }

        else if(flag == 400){
            calculate_w_derivitive(arg[13], arg[14], arg[15], arg[16]);
            System.out.printf("%.5f %.5f %.5f\n", w_der[10], w_der[11], w_der[12]);
            System.out.printf("%.5f %.5f %.5f %.5f %.5f\n", w_der[0], w_der[1], w_der[2], w_der[3], w_der[4]);
            System.out.printf("%.5f %.5f %.5f %.5f %.5f\n", w_der[5], w_der[6], w_der[7], w_der[8], w_der[9]);
        }

        else if(flag == 500){
            int j;
            double error;
            // please change the file location accordingly
            String traning_path = "./train.csv";
            String eval_path = "./eval.csv";

            readcsv(traning_path, train_x1, train_x2, train_x3, train_x4, train_y);
            readcsv(eval_path, eval_x1, eval_x2, eval_x3, eval_x4, eval_y);

            for(i=0; i<train_x1.size(); i++) {
                error = 0.0;
                calculate_output(train_x1.get(i), train_x2.get(i), train_x3.get(i), train_x4.get(i));
                calculate_delta(train_y.get(i));
                calculate_w_derivitive(train_x1.get(i), train_x2.get(i), train_x3.get(i), train_x4.get(i));

                calculate_w_update(arg[13]);

                for (j = 0; j < 12; j++) {
                    System.out.printf("%.5f ", arg[j]);
                }
                System.out.printf("%.5f\n", arg[12]);

                for(j = 0; j < eval_x1.size(); j++){
                    calculate_output(eval_x1.get(j), eval_x2.get(j), eval_x3.get(j), eval_x4.get(j));
                    error += Math.pow((a3 - eval_y.get(j)), 2) / 2;
                }
                System.out.printf("%.5f\n", error);
            }
        }

        else if(flag == 600){
            // please change the file location accordingly
            String traning_path = "./train.csv";
            String testing_path = "./test.csv";
            int right = 0;

            readcsv(traning_path, train_x1, train_x2, train_x3, train_x4, train_y);
            readcsv(testing_path, test_x1, test_x2, test_x3, test_x4, test_y);

            for(i=0; i<train_x1.size(); i++) {
                calculate_output(train_x1.get(i), train_x2.get(i), train_x3.get(i), train_x4.get(i));
                calculate_delta(train_y.get(i));
                calculate_w_derivitive(train_x1.get(i), train_x2.get(i), train_x3.get(i), train_x4.get(i));
                calculate_w_update(arg[13]);
            }

            for(i=0; i<test_x1.size(); i++) {
                calculate_output(test_x1.get(i), test_x2.get(i), test_x3.get(i), test_x4.get(i));
                if(Math.round(a3) == test_y.get(i)) right++;
                System.out.printf("%d %d %.5f\n", test_y.get(i), Math.round(a3), a3);
            }
            System.out.printf("%.2f\n", (double)right/test_x1.size());
        }
    }
}
