import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject; // Add org.json library for JSON parsing

public class GPSLocation {
    public static void main(String[] args) {
        try {
            // API URL for location data
            String apiUrl = "http://ip-api.com/json/";

            // Open connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            String city = jsonResponse.getString("city");
            String country = jsonResponse.getString("country");
            double latitude = jsonResponse.getDouble("lat");
            double longitude = jsonResponse.getDouble("lon");

            // Display location details
            System.out.println("City: " + city);
            System.out.println("Country: " + country);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
