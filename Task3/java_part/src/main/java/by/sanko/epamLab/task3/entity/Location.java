package by.sanko.epamLab.task3.entity;

import java.util.Objects;

public final class Location {
    private double latitude;
    private double longitude;

    public final class Street{
        private int id;
        private String name;

        public Street(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Street street = (Street) o;
            return id == street.id && name.equals(street.name);
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 19 * result + id;
            result = 19 * result + name.hashCode();
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ Street: ").append("    ").append(id).append("    ").append(name).append(" };");
            return builder.toString();
        }
    }

    private Street street;

    public Location(double latitude, double longitude, int id, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = new Street(id, name);
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
