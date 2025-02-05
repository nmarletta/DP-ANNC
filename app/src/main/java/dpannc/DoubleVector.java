package dpannc;

import java.util.Random;

public class DoubleVector implements Vector<Double>{
    private double[] components;
    private int nextEmpty;
    private static Random random = new Random();

    /**
     * Constructs a vector with the given components.
     *
     * @param v the array of components to initialize the vector with.
     */
    public DoubleVector(double[] v) {
        this.components = v.clone();
    }

    /**
     * Constructs a vector with the specified dimension, initialized to zero.
     *
     * @param d the number of dimensions of the vector.
     */
    public DoubleVector(int d) {
        this.components = new double[d];
    }

    /**
     * Returns the number of dimensions of the vector.
     *
     */
    public int getDimensions() {
        return components.length;
    }

    /**
     * Populates the vector with random values sampled from a Gaussian distribution.
     *
     * @return the updated vector with random Gaussian values.
     */
    public DoubleVector randomGaussian() {
        for (int c = 0; c < components.length; c++) {
            this.components[c] = random.nextGaussian();
        }
        return this;
    }

    /**
     * Assigns a value to the next empty component.
     *
     */
    @Override
    public void setNext(Double value) {
        components[nextEmpty] = value;
        nextEmpty += 1;
    }

    /**
     * Returns the components of the vector.
     *
     * @return the array of vector components.
     */
    public double[] get() {
        return components;
    }

    /**
     * Retrieves a specific component of the vector.
     *
     * @param c the index of the component to retrieve.
     * @return the value of the specified component.
     */
    public Double getC(int c) {
        return components[c];
    }

    /**
     * Computes the magnitude (Euclidean norm) of the vector.
     *
     * @return the magnitude of the vector.
     */
    public Double magnitude() {
        double mag = 0.0;
        for (double c : components) {
            mag += c * c;
        }
        return Math.sqrt(mag);
    }

    /**
     * Normalizes the vector to unit length.
     *
     * @return the normalized vector.
     */
    public DoubleVector normalize() {
        double mag = this.magnitude();
        return this.divide(mag);
    }

    /**
     * Copies the components of another vector into this vector.
     *
     * @param v the vector to copy from.
     * @return the updated vector.
     */
    public DoubleVector set(DoubleVector v) {
        for (int c = 0; c < components.length; c++) {
            this.components[c] = v.get()[c];
        }
        return this;
    }

    /**
     * Sets the magnitude of the vector to a specified length while maintaining
     * direction.
     *
     * @param len the desired magnitude.
     * @return the updated vector.
     */
    public DoubleVector setMagnitude(Double len) {
        this.normalize();
        this.multiply(len);
        return this;
    }

    /**
     * Divides each component of the vector by a scalar.
     *
     * @param n the scalar value to divide by.
     * @return the updated vector.
     */
    public DoubleVector divide(Double n) {
        for (int c = 0; c < components.length; c++) {
            this.components[c] /= n;
        }
        return this;
    }

    /**
     * Multiplies each component of the vector by a scalar.
     *
     * @param n the scalar value to multiply by.
     * @return the updated vector.
     */
    public DoubleVector multiply(Double n) {
        for (int c = 0; c < components.length; c++) {
            this.components[c] *= n;
        }
        return this;
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param v the other vector to compute the dot product with.
     * @return the dot product value.
     * @throws IllegalArgumentException if the vectors do not have the same
     *                                  dimension.
     */
    public Double dot(Vector<Double> v) {
        if (this.components.length != v.getDimensions()) {
            throw new IllegalArgumentException("Vectors must have the same dimension for dot product");
        }

        double product = 0.0;
        for (int c = 0; c < components.length; c++) {
            product += this.components[c] * v.getC(c);
        }
        return product;
    }

    /**
     * Returns a clone of this vector.
     *
     * @return a copy of the vector with identical components.
     */
    @Override
    public Vector<Double> clone() {
        return new DoubleVector(components);
    }

    /**
     * Returns a clone of this vector.
     *
     * @return the updated vector.
     */
    @Override
    public Vector<Double> clear() {
        components = new double[components.length];
        return this;
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string containing the vector components.
     */
    @Override
    public String toString() {
        String s = "";
        s += components[0];
        for (int i = 1; i < components.length; i++) {
            s += " : " + components[i];
        }
        return s;
    }

    /**
     * Prints the vector to the standard output.
     */
    public void print() {
        System.out.println(toString());
    }
}
