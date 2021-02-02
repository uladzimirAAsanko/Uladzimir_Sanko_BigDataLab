package by.sanko.epamLab.task3.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

public final class Force {
    private final String id;
    private final String name;

    public Force(@JsonProperty(value = "id") String id, @JsonProperty(value = "name")String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Force force = (Force) o;
        return id.equals(force.id) && name.equals(force.name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 19 + id.hashCode();
        result = result * 19 + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{Force: ").append("    ").append(id).append("    ").append(name);
        return builder.toString();
    }
}
