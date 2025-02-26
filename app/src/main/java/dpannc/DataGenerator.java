package dpannc;

import java.io.FileWriter;
import java.util.Random;

import javax.sound.sampled.SourceDataLine;

public class DataGenerator {

    public static void main(String[] args) {

    }

    public static void generateFile(String filename, Vector v, int amount, float[] dists) {
        String path = "app/src/main/resources/generated/" + filename + ".txt";
        try (FileWriter writer = new FileWriter(path);) {
            for (int i = 0; i < amount; i++) {
                for (float dist : dists) {
                    Vector v1 = generateVectorAtDistance(v, dist).setLabel("[" + dist + "]");
                    writer.write(v1.toString() + "\n");
                    System.out.println("closer: " + v.distance(v1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector generateVectorAtDistance(Vector v, float distance) {
        Random rand = new Random();
        Vector perturbation = new Vector(v.getDimensions());

        // Generate random perturbation using Gaussian distribution
        for (int i = 0; i < v.getDimensions(); i++) {
            perturbation.setNext((float) rand.nextGaussian());
        }

        // Normalize the perturbation to unit length
        perturbation = perturbation.normalize();

        // Scale the perturbation to the desired distance
        perturbation.multiply(distance);

        // Create a new vector by adding the perturbation to the current vector
        Vector newVector = v.clone();
        for (int i = 0; i < v.getDimensions(); i++) {
            newVector.get()[i] += perturbation.get()[i];
        }

        return newVector;
    }
}
