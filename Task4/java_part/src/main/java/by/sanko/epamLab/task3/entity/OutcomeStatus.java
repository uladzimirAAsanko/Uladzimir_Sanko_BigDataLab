package by.sanko.epamLab.task3.entity;

import by.sanko.epamLab.task3.util.parser.DateParser;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public class OutcomeStatus{
    private String category;
    private LocalDate date;

    public OutcomeStatus(@JsonProperty(value = "category") String category, @JsonProperty(value = "date") String date) {
        this.category = category;
        this.date = DateParser.parseDate(date);
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

