package dpannc;

public interface Vector<T extends Number> {
    int getDimensions();

    Vector<T> randomGaussian();

    void setNext(T value);

    T getC(int index);

    T magnitude();

    Vector<T> normalize();

    Vector<T> multiply(T value);

    Vector<T> divide(T value);

    Vector<T> setMagnitude(T value);

    Vector<T> clone();

    Vector<T> clear();

    T dot(Vector<T> v);

    String toString();
}
