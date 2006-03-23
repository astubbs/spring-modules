create table CAR
(
    CAR_ID    INT not null,
    CAR_NAME  VARCHAR(255) not null,
    CAR_MARK  VARCHAR(255) not null,
    CAR_MODEL VARCHAR(255) not null,
    CAR_YEAR  INT not null,
    CAR_PRICE NUMERIC,
    constraint PK_CAR primary key (CAR_ID)
);

create table TEST ( TEST_ID INT not null, TEST_NAME VARCHAR(255) not null, constraint PK_TEST primary key (TEST_ID)
