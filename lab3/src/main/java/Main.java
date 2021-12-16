import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        String geoCode = "Berlin";
        String locale = "en";
        String debug = "true";
        String apiKey = "84f8123e-d997-4512-8aae-36d8b7cf618a";
        //Scanner in = new Scanner(System.in);
        //System.out.println("Enter location: ");
        //geoCode = in.nextLine();
        PlacesAsync placesAsync = new PlacesAsync();
        var places = placesAsync.getPlaces(geoCode, locale, debug, apiKey);
        try {
            ArrayList<Place> listOfPlaces = places.get().get();
            int i = 1;
            for (var place : listOfPlaces) {
                System.out.println(i + ") " + place.toString());
                i++;
            }
            Scanner in = new Scanner(System.in);
            System.out.print("Enter location id: ");
            int id = in.nextInt();
            System.out.println(listOfPlaces.get(id));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
