import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class RandomArrayIndexes {

    // Define a static array of size 36 to store the generated random indexes
    public static int[] IndexArray = new int[36];

    // Method to fetch random indexes from Random.org API
    public static void experimental_indexes() {
        try {
            // Random.org API endpoint and parameters
            String apiKey = "c1592602-fcde-4a0e-aaa1-c82f1edb63e5"; // STA2005S KEY NAME
            int numRows = 4;  // Number of rows (adjust based on grid structure)
            int numCols = 9;  // Number of columns (adjust based on grid structure)
            int totalNumbers = numRows * numCols; // Calculate total numbers needed
            int max_index = totalNumbers - 1;  // The maximum index value for array generation

            // API URL for the Random.org service
            String urlString = "https://api.random.org/json-rpc/4/invoke";

            // JSON request body to specify parameters for random integer generation
            String jsonBody = "{"
                    + "\"jsonrpc\":\"2.0\","
                    + "\"method\":\"generateIntegers\","
                    + "\"params\":{"
                    + "\"apiKey\":\"" + apiKey + "\"," // API Key for authentication
                    + "\"n\":" + totalNumbers + "," // Number of random numbers requested
                    + "\"min\":0," // Minimum value for random number
                    + "\"max\":" + max_index + "," // Maximum value for random number
                    + "\"replacement\":false" // No replacement, unique values
                    + "},"
                    + "\"id\":1" // Request ID for tracking
                    + "}";

            // Open connection to Random.org API using HttpURLConnection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // Set the request method to POST
            conn.setRequestProperty("Content-Type", "application/json"); // Set request type to JSON
            conn.setDoOutput(true); // Allow data to be sent in the request body

            // Send the JSON request to the API
            conn.getOutputStream().write(jsonBody.getBytes());

            // Read the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine); // Append each line of the response
            }
            in.close(); // Close the input stream

            /*
             * Uncomment the next line if you want to debug the full JSON response.
             * System.out.println("Full JSON Response: " + response.toString());
             */

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject resultObject = jsonResponse.getJSONObject("result");
            JSONObject randomObject = resultObject.getJSONObject("random");

            // Extract the "data" array from the "random" object, which contains the generated numbers
            JSONArray randomNumbers = randomObject.getJSONArray("data");

            // Ensure the number of random numbers matches the expected count
            if (randomNumbers.length() != totalNumbers) {
                throw new RuntimeException("Unexpected number of random numbers in the response.");
            }

            // Populate the IndexArray with the random numbers
            for (int i = 0; i < IndexArray.length; i++) {
                IndexArray[i] = randomNumbers.getInt(i); // Store each random number in IndexArray
            }

        } catch (Exception e) {
            // Print the stack trace if an exception occurs during execution
            e.printStackTrace();
        }
    }
}

