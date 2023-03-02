package edu.brown.cs.student.server;

import static spark.Spark.after;

import edu.brown.cs.student.server.Handlers.GetHandler;
import edu.brown.cs.student.server.Handlers.LoadHandler;
import edu.brown.cs.student.server.Handlers.WeatherHandler;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers.
 *
 * <p>We have two endpoints in this demo. They need to share state (a menu). This is a great chance
 * to use dependency injection, as we do here with the menu set. If we needed more endpoints, more
 * functionality classes, etc. we could make sure they all had the same shared state.
 */
public class Server {
  public static void main(String[] args) {
    CSVObject csv = new CSVObject(false, null); // Creates a data source object that
    Spark.port(3232);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.
    */

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Setting up the handler for the GET /filename endpoint
    Spark.get("loadcsv", new LoadHandler(csv)); // when you go to localhost:3232/loadcsv?filepath=FILENAMEHERE
    Spark.get("getcsv", new GetHandler(csv)); // when you go to localhost:3232/getcsv
    Spark.get("weather", new WeatherHandler()); // when you go to localhost:3232/weather?lat=FLOAT&lon=FLOAT
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
