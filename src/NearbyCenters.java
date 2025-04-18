import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class NearbyCenters extends Application {

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        webView.getEngine().loadContent(getMapHtml(), "text/html");

        Scene scene = new Scene(webView, 800, 600);
        stage.setTitle("Nearby Food Donation Centers");
        stage.setScene(scene);
        stage.show();
    }

    private String getMapHtml() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Nearby Centers Map</title>
                <style>
                    html, body {
                        height: 100%;
                        margin: 0;
                        padding: 0;
                    }
                    #mapContainer {
                        width: 100%;
                        height: 100%;
                    }
                    iframe {
                        width: 100%;
                        height: 100%;
                        border: none;
                    }
                </style>
            </head>
            <body>
                <div id="mapContainer">Loading map...</div>
                <script>
                    fetch("https://ipapi.co/json")
                        .then(response => response.json())
                        .then(data => {
                            const city = data.city || "India";
                            const iframe = document.createElement("iframe");
                            iframe.src = "https://www.google.com/maps?q=" + encodeURIComponent(city) + "&output=embed";
                            document.getElementById("mapContainer").innerHTML = "";
                            document.getElementById("mapContainer").appendChild(iframe);
                        })
                        .catch(error => {
                            document.getElementById("mapContainer").innerText = "Unable to load map.";
                            console.error("Location fetch error:", error);
                        });
                </script>
            </body>
            </html>
            """;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
