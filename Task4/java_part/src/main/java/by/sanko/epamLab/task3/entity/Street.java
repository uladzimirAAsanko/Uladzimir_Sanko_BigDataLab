package by.sanko.epamLab.task3.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public final class Street{
    private int id;
    private String name;

    public Street(@JsonProperty(value = "id") int id, @JsonProperty(value = "name") String name) {
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