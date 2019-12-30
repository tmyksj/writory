create table user
(
    id       varchar(36)  not null,
    email    varchar(255) not null,
    password varchar(255) not null,
    created  timestamp    not null default 0,
    modified timestamp    not null default 0,
    primary key (id),
    unique key (email)
) engine = InnoDB
  default charset utf8;
