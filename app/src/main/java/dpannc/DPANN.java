package dpannc;

import java.util.*;

public class DPANN {
    static double sensitivity = 1;
    static double epsilon = 0.5;
    static double delta = 0.2;
    
    static int n, T, remainder;
    static double c, lambda, r, K, alpha, beta, adjSen, threshold, etaU, etaQ;
    Node root;
    List<List<Vector>> queryData; 

    public DPANN(int n, int d) {
        this.n = n;
        remainder = 0;
        root = new Node(0);
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
        root.insertPoint(v);
    }

    public List<List<Vector>> query(Vector q) {
        queryData = new ArrayList<List<Vector>>();
        root.queryFull(q, queryData);
        return queryData;
    }

    // public int query(Vector q) {
    //     return root.query(q);
    // }

    private class Node {
        int level;
        int count;
        List<Vector> content;
        Vector g;
        List<Node> childNodes;

        public Node(int level) {
            this.level = level;
            count = 0;
            content = new ArrayList<>();
            g = new Vector(T).randomGaussian();
            childNodes = new ArrayList<>();
        }

        public void insertPoint(Vector v) {
            if (level >= K) {
                content.addLast(v);
                count++;
                return;
            } else {
                if (!sendToChildNode(v)) {
                    remainder++;
                    return;
                }
                // System.out.println(el + " failed");
            }

        }

        private boolean sendToChildNode(Vector v) {
            for (Node n : childNodes) {
                if (n.accepts(v, etaU)) {
                    n.insertPoint(v);
                    return true;
                }
            }
            while (childNodes.size() < T) {
                Node n = new Node(level + 1);
                if (n.accepts(v, etaU)) {
                    childNodes.add(n);
                    n.insertPoint(v);
                    return true;
                }
            }
            return false;
        }

        // public int query(Vector q) {
        //     if (level >= K) {
        //         System.out.println(content.toString());
        //         return count;
        //     } else {
        //         int total = 0;
        //         for (Node n : childNodes) {
        //             if (n.accepts(q, etaQ)) {
        //                 total += n.query(q);
        //             }
        //         }
        //         return total;
        //     }
        // }

        public void queryFull(Vector q, List<List<Vector>> data) {
            if (level >= K) {
                data.add(content);
                // System.out.println(content.toString());
            } else {
                int total = 0;
                for (Node n : childNodes) {
                    if (n.accepts(q, etaQ)) {
                        n.queryFull(q, data);
                    }
                }
            }
        }

        // public void addNoise() {
        //     if (level >= K) {
        //         count += TLap.generate(sensitivity, epsilon / adjSen, delta / adjSen);
        //         if (count <= threshold) {
        //             count = 0;
        //         }
        //     } else {
        //         for (Node n : childNodes) {
        //             n.addNoise();
        //         }
        //     }
        // }

        private boolean accepts(Vector v, double eta) {
            return g.dot(v).floatValue() >= eta;
        }
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
}