import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationFetcher {
    private static final Logger logger = Logger.getLogger(LocationFetcher.class.getName());
    private static final int TIMEOUT_MS = 5000; // 5 seconds timeout
    private static final String LOCATION_API = "https://ipinfo.io/json";

    public static String fetchCityFromIP() {
        HttpURLConnection con = null;
        BufferedReader in = null;

        try {
            // Set up connection with timeout
            URL url = new URL(LOCATION_API);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(TIMEOUT_MS);
            con.setReadTimeout(TIMEOUT_MS);

            // Read response
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Parse JSON safely
            JSONObject json = new JSONObject(response.toString());

            // Safely extract values with fallbacks
            String city = json.optString("city", "Unknown City");
            String region = json.optString("region", "");
            String country = json.optString("country_name", "");

            // Format location string
            StringBuilder location = new StringBuilder(city);

            if (!region.isEmpty() && !region.equals(city)) {
                location.append(", ").append(region);
            }
            if (!country.isEmpty()) {
                location.append(", ").append(country);
            }

            // Optionally include coordinates
            if (json.has("latitude") && json.has("longitude")) {
                location.append(" (")
                        .append(json.getDouble("latitude"))
                        .append(", ")
                        .append(json.getDouble("longitude"))
                        .append(")");
            }

            return location.toString();

        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to fetch location: " + e.getMessage());
            return "Unknown Location";
        } finally {
            // Clean up resources
            try {
                if (in != null) in.close();
                if (con != null) con.disconnect();
            } catch (Exception e) {
                logger.log(Level.FINE, "Error closing resources", e);
            }
        }
    }
}