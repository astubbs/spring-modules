create table BOOK
(
    BOOK_ID        INT not null,
    BOOK_NAME      VARCHAR(255) not null,
    constraint PK_BOOK primary key (BOOK_ID)
);

create table BOOK_PAGE
(
    BOOK_PAGE_ID   INT not null,
    BOOK_ID        INT not null,
    BOOK_PAGE_TEXT VARCHAR,
    constraint PK_BOOK_PAGE primary key (BOOK_PAGE_ID,BOOK_ID)
);

create table CATEGORY
(
    CATEGORY_ID    INT not null,
    CATEGORY_NAME  VARCHAR,
    constraint PK_CATEGORY primary key (CATEGORY_ID)
);

create table DOCUMENT_ID
(
    NEXT_DOCUMENT_ID INT not null,
    constraint PK_DOCUMENT_ID primary key (NEXT_DOCUMENT_ID)
);