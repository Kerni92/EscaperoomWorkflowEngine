create table if not exists user
(
    id             bigint(20)   not null auto_increment,
    dbcreationdate timestamp    not null default current_timestamp,
    dbupdatedate   timestamp    not null default current_timestamp on update current_timestamp,
    firstname      varchar(512) not null,
    lastname       varchar(512) not null,
    username       varchar(512) not null,
    password       varchar(512) not null,
    enabled        int(1)       not null default 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;