package by.sanko.epamLab.task3.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public final class Location {
    private double latitude;
    private double longitude;
    private Street street;

    public Location( @JsonProperty(value = "latitude") double latitude, @JsonProperty(value = "longitude") double longitude,
                     @JsonProperty(value = "id") int id, @JsonProperty(value = "name") String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = new Street(id, name);
    }

    public Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Street getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0 && Double.compare(location.longitude, longitude) == 0 &&
                street.equals(location.street);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 19 + (int)latitude;
        result = result * 19 + (int)longitude;
        result = result * 19 + street.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ Location: ").append("    ").append(latitude).append("    ").append(longitude).append("    ")
                .append(street.toString()).append(" };");
        return builder.toString();
    }
}
