-- create entity game
create table if not exists game
(
    id                      bigint(20)   not null auto_increment,
    dbcreationdate          timestamp    not null default current_timestamp,
    dbupdatedate            timestamp    not null default current_timestamp on update current_timestamp,
    gameid                  varchar(512) not null,
    starttime               timestamp,
    laststarttime           timestamp,
    endtime                 timestamp,
    totaltime               bigint(20)            default 0,
    usernames               text         not null,
    finished                tinyint(1)   not null default 0,
    fk_workflow             bigint(20)   not null,
    fk_workflowpartinstance bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT game_workflow FOREIGN KEY (fk_workflow) REFERENCES workflow (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

create table if not exists workflowpartinstance
(
    id              bigint(20) not null auto_increment,
    dbcreationdate  timestamp  not null default current_timestamp,
    dbupdatedate    timestamp  not null default current_timestamp on update current_timestamp,
    starttime       timestamp,
    laststarttime   timestamp,
    endtime         timestamp,
    totaltime       bigint(20)          default 0,
    fk_workflowpart bigint(20) not null,
    fk_game         bigint(20) not null,
    PRIMARY KEY (id),
    CONSTRAINT workflowpartinstance_workflowpart FOREIGN KEY (fk_workflowpart) REFERENCES workflowpart (id) ON DELETE CASCADE,
    CONSTRAINT workflowpartinstance_game FOREIGN KEY (fk_game) REFERENCES game (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

create table if not exists riddleinstance
(
    id                      bigint(20) not null auto_increment,
    dbcreationdate          timestamp  not null default current_timestamp,
    dbupdatedate            timestamp  not null default current_timestamp on update current_timestamp,
    fk_riddle               bigint(20) not null,
    fk_workflowpartinstance bigint(20) not null,
    resolved                tinyint(1) not null default 0,
    attempts                int        not null default 0,
    PRIMARY KEY (id),
    CONSTRAINT riddleinstance_riddle FOREIGN KEY (fk_riddle) REFERENCES riddle (id) ON DELETE CASCADE,
    CONSTRAINT riddleinstance_solvedworkflowpart FOREIGN KEY (fk_workflowpartinstance) REFERENCES workflowpartinstance (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

create table if not exists riddleinstance_riddlehint
(
    fk_riddleinstance bigint(20) not null,
    fk_riddlehint     bigint(20) not null,
    CONSTRAINT fkriddleinstance FOREIGN KEY (fk_riddleinstance) REFERENCES riddleinstance (id) ON DELETE CASCADE,
    CONSTRAINT fkriddlehint FOREIGN KEY (fk_riddlehint) REFERENCES riddlehint (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

