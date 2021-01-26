create table outcome_statuses
(
    outcome_status_id serial not null
        constraint outcome_statuses_pkey
            primary key,
    category          varchar(100),
    date              date
);

alter table outcome_statuses
    owner to postgres;

create table streets
(
    street_id bigint not null
        constraint streets_pkey
            primary key,
    name      varchar(100)
);

alter table streets
    owner to postgres;

create table locations
(
    location_id serial not null
        constraint locations_pkey
            primary key,
    longitude   double precision,
    latitude    double precision,
    street_id   bigint
        constraint fk_street_id
            references streets
);

alter table locations
    owner to postgres;

create table crimes
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
            references locations,
    outcome_status_id integer
        constraint fk_outcome_status_id
            references outcome_statuses
);

alter table crimes
    owner to postgres;
insert into outcome_statuses values (-1,null,null);


