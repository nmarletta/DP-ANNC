package dpannc;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class DPANNC {
    int n;
    int d;

    float sensitivity;
    float epsilon;
    float delta;

    LinkedHashMap<String, Bucket> buckets;
    
    int nodeCount, leafs;

    public DPANNC(float sensitivity, float epsilon, float delta) {
        this.sensitivity = sensitivity;
        this.epsilon = epsilon;
        this.delta = delta;     

        buckets = new LinkedHashMap<>();

        nodeCount = 0;
        leafs = 0;
    }

    public abstract void insert(Vector v);

    public abstract List<Bucket> query(Vector q);

    public abstract int remainder();

    public int leafs() {
        return leafs;
    }

    public int nodes() {
        return nodeCount;
    }

    public void populate(int n, int d, String filePath) throws Exception {
        if (n < 1) throw new Exception("Invalid n");
        if (d < 2) throw new Exception("Invalid d");

        this.n = n;
        this.d = d;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null && counter < n) {
                String[] parts = line.split(" ");
                if (parts.length < d + 1)
                    return;

                String word = parts[0];
                Vector vector = new Vector(d);
                for (int i = 1; i <= d; i++) {
                    vector.setNext(Float.parseFloat(parts[i]));
                }
                vector.normalize().setLabel(word);
                insert(vector);
                printProgress(counter, n, 10);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Processing complete.");
    }

    // public int size() {
    //     return counter.size();
    // }

    private void printProgress(int c, int n, int step) {
        double progress = (double) c / n * 100;
        double margin = 1e-9;
        if (Math.abs(progress % step) < margin) {
            System.out.println((int) progress + "% completed");
        }
    }

    public void printBuckets() {
        int i = 0;
        for (String key : buckets.keySet()) {
            List<Vector> vectors = buckets.get(key).getContent();
            System.out.println("bucket-" + i + " (" + vectors.size() + "): ");
            String b = "";
            for (Vector v : vectors) {
                b += "[" + v.getLabel() + "] ";
            }
            System.out.println(b);
            i++;
        }
    }

    public abstract double getC();
    public abstract double getR();

    public abstract class Node {
        protected int level, count;
        protected Node parent;
        

        public Node(int level, Node parent) {
            this.level = level;
            this.count = 0;
            this.parent = parent;
            
        }
        public Node getParent() {
            return parent;
        }
    }
}
