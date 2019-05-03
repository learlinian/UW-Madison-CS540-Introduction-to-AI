import java.util.*;
import java.io.*;

public class BodyVsBrain {
    // please make sure data.csv file is in the same folder with this java file
    private static String path = "./data.csv";
    private static List<Double> body = new ArrayList<>();
    private static List<Double> brain = new ArrayList<>();

    static private void readcsv(){
        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                try {
                    body.add(Double.parseDouble(temp[0]));
                    brain.add(Double.parseDouble(temp[1]));
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

    static private Double[] get_Ave_Der() {
        int i;
        Double[] result = new Double[4];
        double body_sum = 0, brain_sum = 0, body_var_sum = 0, brain_var_sum = 0;

        for(i=0; i<body.size(); i++) {
            body_sum += body.get(i);
            brain_sum += brain.get(i);
        }
        for(i=0; i<body.size(); i++) {
            body_var_sum += Math.pow(body.get(i) - body_sum/body.size(), 2);
            brain_var_sum += Math.pow(brain.get(i) - brain_sum/brain.size(), 2);
        }
        result[0] = body_sum/body.size();
        result[1] = brain_sum/brain.size();
        result[2] = Math.sqrt(body_var_sum/(body.size()-1));
        result[3] = Math.sqrt(brain_var_sum/(brain.size()-1));

        return result;
    }

    static private Double MSE(double b0, double b1) {
        double sum = 0;
        int i;

        for (i = 0; i < body.size(); i++) {
            sum += Math.pow(b0 + b1 * body.get(i) - brain.get(i), 2);
        }

        return sum/body.size();
    }

    static private Double[] MSEDerivitive(double b0, double b1){
        double sum_dev1 = 0, sum_dev2 = 0;
        int i;
        Double[] result = new Double[2];
        for(i=0; i<body.size(); i++) {
            sum_dev1 += (b0 + b1*body.get(i) - brain.get(i));
            sum_dev2 += (b0 + b1*body.get(i) - brain.get(i))*body.get(i);
        }
        result[0] = 2*sum_dev1/body.size();
        result[1] = 2*sum_dev2/body.size();

        return result;
    }

    static private Double[] updata_b(){
        int i;
        double b0, b1=0;
        Double[] result = new Double[2];
        Double[] data = get_Ave_Der();

        for(i=0; i<body.size(); i++){
            b1 += (body.get(i)-data[0]) * (brain.get(i)-data[1]);
        }

        b1 /= Math.pow(data[2], 2) * (body.size() - 1);
        b0 = data[1] - b1*data[0];
        result[0] = b0;
        result[1] = b1;

        return result;
    }

    static public void main(String[] args) {
        int i;
        readcsv();

        int flag = Integer.valueOf(args[0]);
        if (flag == 100) {
            Double[] data;
            data = get_Ave_Der();
            System.out.println(body.size());
            System.out.printf("%.4f %.4f\n", data[0], data[2]);
            System.out.printf("%.4f %.4f\n", data[1], data[3]);
        }

        else if(flag == 200){
            try {
                double b0 = Double.valueOf(args[1]);
                double b1 = Double.valueOf(args[2]);

                System.out.printf("%.4f\n", MSE(b0, b1));
            }
            catch (Exception ex){
                System.out.println("Pleae input 2 variables for b0 and b1 respectively");
            }
        }

        else if(flag == 300){
            try {
                double b0 = Double.valueOf(args[1]);
                double b1 = Double.valueOf(args[2]);
                Double[] derivitive_result;

                derivitive_result = MSEDerivitive(b0, b1);
                System.out.printf("%.4f\n", derivitive_result[0]);
                System.out.printf("%.4f\n", derivitive_result[1]);
            }
            catch (Exception ex){
                System.out.println("Pleae input 2 variables for b0 and b1 respectively");
            }
        }
        else if(flag == 400){
            try {
                double lr = Double.valueOf(args[1]);
                double t = Double.valueOf(args[2]);
                double b0 = 0, b1 = 0;
                Double[] derivitive_result;

                for(i = 1; i <= t; i++) {
                    derivitive_result = MSEDerivitive(b0, b1);

                    b0 -= derivitive_result[0]*lr;
                    b1 -= derivitive_result[1]*lr;

                    System.out.print(i);
                    System.out.printf(" %.4f", b0);
                    System.out.printf(" %.4f", b1);
                    System.out.printf(" %.4f\n", MSE(b0, b1));
                }
            }
            catch (Exception ex){
                System.out.println("Pleae input 2 variables for learning rate and time respectively");
            }
        }

        else if(flag == 500){
            Double[] b;
            b = updata_b();
            System.out.printf("%.4f %.4f %.4f\n", b[0], b[1], MSE(b[0], b[1]));
        }

        else if(flag == 600){
            try {
                double body_w = Double.valueOf(args[1]);
                Double[] b;
                b = updata_b();

                System.out.printf("%.4f\n", b[0] + body_w*b[1]);
            }
            catch (Exception ex){
                System.out.println("Pleae input body weight");
            }
        }

        else if(flag == 700){
            try {
                double lr = Double.valueOf(args[1]);
                double t = Double.valueOf(args[2]);
                double b0 = 0, b1 = 0;
                Double[] derivitive_result, data;

                data = get_Ave_Der();
                for(i=0; i<body.size(); i++){
                    body.set(i, (body.get(i) - data[0])/data[2]);
                }

                for(i = 1; i <= t; i++) {
                    derivitive_result = MSEDerivitive(b0, b1);

                    b0 -= derivitive_result[0]*lr;
                    b1 -= derivitive_result[1]*lr;

                    System.out.print(i);
                    System.out.printf(" %.4f", b0);
                    System.out.printf(" %.4f", b1);
                    System.out.printf(" %.4f\n", MSE(b0, b1));
                }
            }
            catch (Exception ex){
                System.out.println("Pleae input 2 variables for learning rate and time respectively");
            }
        }

        else if(flag == 800){
            try {
                double t = Double.valueOf(args[2]);
                double b0 = 0, b1 = 0;
                Double[] data;

                data = get_Ave_Der();
                for(i=0; i<body.size(); i++){
                    // simply choose item i as the random variable
                    body.set(i, (body.get(i) - data[0])/data[2]);
                }

                for(i = 1; i <= t; i++) {
                    b0 = 2 * (b0 + b1*body.get(i) - brain.get(i));
                    b1 = b0 * body.get(i);

                    System.out.print(i);
                    System.out.printf(" %.4f", b0);
                    System.out.printf(" %.4f", b1);
                    System.out.printf(" %.4f\n", MSE(b0, b1));
                }
            }
            catch (Exception ex){
                System.out.println("Pleae input 2 variables for learning rate and time respectively");
            }
        }

    }
}
