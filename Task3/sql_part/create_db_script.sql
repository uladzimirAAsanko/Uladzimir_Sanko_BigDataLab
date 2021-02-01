create schema crime_api;
create table if not exists crime_api.outcome_statuses
(
    outcome_status_id serial not null
        constraint outcome_statuses_pkey
            primary key,
    category          varchar(100),
    date              date,
    constraint outcome_statuses_date_category_key
        unique (date, category)
);

create table if not exists crime_api.streets
(
    street_id bigint not null
        constraint streets_pkey
            primary key,
    name      varchar(100)
);

create table if not exists crime_api.locations
(
    location_id serial not null
        constraint locations_pkey
            primary key,
    longitude   double precision,
    latitude    double precision,
    street_id   bigint
        constraint fk_street_id
            references crime_api.streets,
    constraint locations_longitude_latitude_key
        unique (longitude, latitude)
);

create table if not exists crime_api.crimes
(
    crimes_id         bigint not null
        constraint crimes_pkey
            primary key,
    category          varchar(100),
    location_type     varchar(100),
    context           varchar(100),
    persistentid      varchar(100),
    location_subtype  varchar(100),
    month             date,
    location_id       integer
        constraint fk_location_id
            references crime_api.locations,
    outcome_status_id integer
        constraint fk_outcome_status_id
            references crime_api.outcome_statuses
);


