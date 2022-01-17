import org.asynchttpclient.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class PlacesAsync {
    String apiKeyGeo = "84f8123e-d997-4512-8aae-36d8b7cf618a";
    String apiKeyWeather = "2107ce94c399346e2352b5f104367529";
    String tripMapAriKey = "5ae2e3f221c38a28845f05b60cf3447e25a3e4f15dcaf60f5e1da8b8";

    PlacesAsync() {

    }

    public CompletableFuture<ArrayList<Place>> getPlaces(String geoCode, String locale, String debug, String apiKey) {
        String url = "https://graphhopper.com/api/1/geocode?q=" + geoCode + "&locale=" + locale +
                "&debug=" + debug + "&key=" + apiKey;

        AsyncHttpClient client = Dsl.asyncHttpClient();
        CompletableFuture<Response> placesRequest = client
                .prepareGet(url)
                .execute()
                .toCompletableFuture();
        return placesRequest.thenApply(this::formListAsync);
    }

    private ArrayList<Place> formListAsync(Response response) {
        ArrayList<Place> list = new ArrayList<>();
        String stringToParse = response.getResponseBody();
        JSONObject obj = new JSONObject(stringToParse);
        JSONArray arr = obj.getJSONArray("hits");
        for (int i = 0; i < arr.length(); i++) {
            Place place = new Place();
            place.setId(i);
            place.setName(arr.getJSONObject(i).getString("name"));
            place.setCountry(arr.getJSONObject(i).getString("country"));
            place.setCountrycode(arr.getJSONObject(i).getString("countrycode"));
            place.setLat(arr.getJSONObject(i).getJSONObject("point").getDouble("lat"));
            place.setLng(arr.getJSONObject(i).getJSONObject("point").getDouble("lng"));
            place.setOsm_value(arr.getJSONObject(i).getString("osm_value"));
            list.add(place);
        }
        return list;
    }

    public CompletableFuture<CompletableFuture<Weather>> getWeather(double lat, double lng, String apiKey) throws InterruptedException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=" + apiKey;
//        String url = "https://api.openweathermap.org/data/2.5/weather?lat=52&lon=13&appid=2107ce94c399346e2352b5f104367529";
        AsyncHttpClient client = Dsl.asyncHttpClient();
        CompletableFuture<Response> weatherRequest = client
                .prepareGet(url)
                .execute()
                .toCompletableFuture();
        return weatherRequest.thenApply(this::ParseWeather);
    }

    public CompletableFuture<Weather> ParseWeather(Response response) {
        CompletableFuture<Weather> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Weather weather = new Weather();
            String stringToParse = response.getResponseBody();
            JSONObject weatherJson = new JSONObject(stringToParse);
            weather.setDesc(weatherJson.getJSONArray("weather")
                    .getJSONObject(0).getString("description"));

            JSONObject mainWeatherSubJson = weatherJson.getJSONObject("main");
            weather.setTemp(mainWeatherSubJson.getDouble("temp") - 273.15);
            weather.setFeelsLike(mainWeatherSubJson.getDouble("feels_like") - 273.15);
            weather.setTempMin(mainWeatherSubJson.getDouble("temp_min") - 273.15);
            weather.setTempMax(mainWeatherSubJson.getDouble("temp_max") - 273.15);
            weather.setPressure(mainWeatherSubJson.getDouble("pressure"));
            weather.setHumidity(mainWeatherSubJson.getDouble("humidity"));

            weather.setVisibility(weatherJson.getDouble("visibility"));

            JSONObject windWeatherJson = weatherJson.getJSONObject("wind");
            weather.setWindSpeed(windWeatherJson.getDouble("speed"));
            weather.setWindDegree(windWeatherJson.getDouble("deg"));
            weather.setWindGust(windWeatherJson.getDouble("gust"));

            weather.setClouds(weatherJson.getJSONObject("clouds").getDouble("all"));
            completableFuture.complete(weather);
        });
        return completableFuture;
    }

    public CompletableFuture<CompletableFuture<LinkedList<CompletableFuture<InterestingLocation>>>>
    getInterestingPlacesAround(double lat, double lng, double radius, String apiKey) throws ExecutionException, InterruptedException {
        String url = "http://api.opentripmap.com/0.1/ru/places/radius?radius=" +
                +radius + "&lon=" + lng + "&lat=" + lat + "&apikey=" + apiKey;
        AsyncHttpClient client = Dsl.asyncHttpClient();
        CompletableFuture<Response> interestingPlacesRequest = client
                .prepareGet(url)
                .execute()
                .toCompletableFuture();
        return interestingPlacesRequest.thenApply(this::parseInterestingPlaces);
    }

    public CompletableFuture<LinkedList<CompletableFuture<InterestingLocation>>>
    parseInterestingPlaces(Response response) {
        CompletableFuture<HashMap<String, InterestingLocation>> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            HashMap<String, InterestingLocation> map = new HashMap<>();
            String stringToParse = response.getResponseBody();
            JSONObject obj = new JSONObject(stringToParse);
            JSONArray arr = obj.getJSONArray("features");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject prop = arr.getJSONObject(i).getJSONObject("properties");
                InterestingLocation location = new InterestingLocation();
                location.setName(prop.getString("name"));
                location.setRate(prop.getInt("rate"));
                location.setXid(prop.getString("xid"));
                map.put(location.getXid(), location);
            }
            completableFuture.complete(map);
        });
        return completableFuture.thenApply(this::getDescriptionOfLocations);
    }

    public LinkedList<CompletableFuture<InterestingLocation>>
    getDescriptionOfLocations(HashMap<String, InterestingLocation> map) {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        LinkedList<CompletableFuture<InterestingLocation>> futureList = new LinkedList<>();
        for (var el : map.keySet()) {
            InterestingLocation l = map.get(el);
            CompletableFuture<InterestingLocation> completableFuture = new CompletableFuture<>();
                String url = "http://api.opentripmap.com/0.1/ru/places/xid/" + el + "?apikey=" + tripMapAriKey;
                CompletableFuture<Response> interestingPlacesRequest = client
                        .prepareGet(url)
                        .execute()
                        .toCompletableFuture();
                interestingPlacesRequest.thenApply( response ->
                Executors.newCachedThreadPool().submit(() -> {
                    JSONObject obj = new JSONObject(response.getResponseBody());
                    if (obj.has("wikipedia_extracts")) {
                        String desc = obj.getJSONObject("wikipedia_extracts").getString("text");
                        l.setDesc(desc);
                    }
                    if (obj.has("wikipedia")) {
                        String wiki = obj.getString("wikipedia");
                        l.setWiki(wiki);
                    }
                    completableFuture.complete(l);
                }));
            futureList.add(completableFuture);
        }
        return futureList;
    }
}
