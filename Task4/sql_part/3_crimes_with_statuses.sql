create function crimes_with_statuses(datestat date, dateend date)
    returns TABLE
            (
                street_id                     bigint,
                street_name                   character varying,
                outcome_category              character varying,
                count_of_crimes_with_category bigint,
                percentage                    numeric
            )
    language sql
as
$$
WITH sum_crimes AS (
    WITH count_crimes
             AS (
            SELECT DISTINCT (streets.street_id, os.category),
                            streets.street_id,
                            name,
                            os.category,
                            c.month,
                            count(c.crimes_id)
                            OVER (PARTITION BY (streets.street_id, os.category)) as count_of_crimes_of_category
            FROM crime_api.streets
                     JOIN crime_api.locations l on streets.street_id = l.street_id
                     JOIN crime_api.crimes c on l.location_id = c.location_id
                     JOIN crime_api.outcome_statuses os on os.outcome_status_id = c.outcome_status_id
            WHERE c.month BETWEEN DateStat and DateEnd
        )
    SELECT count_crimes.street_id,
           count_crimes.name,
           count_crimes.category,
           count_of_crimes_of_category,
           count_crimes.month,
           sum(count_of_crimes_of_category) OVER (PARTITION BY (street_id)) as all_crimes_at_street
    FROM count_crimes
)
SELECT sum_crimes.street_id,
       sum_crimes.name,
       sum_crimes.category,
       sum_crimes.count_of_crimes_of_category,
       100 * count_of_crimes_of_category / all_crimes_at_street as percentage
FROM sum_crimes;
$$;

SELECT * FROM crimes_with_statuses('2018-1-1','2018-3-1');

