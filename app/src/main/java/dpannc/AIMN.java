package dpannc;

import java.util.*;

import javax.management.Query;

public class AIMN extends DPANNC {

    static int T, remainder;
    static double c, lambda, r, K, alpha, beta, adjSen, threshold, etaU, etaQ;
    Node root;

    public AIMN(float sensitivity, float epsilon, float delta) {
        super(sensitivity, epsilon, delta);
    }

    public void insert(Vector v) {
        root.insertPoint(v);
    }

    // public int query(Vector q) {
    //     return root.query(q);
    // }
    public List<Bucket> query(Vector q) {
        return root.query(q, new ArrayList<Bucket>());
    }

    public int remainder() {
        return remainder;
    }

    public double getC() {
        return c;
    }

    public double getR() {
        return r;
    }

    public int sizePercentage() {
        return (int) (100 / Math.pow(T, K) * nodeCount);
    }

    @Override
    public void populate(int n, int d, String filePath) {
        initSettings(n, d, epsilon, delta);
        try {
            super.populate(n, d, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printSettings();
    }

    public void initSettings(int n, int d, float epsilon, float delta) {
        root = new Node(0, null);
        remainder = 0;
        T = d;
        c = 2;
        lambda = (2 * Math.sqrt(2 * c)) / (c * c + 1);
        r = 1 / Math.pow(log(n, 10), 1.0 / 8.0); 
        K = Math.sqrt(ln(n));
        alpha = 1 - ((r * r) / 2); // cosine
        beta = Math.sqrt(1 - (alpha * alpha)); // sine
        adjSen = 2;
        threshold = (adjSen / epsilon) * ln(1 + (Math.exp(epsilon / 2) - 1) / delta);
        etaU = Math.sqrt((ln(n) / K)) * (lambda / r);
        etaQ = alpha * etaU - 2 * beta * Math.sqrt(ln(K));
    }

    public class Node extends DPANNC.Node {
        Vector g;
        protected List<Node> childNodes;

        public Node(int level, Node parent) {
            super(level, parent);
            g = new Vector(T).randomGaussian();
            childNodes = new ArrayList<>();
        }

        public void insertPoint(Vector v) {
            if (level >= K) {
                buckets.computeIfAbsent(g.toString(), k -> new Bucket(g)).add(v);             
                count++;
            } else {
                if (!sendToChildNode(v)) {
                    remainder++;
                }
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
                Node n = new Node(level + 1, this);
                if (n.accepts(v, etaU)) {
                    childNodes.add(n);
                    n.insertPoint(v);
                    if(level + 1 >= K) leafs++;
                    nodeCount++;
                    return true;
                }
            }
            return false;
        }

        // public int query(Vector q) {
        //     if (level >= K) {
        //         query.put(g, buckets.get(g));
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
        public List<Bucket> query(Vector q, List<Bucket> query) {
            if (level >= K) {
                query.add(buckets.get(g.toString()));
                return query;
            } else {
                for (Node n : childNodes) {
                    if (n.accepts(q, etaQ)) {
                        n.query(q, query);
                    }
                }
                return query;
            }
        }


        public void addNoise() {
            // if (level >= K) {
            //     count += TLap.generate(sensitivity, epsilon / adjSen, delta / adjSen);
            //     if (count <= threshold) {
            //         count = 0;
            //     }
            // } else {
            //     for (Node n : childNodes) {
            //         n.addNoise();
            //     }
            // }
        }

        private boolean accepts(Vector v, double etaU) {
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