import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class PlacesAsync {
    PlacesAsync() {

    }

    public CompletableFuture<CompletableFuture<ArrayList<Place>>> getPlaces(String geoCode, String locale, String debug, String apiKey) {
        String url = "https://graphhopper.com/api/1/geocode?q=" + geoCode + "&locale=" + locale +
                "&debug=" + debug + "&key=" + apiKey;

        AsyncHttpClient client = Dsl.asyncHttpClient();
        CompletableFuture<Response> placesRequest = client
                .prepareGet(url)
                .execute()
                .toCompletableFuture();
        return placesRequest.thenApply(this::formListAsync);
    }

    private CompletableFuture<ArrayList<Place>> formListAsync(Response response) {
        CompletableFuture<ArrayList<Place>> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
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
                place.setLat(arr.getJSONObject(i).getJSONObject("point").getFloat("lat"));
                place.setLng(arr.getJSONObject(i).getJSONObject("point").getFloat("lng"));
                place.setOsm_value(arr.getJSONObject(i).getString("osm_value"));
                list.add(place);
            }
            completableFuture.complete(list);
        });
        return completableFuture;
    }

    public CompletableFuture<Response> getWeather(float lat, float lng, String apiKey) {
        String url = "api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=" + apiKey;
        AsyncHttpClient client = Dsl.asyncHttpClient();
        CompletableFuture<Response> weatherRequest = client
                .prepareGet(url)
                .execute()
                .toCompletableFuture();
        return weatherRequest;
    }
}
