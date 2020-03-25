package bearmaps.proj2c;

import bearmaps.proj2c.server.handler.APIRouteHandler;
import bearmaps.proj2c.utils.Constants;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

/**
 * Created by rahul
 */
public class MapServerInitializer {

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Place any initialization statements that will be run before the server main loop here.
     * Do not place it in the main function. Do not place initialization code anywhere else.
     **/

    public static void initializeServer(Map<String, APIRouteHandler> apiHandlers){
        port(getHerokuAssignedPort());
        Constants.SEMANTIC_STREET_GRAPH = new AugmentedStreetMapGraph(Constants.OSM_DB_PATH);
        staticFileLocation("/page");
        /* Allow for all origin requests (since this is not an authenticated server, we do not
         * care about CSRF).  */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        Set<String> paths = new HashSet<>();
        for(Map.Entry<String, APIRouteHandler> apiRoute: apiHandlers.entrySet()){
            if(paths.contains(apiRoute.getKey())){
                throw new RuntimeException("Duplicate API Path found");
            }
            get("/"+apiRoute.getKey(), apiRoute.getValue());
            paths.add(apiRoute.getKey());
        }


    }
}
