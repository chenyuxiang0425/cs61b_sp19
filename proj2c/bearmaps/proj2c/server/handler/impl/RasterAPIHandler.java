package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.*;
import static bearmaps.proj2c.utils.Constants.ROOT_ULLAT;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);

        Map<String, Object> results = new HashMap<>();
        //query_success
        boolean query_success = validateTheParams(requestParams);
        results.put("query_success", query_success);

        // depth
        int depth = processRequestGetDepth(requestParams);
        results.put("depth", depth);
        //lon and lat
        Map<String, Object> coordinateUL = processRequestGetXYLonLat(requestParams.get("ullon"),requestParams.get("ullat"),depth,"ul");
        Map<String, Object> coordinateLR = processRequestGetXYLonLat(requestParams.get("lrlon"),requestParams.get("lrlat"),depth,"lr");
        results.put("raster_ul_lon", coordinateUL.get("lon"));
        results.put("raster_ul_lat", coordinateUL.get("lat"));
        results.put("raster_lr_lon", coordinateLR.get("lon"));
        results.put("raster_lr_lat", coordinateLR.get("lat"));
        // render_grid: list of png
        int xLeft = (int) coordinateUL.get("x");
        int xRight = (int) coordinateLR.get("x");
        int yLeft = (int) coordinateUL.get("y");
        int yRight = (int) coordinateLR.get("y");
        String[] [] pnglist = pngList(depth,xLeft,xRight,yLeft,yRight);
        results.put("render_grid", pnglist);
         return results;
    }

    private boolean validateTheParams(Map<String, Double> requestParams) {
        double lrlon = requestParams.get("lrlon");
        double ullon = requestParams.get("ullon");
        double lrlat = requestParams.get("lrlat");
        double ullat = requestParams.get("ullat");
        if (lrlon < ullon || lrlat > ullat)  return false;
        return !(ullon > ROOT_LRLON) && !(lrlon < ROOT_ULLON) && !(ullat < ROOT_LRLAT) && !(lrlat > ROOT_ULLAT);
    }


    private String[][] pngList(int depth,int xLeft,int xRight,int yLeft,int yRight) {
        int rowNum = yRight - yLeft + 1;
        int colNum = xRight - xLeft + 1;
        String[][] renderGrid = new String[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                renderGrid[i][j] = "d" + depth + "_x" + (xLeft + j) + "_y" + (yLeft + i) + ".png";
            }
        }
        return renderGrid;
    }

    //O(1)time,much better
    private Map<String, Object> processRequestGetXYLonLat(double lon,double lat,int depth,String condition) {
        int x = 0;
        int y = 0;
        double locatedLon = ROOT_ULLON;
        double locatedLat = ROOT_LRLAT;
        if (lon < ROOT_ULLON) lon = ROOT_ULLON;
        if (lat < ROOT_LRLAT) lat = ROOT_LRLAT;
        int bounds = (int)(Math.pow(2,depth) - 1);


        Map<String, Object> pointProperty = new HashMap<>();

        double deltLon = (ROOT_LRLON - ROOT_ULLON) / Math.pow(2,depth);
        double deltLat = (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2,depth);

        double calcLat = Math.floor(Math.abs(lat - ROOT_ULLAT) / deltLat);
        double calcLon = Math.floor(Math.abs(lon - ROOT_ULLON) / deltLon);
        if (condition.equals("ul")) {
            x = (int) calcLon;
            y = (int) calcLat;
            locatedLon = (x * deltLon) +  ROOT_ULLON;
            locatedLat = -(y * deltLat) +  ROOT_ULLAT;
        }

        if (condition.equals("lr")) {
            x = (int) calcLon;
            y = (int) calcLat;
            locatedLon = ((x+1) * deltLon) +  ROOT_ULLON;
            locatedLat = -((y+1) * deltLat) +  ROOT_ULLAT;
        }

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > bounds) x = bounds;
        if (y > bounds) y = bounds;
        if (locatedLon > ROOT_LRLON) locatedLon = ROOT_LRLON;
        if (locatedLon < ROOT_ULLON) locatedLon = ROOT_ULLON;
        if (locatedLat > ROOT_ULLAT) locatedLat = ROOT_ULLAT;
        if (locatedLat < ROOT_LRLAT) locatedLat = ROOT_LRLAT;

        pointProperty.put("x",x);
        pointProperty.put("y",y);
        pointProperty.put("lon", locatedLon);
        pointProperty.put("lat", locatedLat);
        return pointProperty;
    }

    // O(N)time, not good
    private Map<String, Object> naiveprocessRequestGetXYLonLat(double lon,double lat,int depth,String condition) {
        int x = 0; // x from 0 to 2^depth -1
        int y = (int) Math.pow(2,depth) - 1; // y from 0 to 2^depth -1
        // init map
        if (lon < ROOT_ULLON) lon = ROOT_ULLON;
        if (lat < ROOT_LRLAT) lat = ROOT_LRLAT;

        Map<String, Object> pointProperty = new HashMap<>();
        //pointProperty : as to d2_x0_y1.png
        //                x is the leftest and rightest x of the png
        //                lon is the leftest and rightest lon of the png

        double deltLon = (ROOT_LRLON - ROOT_ULLON) / Math.pow(2,depth);
        double deltLat = (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2,depth);

        for (double initLon = ROOT_ULLON;initLon < ROOT_LRLON; initLon += deltLon) {
            if (lon < initLon) {
                if (condition.equals("lr")) {
                    pointProperty.put("lon", initLon);
                }
                break;
            }
            pointProperty.put("x",x);
            pointProperty.put("lon",initLon);
            x = x + 1;
        }
        for (double initLat = ROOT_LRLAT; initLat < ROOT_ULLAT; initLat += deltLat) {
            if (lat < initLat) {
                if (condition.equals("ul")) {
                    pointProperty.put("lat", initLat);
                }
                break;
            }
            pointProperty.put("y",y);
            pointProperty.put("lat",initLat);
            y = y - 1;
        }
        return pointProperty;
    }

    // return Deep
    private int processRequestGetDepth(Map<String, Double> requestParams) {
        double lrlon = requestParams.get("lrlon");
        double ullon = requestParams.get("ullon");
        double width = requestParams.get("w");
        double LonDPP = (lrlon - ullon) / width;
        double LonDPPOrigin = (ROOT_LRLON - ROOT_ULLON)/TILE_SIZE;
        int depth = 0;
        while (LonDPP < LonDPPOrigin && depth < 7) {
            LonDPPOrigin = LonDPPOrigin / 2;
            depth +=1;
        }
        return depth;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
