import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ArrayGeneration {

    // ArrayLists to store multiple random integer arrays of different sizes
    public static ArrayList<int[]> FiftyThousand;
    public static ArrayList<int[]> HundredThousand;
    public static ArrayList<int[]> TwoHundredThousand;
    public static ArrayList<int[]> FourHundredThousand;

    // Function to fetch random numbers using the /dev/urandom device
    public static int[] fetchRandomNumbersFromURandom(int size) throws IOException {
        int[] randomNumbers = new int[size]; // Create an array to hold random numbers
        try (InputStream is = new FileInputStream("/dev/urandom")) { // Open /dev/urandom as an input stream
            byte[] bytes = new byte[size * 4]; // Allocate a byte array (4 bytes per integer)
            is.read(bytes); // Read random bytes from /dev/urandom

            // Convert the byte data into integers
            for (int i = 0; i < size; i++) {
                int value = ((bytes[i * 4] & 0xFF) << 24) | // Convert 4 bytes to an integer
                        ((bytes[i * 4 + 1] & 0xFF) << 16) |
                        ((bytes[i * 4 + 2] & 0xFF) << 8) |
                        (bytes[i * 4 + 3] & 0xFF);
                randomNumbers[i] = value; // Store the integer in the array
            }
        }
        return randomNumbers; // Return the array of random integers
    }

    // Function to generate multiple random integer arrays and store them in an ArrayList
    public static ArrayList<int[]> generateRandomIntegerArrays(int numberOfArrays, int arraySize)
            throws IOException {
        ArrayList<int[]> randomArrays = new ArrayList<>(); // Create a list to hold the arrays

        // Generate the specified number of random integer arrays
        for (int i = 0; i < numberOfArrays; i++) {
            // Fetch a random integer array and add it to the ArrayList
            randomArrays.add(fetchRandomNumbersFromURandom(arraySize));
        }

        return randomArrays; // Return the list of random arrays
    }

    // Function to generate blocks of random integer arrays for different sizes
    public static void GenerateBlocks() {
        try {
            // Generate and store 9 arrays of size 50,000 in FiftyThousand
            FiftyThousand = generateRandomIntegerArrays(9, 50000);
            // Generate and store 9 arrays of size 100,000 in HundredThousand
            HundredThousand = generateRandomIntegerArrays(9, 100000);
            // Generate and store 9 arrays of size 200,000 in TwoHundredThousand
            TwoHundredThousand = generateRandomIntegerArrays(9, 200000);
            // Generate and store 9 arrays of size 400,000 in FourHundredThousand
            FourHundredThousand = generateRandomIntegerArrays(9, 400000);
        } catch (IOException e) {
            // Handle any IOExceptions that occur during array generation
            e.printStackTrace();
        }
    }

}

