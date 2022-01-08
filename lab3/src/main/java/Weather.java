public class Weather {
    Weather() {
    }

    private String desc;
    private double temp;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private double pressure;
    private double humidity;
    private double visibility;
    private double windSpeed;
    private double windDegree;
    private double windGust;
    private double clouds;

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindGust(double windGust) {
        this.windGust = windGust;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }


    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }


    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Weather {\n");
        sb.append("desc: ").append(desc).append('\n');
        sb.append("temperature: ").append(String.format("%.2f", temp)).append('\n');
        sb.append("feelsLike: ").append(String.format("%.2f", feelsLike)).append('\n');
        sb.append("tempMin: ").append(String.format("%.2f", tempMin)).append('\n');
        sb.append("tempMax: ").append(String.format("%.2f", tempMax)).append('\n');
        sb.append("pressure: ").append(pressure).append('\n');
        sb.append("humidity: ").append(humidity).append('\n');
        sb.append("visibility: ").append(visibility).append('\n');
        sb.append("windSpeed: ").append(windSpeed).append('\n');
        sb.append("windDegree: ").append(windDegree).append('\n');
        sb.append("windGust: ").append(windGust).append('\n');
        sb.append("clouds: ").append(clouds).append('\n');
        sb.append('}');
        return sb.toString();
    }
}
