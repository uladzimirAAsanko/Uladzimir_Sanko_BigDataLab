create function correlation_sas_witn_crimes(datestat date, dateend date)
    returns TABLE
            (
                uniq_rec         record,
                street_id        bigint,
                street_name      character varying,
                month            date,
                drugs_crimes     bigint,
                drugs_search     bigint,
                weapons_crimes   bigint,
                offensive_search bigint,
                theft_crimes     bigint,
                theft_search     bigint
            )
    language sql
as
$$
SELECT DISTINCT (l.street_id, month),
                l.street_id,
                streets.name,
                month,
                count(case when category = 'drugs' then 1 end)                 as     drugs_crimes,
                count(case when sas.object_of_search = 'Controlled drugs' then 1 end) drugs_search,
                count(case when category = 'possession-of-weapons' then 1 end) as     weapons_crimes,
                count(case when sas.object_of_search = 'Offensive weapons' then 1 end) +
                count(case when sas.object_of_search = 'Firearms' then 1 end)  as     offensive_search,
                count(case when category = 'shoplifting' then 1 end) +
                count(case when category = 'theft-from-the-person' then 1 end) as     thefr_crimes,
                count(case when sas.object_of_search = 'Stolen goods' then 1 end)     theft_search
FROM crime_api.streets
         JOIN crime_api.locations l on crime_api.streets.street_id = l.street_id
         JOIN crime_api.crimes c on l.location_id = c.location_id
         JOIN crime_api.stop_and_searches sas on l.location_id = sas.location_id
WHERE month between DateStat and DateEnd
GROUP BY streets.name, l.street_id, month ;
$$;
SELECT * FROM correlation_sas_witn_crimes('2018-1-1','2018-6-1');

