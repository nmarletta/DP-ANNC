
package dpannc;

public class App {

    public static void main(String[] args) {
        Vector v = new FloatVector(2).randomGaussian();
        Vector w = new DoubleVector(2).randomGaussian();
        System.out.println(v.dot(w));
        // String filePath = "../resources/GloVe-6B/glove.6B.50d.txt";
        // DPANNC tree = new DPANNC(50);
        // VectorProcessor.populate(tree, 50, filePath);

        // tree.query(null, 0);
    }
}
