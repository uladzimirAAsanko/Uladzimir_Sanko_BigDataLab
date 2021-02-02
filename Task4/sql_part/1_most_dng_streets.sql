create function crimes_at_street(datestat date, dateend date)
    returns TABLE
            (
                street_id        bigint,
                street_name      character varying,
                period           character varying,
                crimes_at_street bigint
            )
    language sql
as
$$
SELECT DISTINCT streets.street_id,
                name,
                concat_ws(' ', 'from', DateStat, 'till', DateEnd),
                count(c.crimes_id) OVER (PARTITION BY streets.street_id) as crimes_at_street
FROM crime_api.streets
         JOIN crime_api.locations l on streets.street_id = l.street_id
         JOIN crime_api.crimes c on l.location_id = c.location_id
WHERE c.month between DateStat and DateEnd
ORDER BY crimes_at_street DESC;
$$;
SELECT * FROM crimes_at_street('2018-1-1','2018-6-1');
