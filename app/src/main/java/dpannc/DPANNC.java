package dpannc;

import java.util.*;

public class DPANNC {
    static int n = 1000;
    static double sensitivity = 1;
    static double epsilon = 0.5;
    static double delta = 0.2;
    
    static int T; // log2(n);
    static double c = 1;
    static double lambda = (2 * Math.sqrt(2 * c)) / (c * c + 1);
    static double r = 1 / Math.pow(log(n, 10), 1.0 / 8.0);
    static double K = Math.sqrt(ln(n));
    static double alpha = 1 - ((r * r) / 2); // cosine
    static double beta = Math.sqrt(1 - (alpha * alpha)); // sine
    static double adjSen = 2;
    static double threshold = (adjSen / epsilon) * ln(1 + (Math.exp(epsilon / 2) - 1) / delta);
    static double etaU = Math.sqrt((ln(n) / K)) * (lambda / r);
    static double etaQ = alpha * etaU - 2 * beta * Math.sqrt(ln(K));
    int remainder = 0;
    Node root;

    public DPANNC(int d) {
        T = d;
        printSettings();
    }

    public int query(Vector q, double etaQ) {
        return root.query(q);
    }

    private class Node<T extends Number> {
        int level, count;
        Vector g;
        LinkedList<Node> childNodes;

        public Node(int level) {
            this.level = level;
            count = 0;
            g = new FloatVector(T).randomGaussian().normalize();
            childNodes = new LinkedList<>();
        }

        public void insertPoint(Vector v, double etaU) {
            if (level >= K) {
                count++;
            } else {
                if (!sendToChildNode(v, etaU)) {
                    remainder++;
                }
            }
        }

        private boolean sendToChildNode(Vector v, double etaU) {
            for (Node n : childNodes) {
                if (n.accepts(v, etaU)) {
                    n.insertPoint(v, etaU);
                    return true;
                }
            }
            while (childNodes.size() < T) {
                Node n = new Node(level + 1);
                if (n.accepts(v, etaU)) {
                    childNodes.add(n);
                    n.insertPoint(v, etaU);
                    return true;
                }
            }
            return false;
        }

        public int query(Vector q) {
            if (level >= K) {
                return count;
            } else {
                int total = 0;
                for (Node n : childNodes) {
                    if (n.accepts(q, etaQ)) {
                        total += n.query(q);
                    }
                }
                return total;
            }
        }

        public void addNoise() {
            if (level >= K) {
                count += TLap.generate(sensitivity, epsilon / adjSen, delta / adjSen);
                if (count <= threshold) {
                    count = 0;
                }
            } else {
                for (Node n : childNodes) {
                    n.addNoise();
                }
            }
        }

        private boolean accepts(Vector<T> v, double etaU) {
            return g.dot(v).floatValue() >= etaU;
        }
    }

    public static double log(double N, int base) {
        return Math.log(N) / Math.log(base);
    }
    public static double ln(double N) {
        return Math.log(N);
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
}