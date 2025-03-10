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
  default charset utf8mb4;

create table item
(
    id       varchar(36)  not null,
    user_id  varchar(36)  not null,
    title    varchar(255) not null,
    created  timestamp    not null default 0,
    modified timestamp    not null default 0,
    primary key (id),
    foreign key (user_id) references user (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;

create table item_section
(
    id       varchar(36)  not null,
    item_id  varchar(36)  not null,
    position int          not null,
    header   varchar(255) not null,
    body     text         not null,
    star     tinyint(1)   not null,
    created  timestamp    not null default 0,
    modified timestamp    not null default 0,
    primary key (id),
    foreign key (item_id) references item (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;

create table privacy_policy
(
    id       varchar(36) not null,
    body     text        not null,
    created  timestamp   not null default 0,
    modified timestamp   not null default 0,
    primary key (id)
) engine = InnoDB
  default charset utf8mb4;

create table terms_of_service
(
    id       varchar(36) not null,
    body     text        not null,
    created  timestamp   not null default 0,
    modified timestamp   not null default 0,
    primary key (id)
) engine = InnoDB
  default charset utf8mb4;
