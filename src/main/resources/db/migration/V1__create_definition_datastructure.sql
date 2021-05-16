-- create base entity workflow
create table if not exists workflow
(
    id                   bigint(20)   not null auto_increment,
    dbcreationdate       timestamp    not null default current_timestamp,
    dbupdatedate         timestamp    not null default current_timestamp on update current_timestamp,
    name                 varchar(512) not null,
    fk_workflowstartpart bigint(20)            default null,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- create entity workflowpart

create table if not exists workflowpart
(
    id             bigint(20)   not null auto_increment,
    dbcreationdate timestamp    not null default current_timestamp,
    dbupdatedate   timestamp    not null default current_timestamp on update current_timestamp,
    name           varchar(512) not null,
    description    text         not null,
    parttype       varchar(255) not null,
    fk_workflow    bigint(20)   not null,
    PRIMARY KEY (id),
    CONSTRAINT workflowpart_workflow FOREIGN KEY (fk_workflow) REFERENCES workflow (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- add constraint to fk_workflowpart to table workflow
alter table workflow
    ADD CONSTRAINT workflow_fk_workflowstartpart FOREIGN KEY (fk_workflowstartpart) REFERENCES workflowpart (id);

-- create entity workflowtransition
create table if not exists workflowtransition
(
    id                 bigint(20)   not null auto_increment,
    dbcreationdate     timestamp    not null default current_timestamp,
    dbupdatedate       timestamp    not null default current_timestamp on update current_timestamp,
    name               varchar(512) not null,
    description        text         not null,
    fk_sourcepart      bigint(20)   not null,
    fk_destinationpart bigint(20)   not null,
    PRIMARY KEY (id),
    CONSTRAINT workflowtransition_sourcewp FOREIGN KEY (fk_sourcepart) REFERENCES workflowpart (id) ON DELETE CASCADE,
    CONSTRAINT workflowtransition_destwp FOREIGN KEY (fk_destinationpart) REFERENCES workflowpart (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- create entity solution
create table if not exists solution
(
    id              bigint(20)   not null auto_increment,
    dbcreationdate  timestamp    not null default current_timestamp,
    dbupdatedate    timestamp    not null default current_timestamp on update current_timestamp,
    name            varchar(512) not null,
    description     text,
    type            varchar(255) not null,
    solution        text         not null,
    solutionoptions text,
    fk_workflowpart bigint(20)   not null,
    PRIMARY KEY (id),
    CONSTRAINT solution_sourcewp FOREIGN KEY (fk_workflowpart) REFERENCES workflowpart (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- create entity riddle
create table if not exists riddle
(
    id              bigint(20)   not null auto_increment,
    dbcreationdate  timestamp    not null default current_timestamp,
    dbupdatedate    timestamp    not null default current_timestamp on update current_timestamp,
    name            varchar(512) not null,
    sortindex       int          not null default 0,
    content         text         not null,
    fk_workflowpart bigint(20)   not null,
    PRIMARY KEY (id),
    CONSTRAINT riddle_workflowpart FOREIGN KEY (fk_workflowpart) REFERENCES workflowpart (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- create entity riddlehint
create table if not exists riddlehint
(
    id             bigint(20)   not null auto_increment,
    dbcreationdate timestamp    not null default current_timestamp,
    dbupdatedate   timestamp    not null default current_timestamp on update current_timestamp,
    name           varchar(512) not null,
    content        text         not null,
    fk_riddle      bigint(20)   not null,
    PRIMARY KEY (id),
    CONSTRAINT riddlehint_riddle FOREIGN KEY (fk_riddle) REFERENCES riddle (id) ON DELETE CASCADE

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;





