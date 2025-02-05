package dpannc;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VectorProcessor {

    public static void populate(DPANNC tree, int d, String filePath) {
        // String filePath = "../resources/GloVe-6B/glove.6B.50d.txt"; // Path to file
        
        try (FileChannel fileChannel = new FileInputStream(filePath).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            StringBuilder sb = new StringBuilder();
            String currentWord = null;
            Vector vector = new FloatVector(50);

            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    char c = (char) buffer.get();
                    if (c == ' ' || c == '\n') {
                        if (sb.length() > 0) {
                            String wordOrNumber = sb.toString();
                            sb.setLength(0);

                            if (currentWord == null) {
                                // First token in a line is the word
                                currentWord = wordOrNumber;
                            } else {
                                // Remaining tokens are the vector components
                                vector.setNext(Float.parseFloat(wordOrNumber));
                            }
                        }

                        if (c == '\n' && currentWord != null) {
                            // **Feed the vector into ANNC structure**
                            processVector(currentWord, vector);

                            // Reset for the next line
                            currentWord = null;
                            vector.clear();
                        }
                    } else {
                        sb.append(c);
                    }
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Processing complete.");
    }
    public static void main(String[] args) {
        
    }

    /**
     * Process a single vector in ANNC.
     * Modify this method to integrate with your ANNC logic.
     */
    private static void processVector(String word, Vector<Float> vector) {
        
    }
}

