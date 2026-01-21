drop table if exists public.system_users;

create table public.system_users
(
    id          bigint       not null
        primary key,
    nickname    varchar(64)  not null default '',
    username    varchar(64)  not null default '',
    password    varchar(256) not null default '',
    status      integer      not null default 1,
    create_by   bigint,
    update_by   bigint,
    create_time timestamptz,
    update_time timestamptz
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
    id          bigint        not null
        primary key,
    name        varchar(64)   not null,
    father_id   bigint        not null,
    full_id     varchar(1024) not null,
    status      varchar(16)   not null,
    sort        int           not null default 0,
    remark      varchar(256)  not null default '',
    create_by   bigint,
    update_by   bigint,
    create_time timestamptz,
    update_time timestamptz
);

comment on table public.system_role is '角色表';

comment on column public.system_role.name is '角色名称';
comment on column public.system_role.father_id is '父节点id';
comment on column public.system_role.full_id is '全路径id';
comment on column public.system_role.status is '启用状态';
comment on column public.system_role.sort is '排序值';
comment on column public.system_role.remark is '备注';

create index
    on public.system_role (father_id);

create index
    on public.system_role (full_id);

drop table if exists public.system_menu;

create table public.system_menu
(
    id          bigint       not null
        primary key,
    father_id   bigint       not null default -1,
    full_id     varchar(256) not null default '',
    path        varchar(64)  not null default '',
    name        varchar(64)  not null default '',
    component   varchar(64)  not null default '',
    type        varchar(16)  not null default 'FOLDER',
    meta        jsonb        not null default '{}'::jsonb,
    enable      boolean      not null default true,
    sort        int          not null default 0,
    create_by   bigint,
    update_by   bigint,
    create_time timestamptz,
    update_time timestamptz,
    del_flag    boolean               default false
);

comment on table public.system_menu is '菜单表';

comment on column public.system_menu.father_id is '父id';
comment on column public.system_menu.full_id is '全路径id';
comment on column public.system_menu.path is '路径';
comment on column public.system_menu.name is '名称';
comment on column public.system_menu.component is '组件路径';
comment on column public.system_menu.type is '菜单类型';
comment on column public.system_menu.meta is '元数据';
comment on column public.system_menu.sort is '排序值';
comment on column public.system_menu.enable is '启用状态';

create index on public.system_menu (del_flag);
create index on public.system_menu (father_id);
create index on public.system_menu (full_id);
