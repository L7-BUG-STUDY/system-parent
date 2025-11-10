drop table if exists public.system_users;
drop table if exists public.system_role;

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
    update_time timestamp,
    del_flag    boolean
);

comment on table public.system_users is '用户表';

comment on column public.system_users.nickname is '用户名称';

comment on column public.system_users.username is '用户名';

comment on column public.system_users.password is '密码';

comment on column public.system_users.status is '1启用';

alter table public.system_users
    owner to admin;

create unique index system_users_username_idx
    on public.system_users (username);

create index system_users_status_del_flag_idx
    on public.system_users (status, del_flag);

create index system_users_create_by_idx
    on public.system_users using hash (create_by);

create index system_users_create_time_idx
    on public.system_users (create_time);

create table public.system_role
(
    id          bigint not null
        primary key,
    father_code varchar(64)   default NULL::character varying,
    code        varchar(64)   default NULL::character varying,
    full_code   varchar(1024) default NULL::character varying,
    name        varchar(64)   default NULL::character varying,
    full_name   varchar(1024) default NULL::character varying,
    remark      varchar(256)  default NULL::character varying,
    status      integer       default 1,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    del_flag    boolean
);

comment on table public.system_role is '用户表';

comment on column public.system_role.father_code is '上级id';

comment on column public.system_role.code is '角色标识';

comment on column public.system_role.full_code is '全路径名称';

comment on column public.system_role.name is '名称';

comment on column public.system_role.remark is '备注';

comment on column public.system_role.status is '1启用';

alter table public.system_role
    owner to admin;

create index system_role_father_code_idx
    on public.system_role (father_code);

create unique index system_role_code_idx
    on public.system_role (code);

create index system_role_full_code_idx
    on public.system_role (full_code);

create index system_role_status_del_flag_idx
    on public.system_role (status, del_flag);

create index system_role_create_by_idx
    on public.system_role using hash (create_by);

create index system_role_create_time_idx
    on public.system_role (create_time);

