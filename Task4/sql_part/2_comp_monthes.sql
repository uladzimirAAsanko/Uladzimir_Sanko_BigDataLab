create function month_to_month_comparsion(datestat date, dateend date)
    returns TABLE
            (
                category             character varying,
                month                date,
                current_month_count  bigint,
                previous_month_count bigint,
                delta                bigint,
                percentage           numeric
            )
    language sql
as
$$
WITH prev
         AS (
        WITH count
                 AS (SELECT DISTINCT (category, month),
                                     category,
                                     month,
                                     count(crimes_id) OVER (PARTITION BY category,month ) as current_month_count
                     FROM crime_api.crimes
                     WHERE month BETWEEN DateStat and DateEnd
            )
        SELECT category,
               month,
               current_month_count,
               lead(current_month_count, 1) over (PARTITION BY category) as previos
        FROM count
    )
SELECT category,
       month,
       current_month_count,
       previos,
       previos - current_month_count                     as delta,
       100.0 * (previos - current_month_count) / previos as percentage
FROM prev
ORDER BY category, month;
$$;

SELECT * FROM month_to_month_comparsion('2018-1-1','2018-3-1');


