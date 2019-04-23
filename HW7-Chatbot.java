import java.util.*;
import java.io.*;

public class Chatbot{
    private static String filename = "./corpus.txt";
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }
    private static ArrayList<Integer> corpus = readCorpus();
    private static int v = Collections.max(corpus)+1;

    private static int trigram(double r, int h1, int h2){
        double sum = 0.0;
        int w, count, count_w, i;
        for(w = 0; w < v; w++) {
            count = 0;
            count_w = 0;
            for(i=0; i<corpus.size()-2; i++){
                if (corpus.get(i) == h1 && corpus.get(i+1) == h2 && corpus.get(i+2) == w){
                    count_w++;
                    count++;
                }
                else if (corpus.get(i) == h1 && corpus.get(i+1) == h2){
                    count++;
                }
            }
            sum += (double)(count_w + 1) / (count + v);
            if (sum >= r) break;
        }
        return w;
    }

    private  static int bigram(double r, int h1){
        double sum = 0.0;
        int w, count, count_w, i;

        for(w = 0; w < v; w++) {
            count = 0;
            count_w = 0;
            for(i=0; i<corpus.size()-1; i++){
                if (corpus.get(i) == h1 && corpus.get(i+1) == w){
                    count_w++;
                }
                if (corpus.get(i) == h1){
                    count++;
                }
            }
            sum += (double)(count_w + 1) / (count + v);
            if (sum > r) break;
        }
        return w;
    }

    private static int unigram(double r){
        double sum = 0.0;
        int w, count, i;

        for(w = 0; w < v; w++) {
            count = 0;
            for (i = 0; i < corpus.size(); i++) {
                if (corpus.get(i) == w){
                    count++;
                }
            }
            sum += (double)(count + 1) / (corpus.size() + v);
            if (sum > r) break;
        }
        return w;
    }

    static public void main(String[] args){
		int flag = Integer.valueOf(args[0]);
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            int count;

            count = Collections.frequency(corpus, w);
            System.out.println(count);
            System.out.printf("%.7f", (float)(count+1)/(corpus.size()+v));
        }

        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            double sum = 0;
            int i;
            int count;

            //TODO generate
            double r = (float) n1 / n2;
            for (i = 0; i<v; i++){
                count = Collections.frequency(corpus, i);
                double current_P = (double)(count+1)/(corpus.size()+v);
                sum += current_P;
                if(sum >= r){
                    System.out.println(i);
                    System.out.printf("%.7f\n", sum - current_P);
                    System.out.printf("%.7f\n", sum);
                    break;
                }
            }
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count_h = 0;
            int count_hw = 0;
            boolean check = false;
            //TODO
            for(int temp : corpus){
                if(temp == w && check){
                    count_hw++;
                    if(h != w) check = false;
                    else count_h++;
                }
                else if(temp == h){
                    count_h++;
                    check = true;
                }
                else check = false;
            }
            if(corpus.get(corpus.size()-1) == h) count_h--;

            System.out.println(count_hw);
            System.out.println(count_h);
            System.out.printf("%.7f\n", (float)(count_hw+1)/(count_h+v));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            int i;
            double sum = 0;
            int count_h;
            int count_hw;
            boolean check = false;

            for (i = 0; i<v; i++) {
                count_h = 0;
                count_hw = 0;
                for (int temp : corpus) {
                    if (temp == i && check) {
                        count_hw++;
                        if (i != h) check = false;
                        else count_h++;
                    }
                    else if (temp == h) {
                        count_h++;
                        check = true;
                    }
                    else check = false;
                }

                if(corpus.get(corpus.size()-1) == h) count_h--;

                double current_P = (double)(count_hw+1)/(count_h+v);
                sum += current_P;

                if(sum >= (double)n1/n2){
                    System.out.println(i);
                    System.out.printf("%.7f\n", sum - current_P);
                    System.out.printf("%.7f\n", sum);
                    break;
                }
            }
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);

            int i;
            int count_h2w = 0;
            int count_h2 = 0;

            for(i=0; i<corpus.size()-2; i++){
                if (corpus.get(i) == h1 && corpus.get(i+1) == h2 && corpus.get(i+2) == w){
                    count_h2w++;
                }
                if (corpus.get(i) == h1 && corpus.get(i+1) == h2){
                    count_h2++;
                }
            }

            System.out.println(count_h2w);
            System.out.println(count_h2);
            System.out.printf("%.7f\n", (float)(count_h2w+1)/(count_h2+v));
			
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            int i, j, count_h2, count_h2w;
            double sum = 0;

            for (i = 0; i < v; i++) {
                count_h2 = 0;
                count_h2w = 0;
                for(j=0; j<corpus.size()-2; j++){
                    if (corpus.get(j) == h1 && corpus.get(j+1) == h2 && corpus.get(j+2) == i){
                        count_h2w++;
                        count_h2++;
                    }
                    else if (corpus.get(j) == h1 && corpus.get(j+1) == h2){
                        count_h2++;
                    }
                }

                double current_P = (double)(count_h2w+1)/(count_h2+v);
                sum += current_P;
                if(sum >= (double)n1/n2){
                    System.out.println(i);
                    System.out.printf("%.7f\n", sum - current_P);
                    System.out.printf("%.7f\n", sum);
                    break;
                }
            }
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1, h2, w;
            double r;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                r = rng.nextDouble();

                // unigram model
                w = unigram(r);
                h1 = w;
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12) return;

                // bigram model
                r = rng.nextDouble();
                w = bigram(r, h1);
                h2 = h1;
                h1 = w;
                System.out.println(h1);

                // trigram model
                while(h1 != 9 && h1 != 10 && h1 != 12) {
                    r = rng.nextDouble();
                    w = trigram(r, h1, h2);
                    h2 = h1;
                    h1 = w;
                    System.out.println(h1);
                }
            }

            else if(t == 1){
                h1 = Integer.valueOf(args[3]);

                // bigram model
                r = rng.nextDouble();
                w = bigram(r, h1);
                h2 = h1;
                h1 = w;
                System.out.println(h1);

                // trigram model
                while(h1 != 9 && h1 != 10 && h1 != 12) {
                    r = rng.nextDouble();
                    w = trigram(r, h1, h2);
                    h2 = h1;
                    h1 = w;
                    System.out.println(h1);
                }
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);

                // trigram model
                while(h1 != 9 && h1 != 10 && h1 != 12) {
                    r = rng.nextDouble();
                    w = trigram(r, h1, h2);
                    h2 = h1;
                    h1 = w;
                    System.out.println(h1);
                }
            }
        }
    }
}
