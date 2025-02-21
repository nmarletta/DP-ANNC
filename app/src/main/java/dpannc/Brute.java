package dpannc;

import java.util.*;

public class Brute {
    static double sensitivity = 1;
    static double epsilon = 0.5;
    static double delta = 0.2;

    static int n, T, remainder;
    static double c, lambda, r, K, alpha, beta, adjSen, threshold, etaU, etaQ;
    List<Vector> vectors;

    public Brute(int n, int d) {
        this.n = n;
        remainder = 0;
        vectors = new ArrayList<>();
        T = d;
        c = 1;
        lambda = (2 * Math.sqrt(2 * c)) / (c * c + 1);
        r = 1 / Math.pow(log(n, 10), 1.0 / 8.0);
        K = Math.sqrt(ln(n));
        alpha = 1 - ((r * r) / 2); // cosine
        beta = Math.sqrt(1 - (alpha * alpha)); // sine
        adjSen = 2;
        threshold = (adjSen / epsilon) * ln(1 + (Math.exp(epsilon / 2) - 1) / delta);
        etaU = Math.sqrt((ln(n) / K)) * (lambda / r);
        etaQ = alpha * etaU - 2 * beta * Math.sqrt(ln(K));
        printSettings();
    }

    public void insert(Vector v) {
        vectors.add(v);
    }

    public int query(Vector q) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector v = vectors.get(i);
            if (v.dot(q) >= etaQ) {
                result.add(v.getLabel());
            }
            printProgress(i, n, 10);
        }
        System.out.println(result.toString());
        return result.size();
    }

    public int getSize() {
        return vectors.size();
    }

    public static void printSettings() {
        System.out.println("c: " + c);
        System.out.println("lambda: " + lambda);
        System.out.println("r: " + r);
        System.out.println("K: " + K);
        System.out.println("alpha: " + alpha);
        System.out.println("beta: " + beta);
        System.out.println("threshold: " + threshold);
        System.out.println("etaU: " + etaU);
        System.out.println("etaQ: " + etaQ);
    }

    public static double log(double N, int base) {
        return Math.log(N) / Math.log(base);
    }

    public static double ln(double N) {
        return Math.log(N);
    }

    private static void printProgress(int c, int n, int step) {
        double progress = (double) c / n * 100;
        double epsilon = 1e-9;
        if (Math.abs(progress % step) < epsilon) {
            System.out.println((int) progress + "% completed");
        }
    }
}