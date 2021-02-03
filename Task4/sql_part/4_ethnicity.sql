create function ethnicity(datestat date, dateend date)
    returns TABLE
            (
                ethnicity      character varying,
                count_of_all   bigint,
                arrest_rate    bigint,
                no_action_rate bigint,
                other_rate     bigint
            )
    language sql
as
$$
WITH all_count AS (
    SELECT DISTINCT officer_defined_ethnicity,
                    count(officer_defined_ethnicity) OVER (PARTITION BY officer_defined_ethnicity) as count_all
    FROM crime_api.stop_and_searches
)
SELECT DISTINCT stop_and_searches.officer_defined_ethnicity,
                count_all,
                count(case when stop_and_searches.outcome = 'Arrest' then 1 end) as                       arrest_rate,
                count(case
                          when stop_and_searches.outcome = 'A no further action disposal'
                              then 1 end) as                                                              no_action_rate,
                count_all - count(case when stop_and_searches.outcome = 'Arrest' then 1 end) -
                count(case when stop_and_searches.outcome = 'A no further action disposal' then 1 end) as other_rate
FROM crime_api.stop_and_searches
         INNER JOIN all_count ON all_count.officer_defined_ethnicity = stop_and_searches.officer_defined_ethnicity
WHERE crime_api.stop_and_searches.date_time between DateStat and DateEnd
GROUP BY stop_and_searches.officer_defined_ethnicity, all_count.count_all;
$$;


