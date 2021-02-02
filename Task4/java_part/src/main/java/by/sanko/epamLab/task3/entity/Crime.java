package by.sanko.epamLab.task3.entity;

import by.sanko.epamLab.task3.util.parser.DateParser;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public final class Crime {
    private String category;
    private String locationType;
    @JsonProperty("location")
    private Location location;
    private String context;
    @JsonProperty("outcome_status")
    private OutcomeStatus outcomeStatus;
    private String persistentID;
    private long id;
    private String locationSubtype;
    private LocalDate month;


    public Crime(@JsonProperty(value = "category") String category, @JsonProperty(value = "location_type")String locationType,
                 @JsonProperty(value = "location") Location location, @JsonProperty(value = "context") String context,
                 @JsonProperty(value = "outcome_status") OutcomeStatus outcomeStatus,
                 @JsonProperty(value = "persistent_id") String persistentID,
                 @JsonProperty(value = "id") long id,@JsonProperty(value = "location_subtype") String locationSubtype,
                 @JsonProperty(value = "month") String month) {
        this.category = category;
        this.locationType = locationType;
        this.location = location;
        this.context = context;
        this.outcomeStatus = outcomeStatus;
        this.persistentID = persistentID;
        this.locationSubtype = locationSubtype;
        this.month = DateParser.parseDate(month);
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public String getLocationType() {
        return locationType;
    }

    public Location getLocation() {
        return location;
    }

    public String getContext() {
        return context;
    }

    public OutcomeStatus getOutcomeStatus() {
        return outcomeStatus;
    }

    public String getPersistentID() {
        return persistentID;
    }

    public String getLocationSubtype() {
        return locationSubtype;
    }

    public LocalDate getMonth() {
        return month;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return false;
        }
        if(obj == null || obj.getClass() != this.getClass() ){
            return false;
        }
        Crime obj2 = (Crime) obj;
        boolean status = false;
        if(outcomeStatus == null){
            if(obj2.outcomeStatus == null){
                status = true;
            }
        }else {
            status = outcomeStatus.equals(obj2.outcomeStatus);
        }
        return obj2.category.equals(this.category) && obj2.locationType.equals(this.locationType) &&
                obj2.location.equals(this.location) && obj2.context.equals(this.context) && status &&
                obj2.persistentID.equals(this.persistentID) &&
                obj2.locationSubtype.equals(this.locationSubtype) && obj2.month.equals(this.month) && this.id == obj2.id;
    }

    @Override
    public int hashCode() {
        int result = 1;
        int status = 0;
        if(outcomeStatus != null){
            status = outcomeStatus.hashCode();
        }
        result = 19 * result + category.hashCode();
        result = 19 * result + locationType.hashCode();
        result = 19 * result + location.hashCode();
        result = 19 * result + context.hashCode();
        result = 19 * result + status;
        result = 19 * result + persistentID.hashCode();
        result = 19 * result + (int) id;
        result = 19 * result + locationSubtype.hashCode();
        result = 19 * result + month.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String status = "null";
        if(outcomeStatus != null){
            status = outcomeStatus.toString();
        }
        builder.append("{ Crime: ").append("        ").append(id).append("       ").append(category).append("        ")
                .append(locationType).append("        ")
                .append(location.toString()).append("        ").append(context).append("        ")
                .append(status).append("        ").append(persistentID).append("        ")
                .append(locationSubtype).append("        ").append(month).append(" };\n");
        return builder.toString();
    }
}
