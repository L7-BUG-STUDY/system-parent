drop table if exists public.system_users;

create table public.system_users
(
    id          bigint not null
        primary key,
    nickname    varchar(64)  default NULL::character varying,
    username    varchar(64)  default NULL::character varying,
    password    varchar(256) default NULL::character varying,
    status      integer      default 1,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp
);

comment on table public.system_users is '用户表';

comment on column public.system_users.nickname is '用户名称';

comment on column public.system_users.username is '用户名';

comment on column public.system_users.password is '密码';

comment on column public.system_users.status is '1启用';

create unique index system_users_username_idx
    on public.system_users (username);

create index system_users_status_idx
    on public.system_users (status);
INSERT INTO public.system_users (id, nickname, username, password, status, create_by, update_by, create_time,
                                 update_time)
VALUES (-1, 'root', 'root', '$2b$10$2Wh0XP9AAgTH.XWi2gfPyuwyrFhjUJIbZs4sFVSzedDS/XA3eycYS', 1, -1, -1,
        now(), now());

drop table if exists public.system_role;
create table public.system_role
(
    id          bigint not null
        primary key,
    code        varchar(64)   default NULL,
    name        varchar(64)   default NULL,
    father_id   bigint        default NULL,
    full_id     varchar(2048) default NULL,
    full_name   varchar(1024) default NULL,
    remark      varchar(256)  default NULL,
    status      integer       default 1,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp
);

comment on table public.system_role is '角色表';

comment on column public.system_role.code is '角色编码';
comment on column public.system_role.name is '角色名称';
comment on column public.system_role.father_id is '上级id';
comment on column public.system_role.full_id is '全路径id';
comment on column public.system_role.full_name is '全路径名称';
comment on column public.system_role.remark is '备注';
comment on column public.system_role.status is '1启用';

create unique index
    on public.system_role (code);

create index
    on public.system_role (father_id);

create index
    on public.system_role (full_id);

create index
    on public.system_role (status);

