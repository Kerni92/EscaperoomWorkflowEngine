-- create entity game
create table if not exists game
(
    id                     bigint(20)   not null auto_increment,
    dbcreationdate         timestamp    not null default current_timestamp,
    dbupdatedate           timestamp    not null default current_timestamp on update current_timestamp,
    gameid                 varchar(512) not null,
    starttime              timestamp,
    endtime                timestamp,
    totaltime              bigint(20)            default 0,
    usernames              text         not null,
    fk_workflow            bigint(20)   not null,
    fk_currentworkflowpart bigint(20)   not null,
    PRIMARY KEY (id),
    CONSTRAINT game_workflow FOREIGN KEY (fk_workflow) REFERENCES workflow (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

create table if not exists executedworkflowpart
(
    id              bigint(20) not null auto_increment,
    dbcreationdate  timestamp  not null default current_timestamp,
    dbupdatedate    timestamp  not null default current_timestamp on update current_timestamp,
    starttime       timestamp,
    endtime         timestamp,
    totaltime       bigint(20)          default 0,
    fk_workflowpart bigint(20) not null,
    fk_game         bigint(20) not null,
    PRIMARY KEY (id),
    CONSTRAINT executedworkflowpart_workflowpart FOREIGN KEY (fk_workflowpart) REFERENCES workflowpart (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

create table if not exists solvedriddle
(
    id                      bigint(20) not null auto_increment,
    dbcreationdate          timestamp  not null default current_timestamp,
    dbupdatedate            timestamp  not null default current_timestamp on update current_timestamp,
    fk_riddle               bigint(20) not null,
    fk_executedworkflowpart bigint(20) not null,
    PRIMARY KEY (id),
    CONSTRAINT solvedriddle_riddle FOREIGN KEY (fk_riddle) REFERENCES riddle (id) ON DELETE CASCADE,
    CONSTRAINT solvedriddle_solvedworkflowpart FOREIGN KEY (fk_executedworkflowpart) REFERENCES executedworkflowpart (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

create table if not exists solvedriddle_riddlehint
(
    fk_solvedriddle bigint(20) not null,
    fk_riddlehint   bigint(20) not null,
    CONSTRAINT fksolvedriddle FOREIGN KEY (fk_solvedriddle) REFERENCES solvedriddle (id) ON DELETE CASCADE,
    CONSTRAINT fkriddlehint FOREIGN KEY (fk_riddlehint) REFERENCES riddlehint (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

