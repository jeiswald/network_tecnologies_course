import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        String geoCode = "Berlin";
        String locale = "en";
        String debug = "true";
        String apiKeyGeo = "84f8123e-d997-4512-8aae-36d8b7cf618a";
        String apiKeyWeather = "2107ce94c399346e2352b5f104367529";
        String tripMapApiKey = "5ae2e3f221c38a28845f05b60cf3447e25a3e4f15dcaf60f5e1da8b8";
        //Scanner in = new Scanner(System.in);
        //System.out.println("Enter location: ");
        //geoCode = in.nextLine();
        PlacesAsync placesAsync = new PlacesAsync();
        try {
        while (true) {
            var places = placesAsync.getPlaces(geoCode, locale, debug, apiKeyGeo);
                ArrayList<Place> listOfPlaces = places.get();
                for (var place : listOfPlaces) {
                    System.out.println(listOfPlaces.indexOf(place) + ") " + place.toString());
                }
                Scanner in = new Scanner(System.in);
                System.out.print("Enter location id: ");
                int id = in.nextInt();

                var weatherRequest = placesAsync
                        .getWeather(listOfPlaces.get(id).getLat(), listOfPlaces.get(id).getLng(), apiKeyWeather);

            LinkedList<InterestingLocation> interestingLocations = placesAsync.getInterestingPlacesAround
                        (listOfPlaces.get(id).getLat(), listOfPlaces.get(id).getLng(), 1000, tripMapApiKey).get().get().get();
            for (var el : interestingLocations) {
                System.out.println(el);
            }
            System.out.println(weatherRequest.get().get().toString());

        }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
//    String url = "http://api.opentripmap.com/0.1/ru/places/radius?radius=2000&lon=-122.272705&lat=37.80513" +
//                "&apikey=5ae2e3f221c38a28845f05b60cf3447e25a3e4f15dcaf60f5e1da8b8";