drop table if exists system_users;
-- 创建用户表
CREATE TABLE system_users
(
    id          bigint NOT NULL PRIMARY KEY,
    nickname    varchar(64)  DEFAULT NULL,
    username    varchar(64)  DEFAULT NULL,
    password    varchar(256) DEFAULT NULL,
    status      integer      DEFAULT 1,
    create_by   bigint       DEFAULT NULL,
    update_by   bigint       DEFAULT NULL,
    create_time timestamp    DEFAULT NULL,
    update_time timestamp    DEFAULT NULL,
    del_flag    boolean      DEFAULT NULL
);
drop table if exists system_role;
-- 创建角色表
CREATE TABLE system_role
(
    id          bigint NOT NULL PRIMARY KEY,
    father_code varchar(64)   DEFAULT NULL,
    code        varchar(64)   DEFAULT NULL,
    full_code   varchar(1024) DEFAULT NULL,
    name        varchar(64)   DEFAULT NULL,
    full_name   varchar(1024) DEFAULT NULL,
    remark      varchar(256)  DEFAULT NULL,
    status      integer       DEFAULT 1,
    create_by   bigint        DEFAULT NULL,
    update_by   bigint        DEFAULT NULL,
    create_time timestamp     DEFAULT NULL,
    update_time timestamp     DEFAULT NULL,
    del_flag    boolean       DEFAULT NULL
);
