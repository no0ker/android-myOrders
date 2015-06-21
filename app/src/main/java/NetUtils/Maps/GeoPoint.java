package NetUtils.Maps;

public class GeoPoint {
    private double lat;
    private double lng;

    public GeoPoint() {
        lat = 0;
        lng = 0;
    }

    public GeoPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
