public class Place {
    private int id;
    private String name;
    private String country;
    private String countrycode;
    private float lat;
    private float lng;
    private String osm_value;

    Place(String name, String country, String countrycode, float lat, float lng, String osm_value) {
        this.name = name;
        this.country = country;
        this.countrycode = countrycode;
        this.lat = lat;
        this.lng = lng;
        this.osm_value = osm_value;
    }

    Place() {
    }

    ;

    @Override
    public String toString() {
        return "{" + '\'' +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", countrycode='" + countrycode + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", osm_value='" + osm_value + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOsm_value(String osm_value) {
        this.osm_value = osm_value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getCountry() {
        return country;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public String getOsm_value() {
        return osm_value;
    }
}
