package dpannc;

import java.util.*;

public class Bucket {
    private int id;
    static int nextId = 0;
    private Vector g;
    private List<Vector> path;
    private DPANNC.Node parent;
    private List<Vector> content;

    public Bucket(Vector g) {
        id = nextId;
        nextId++;
        this.g = g;
        content = new ArrayList<>();
    }

    public void add(Vector v) {
        content.add(v);
    }

    public String toString() {
        String str = "Bucket " + id + ": ";
        for (Vector v : content) {
            str += v.getLabel() + ", ";
        }
        return str;
    }

    public List<Vector> getPath() {
        return path;
    }

    public List<Vector> getContent() {
        return content;
    }
}
