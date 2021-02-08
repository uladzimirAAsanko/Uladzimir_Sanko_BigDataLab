package by.sanko.epamLab.task3.entity;

import by.sanko.epamLab.task3.util.parser.DateParser;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public final class StopAndSearch {
    private final String type;
    private final Boolean involvedPerson;
    private final LocalDateTime dateTime;
    private final Boolean operation;
    private final String operationName;
    private final Location location;
    private final String gender;
    private final String ageRange;
    private final String selfDefinedEthnicity;
    private final String officerDefinedEthnicity;
    private final String legislation;
    private final String objectOfSearch;
    private final String outcome;
    private final Boolean outcomeLinkedToObjectOfSearch;
    private final Boolean removalOfMoreThanOuterClothing;
    private final Force outcomeObject;

    public Force getOutcomeObject() {
        return outcomeObject;
    }

    public StopAndSearch(@JsonProperty(value = "type") String type,
                         @JsonProperty(value = "involved_person") Boolean involvedPerson,
                         @JsonProperty(value = "datetime") String dateTime,
                         @JsonProperty(value = "operation") Boolean operation,
                         @JsonProperty(value = "operation_name") String operationName,
                         @JsonProperty(value = "location") Location location,
                         @JsonProperty(value = "gender") String gender,
                         @JsonProperty(value = "age_range") String ageRange,
                         @JsonProperty(value = "self_defined_ethnicity") String selfDefinedEthnicity,
                         @JsonProperty(value = "officer_defined_ethnicity") String officerDefinedEthnicity,
                         @JsonProperty(value = "legislation") String legislation,
                         @JsonProperty(value = "object_of_search") String objectOfSearch,
                         @JsonProperty(value = "outcome") String outcome,
                         @JsonProperty(value = "outcome_linked_to_object_of_search") Boolean outcomeLinkedToObjectOfSearch,
                         @JsonProperty(value = "removal_of_more_than_outer_clothing") Boolean removalOfMoreThanOuterClothing,
                         @JsonProperty(value = "outcome_object") Force outcomeObject) {
        this.type = type;
        this.involvedPerson = involvedPerson;
        this.dateTime = DateParser.parseDateTime(dateTime);
        this.operation = operation;
        this.operationName = operationName;
        this.location = location;
        this.gender = gender;
        this.ageRange = ageRange;
        this.selfDefinedEthnicity = selfDefinedEthnicity;
        this.officerDefinedEthnicity = officerDefinedEthnicity;
        this.legislation = legislation;
        this.objectOfSearch = objectOfSearch;
        this.outcome = outcome;
        this.outcomeLinkedToObjectOfSearch = outcomeLinkedToObjectOfSearch;
        this.removalOfMoreThanOuterClothing = removalOfMoreThanOuterClothing;
        this.outcomeObject = outcomeObject;
    }

    public String getType() {
        return type;
    }

    public Boolean getInvolvedPerson() {
        return involvedPerson;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Boolean getOperation() {
        return operation;
    }

    public String getOperationName() {
        return operationName;
    }

    public Location getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getSelfDefinedEthnicity() {
        return selfDefinedEthnicity;
    }

    public String getOfficerDefinedEthnicity() {
        return officerDefinedEthnicity;
    }

    public String getLegislation() {
        return legislation;
    }

    public String getObjectOfSearch() {
        return objectOfSearch;
    }

    public String getOutcome() {
        return outcome;
    }

    public Boolean getOutcomeLinkedToObjectOfSearch() {
        return outcomeLinkedToObjectOfSearch;
    }

    public Boolean getRemovalOfMoreThanOuterClothing() {
        return removalOfMoreThanOuterClothing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StopAndSearch)) return false;
        StopAndSearch that = (StopAndSearch) o;
        return Objects.equals(type, that.type) && Objects.equals(involvedPerson, that.involvedPerson) && Objects.equals(dateTime, that.dateTime) && Objects.equals(operation, that.operation) && Objects.equals(operationName, that.operationName) && Objects.equals(location, that.location) && Objects.equals(gender, that.gender) && Objects.equals(ageRange, that.ageRange) && Objects.equals(selfDefinedEthnicity, that.selfDefinedEthnicity) && Objects.equals(officerDefinedEthnicity, that.officerDefinedEthnicity) && Objects.equals(legislation, that.legislation) && Objects.equals(objectOfSearch, that.objectOfSearch) && Objects.equals(outcome, that.outcome) && Objects.equals(outcomeLinkedToObjectOfSearch, that.outcomeLinkedToObjectOfSearch) && Objects.equals(removalOfMoreThanOuterClothing, that.removalOfMoreThanOuterClothing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, involvedPerson, dateTime, operation, operationName, location, gender, ageRange, selfDefinedEthnicity, officerDefinedEthnicity, legislation, objectOfSearch, outcome, outcomeLinkedToObjectOfSearch, removalOfMoreThanOuterClothing);
    }
}
