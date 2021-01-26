package by.sanko.epamLab.task3.entity;

import java.time.LocalDate;
import java.util.Objects;

public final class Crime {
    private String category;
    private String locationType;
    private Location location;
    private String context;
    private OutcomeStatus outcomeStatus;
    private String persistentID;
    private long id;
    private String locationSubtype;
    private LocalDate month;

    public class OutcomeStatus{
        private String category;
        private LocalDate date;

        public OutcomeStatus(String category, LocalDate date) {
            this.category = category;
            this.date = date;
        }

        public String getCategory() {
            return category;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OutcomeStatus that = (OutcomeStatus) o;
            if(date == null ){
                if(that.date == null && this.category.equals(that.category)){
                    return true;
                }
                return false;
            }
            return this.category.equals(that.category) && date.equals(that.date);
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 19 * result + category.hashCode();
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ OutcomeStatus: ");
            if(date == null){
                builder.append("null };");
            }else {
                builder.append("    ").append(category).append("    ").append(date.toString()).append(" };");
            }
            return builder.toString();
        }
    }

    public Crime(String category, String locationType, Location location, String context, String outcomeStatusCategory,
                 LocalDate outcomeDate, String persistentID, long id, String locationSubtype, LocalDate month) {
        this.category = category;
        this.locationType = locationType;
        this.location = location;
        this.context = context;
        this.outcomeStatus = new OutcomeStatus(outcomeStatusCategory, outcomeDate);
        this.persistentID = persistentID;
        this.locationSubtype = locationSubtype;
        this.month = month;
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
        return obj2.category.equals(this.category) && obj2.locationType.equals(this.locationType) &&
                obj2.location.equals(this.location) && obj2.context.equals(this.context) &&
                obj2.outcomeStatus.equals(this.outcomeStatus) && obj2.persistentID.equals(this.persistentID) &&
                obj2.locationSubtype.equals(this.locationSubtype) && obj2.month.equals(this.month) && this.id == obj2.id;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 19 * result + category.hashCode();
        result = 19 * result + locationType.hashCode();
        result = 19 * result + location.hashCode();
        result = 19 * result + context.hashCode();
        result = 19 * result + outcomeStatus.hashCode();
        result = 19 * result + persistentID.hashCode();
        result = 19 * result + (int) id;
        result = 19 * result + locationSubtype.hashCode();
        result = 19 * result + month.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ Crime: ").append("        ").append(id).append("       ").append(category).append("        ")
                .append(locationType).append("        ")
                .append(location.toString()).append("        ").append(context).append("        ")
                .append(outcomeStatus.toString()).append("        ").append(persistentID).append("        ")
                .append(locationSubtype).append("        ").append(month);
        return builder.toString();
    }
}
