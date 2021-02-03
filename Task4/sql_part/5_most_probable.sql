create function most_common_values(datestat date, dateend date)
    returns TABLE
            (
                street_id        bigint,
                street_name      character varying,
                age_range        character varying,
                common_age_range bigint,
                gender           character varying,
                common_gender    bigint,
                ethnicity        character varying,
                common_ethnicity bigint,
                object_of_search character varying,
                common_objects   bigint,
                outcome          character varying,
                common_outcome   bigint
            )
    language sql
as
$$
WITH outc AS (
    SELECT DISTINCT l.street_id,
                    name,
                    count(outcome) OVER (PARTITION BY l.street_id,outcome)                                     as count_outcome,
                    count(object_of_search)
                    OVER (PARTITION BY l.street_id,object_of_search)                                           as count_objects,
                    count(officer_defined_ethnicity)
                    OVER (PARTITION BY l.street_id,officer_defined_ethnicity)                                  as count_off_ethnicity,
                    count(age_range) OVER (PARTITION BY l.street_id,age_range)                                 as count_of_ages,
                    count(gender) OVER (PARTITION BY l.street_id,gender)                                       as count_gender,
                    gender,
                    officer_defined_ethnicity,
                    age_range,
                    object_of_search,
                    outcome
    FROM crime_api.stop_and_searches
             JOIN crime_api.locations l on l.location_id = stop_and_searches.location_id
             JOIN crime_api.streets s on s.street_id = l.street_id
    WHERE stop_and_searches.outcome IS NOT NULL
        AND stop_and_searches.object_of_search IS NOT NULL
        AND stop_and_searches.officer_defined_ethnicity IS NOT NULL
        AND stop_and_searches.gender IS NOT NULL
        AND age_range IS NOT NULL
       OR '0'
        AND crime_api.stop_and_searches.date_time BETWEEN DateStat and DateEnd
)
SELECT DISTINCT street_id,
                name,
                age_range,
                max(count_of_ages) OVER (PARTITION BY street_id)       as common_age_range,
                gender,
                max(count_gender) OVER (PARTITION BY street_id)        as common_gender,
                officer_defined_ethnicity,
                max(count_off_ethnicity) OVER (PARTITION BY street_id) as common_ethnicity,
                object_of_search,
                max(count_objects) OVER (PARTITION BY street_id)       as common_objects,
                outcome,
                max(count_outcome) OVER (PARTITION BY street_id)       as common_outcome
FROM outc
GROUP BY name, street_id, outcome, age_range, gender, officer_defined_ethnicity, object_of_search, count_of_ages,
         count_objects, count_off_ethnicity, count_gender, count_outcome;
$$;
SELECT * FROM most_common_values('2018-1-1','2018-3-1');

