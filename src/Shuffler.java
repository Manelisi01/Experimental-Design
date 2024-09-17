import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Shuffler {

    // Fetch true random numbers from Random.org API, returning a list of integers
    public static List<Integer> fetchTrueRandomNumbers(int maxIndex) throws IOException {
        // Build the JSON-RPC API request URL
        String urlString = "https://api.random.org/json-rpc/4/invoke";

        // The number of random integers to request is the maxIndex + 1
        int count = maxIndex + 1;

        // JSON request body with API key and other parameters
        String apiKey = "c1592602-fcde-4a0e-aaa1-c82f1edb63e5"; // STA2005S KEY NAME
        String jsonBody = "{"
                + "\"jsonrpc\":\"2.0\"," // JSON-RPC version
                + "\"method\":\"generateIntegers\"," // Method to generate integers
                + "\"params\":{" 
                + "\"apiKey\":\"" + apiKey + "\"," // API key for authentication
                + "\"n\":" + count + "," // Number of integers requested
                + "\"min\":0," // Minimum integer value
                + "\"max\":" + maxIndex + "," // Maximum integer value
                + "\"replacement\":false" // No repetition of numbers (unique)
                + "},"
                + "\"id\":1" // Request ID for tracking
                + "}";

        // Open connection to the Random.org API
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST"); // Set HTTP method to POST
        conn.setRequestProperty("Content-Type", "application/json"); // Set content type to JSON
        conn.setDoOutput(true); // Enable sending request body

        // Send the JSON request to the API
        conn.getOutputStream().write(jsonBody.getBytes());

        // Read the API response
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        // Append each line of the response to the StringBuilder
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close(); // Close the BufferedReader

        // Parse the response JSON
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray randomNumbers = jsonResponse.getJSONObject("result")
                .getJSONObject("random")
                .getJSONArray("data");

        // Convert JSON array to List<Integer>
        List<Integer> randomNumbersList = new ArrayList<>();
        for (int i = 0; i < randomNumbers.length(); i++) {
            randomNumbersList.add(randomNumbers.getInt(i)); // Add each random number to the list
        }

        // Ensure the List has the requested number of unique elements
        if (randomNumbersList.size() < count) {
            throw new IOException("Not enough unique random numbers were returned.");
        }

        // Return the list of random indices
        return randomNumbersList;
    }

    // Generic method to shuffle an ArrayList using a list of random indices
    public static <T> void shuffleArrayList(List<T> list, List<Integer> randomIndices) {

        // Create a copy of the original list
        List<T> shuffledList = new ArrayList<>(list);

        // Reassign each element of the original list based on randomIndices
        for (int i = 0; i < list.size(); i++) {
            list.set(i, shuffledList.get(randomIndices.get(i)));
        }
    }

    // Shuffle the elements of an ArrayList containing int[] arrays
    public static void ShuffleBlocks(ArrayList<int[]> arraylist) {

        try {
            // Fetch random indices for shuffling the arraylist
            List<Integer> randomIndices = fetchTrueRandomNumbers(arraylist.size() - 1);

            // Shuffle the arraylist using the fetched random indices
            shuffleArrayList(arraylist, randomIndices);

        } catch (IOException e) {
            // Catch and print any IOExceptions that occur
            e.printStackTrace();
        }
    }

    /*
     * DEBUG
     * 
     * public static void main(String[] args) {
     * 
     * // Initialize the ArrayList with int[] arrays
     * ArrayList<int[]> OneThousand = new ArrayList<>();
     * 
     * // Add 5 int[] elements to the ArrayList
     * OneThousand.add(new int[] { 1, 2, 3 });
     * OneThousand.add(new int[] { 4, 5, 6 });
     * OneThousand.add(new int[] { 7, 8, 9 });
     * OneThousand.add(new int[] { 10, 11, 12 });
     * OneThousand.add(new int[] { 13, 14, 15 });
     * 
     * // Call ShuffleBlocks to shuffle the elements of the ArrayList
     * ShuffleBlocks(OneThousand);
     * 
     * // Print the shuffled ArrayList to verify
     * for (int[] arr : OneThousand) {
     * 
     * for (int num : arr) {
     * System.out.print(num + " ");
     * }
     * System.out.println();
     * }
     * }
     * 
     */
}

