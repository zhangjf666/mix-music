/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2021/6/3 14:48:49                            */
/*==============================================================*/


drop index name on song;

drop table if exists song;

drop table if exists song_album;

drop table if exists song_relative;

drop table if exists song_singer;

drop index name on sys_menu;

drop table if exists sys_menu;

drop index name on sys_role;

drop table if exists sys_role;

drop index role_id on sys_role_menu;

drop table if exists sys_role_menu;

drop index username on sys_user;

drop table if exists sys_user;

drop index user_id on sys_user_role;

drop table if exists sys_user_role;

/*==============================================================*/
/* Table: song                                                  */
/*==============================================================*/
create table song
(
   internal_id          bigint not null auto_increment comment ''内部id'',
   id                   varchar(50) not null comment ''歌曲id'',
   name                 varchar(100) not null comment ''歌曲名称'',
   pic_url              varchar(200) comment ''歌曲图片'',
   duration             bigint comment ''时长'',
   url                  varchar(1000) comment ''歌曲url'',
   br                   varchar(10) comment ''音质'',
   lyric                text comment ''歌词'',
   platform             char(1) comment ''所属歌曲平台'',
   primary key (internal_id)
);

alter table song comment ''歌曲信息表'';

/*==============================================================*/
/* Index: name                                                  */
/*==============================================================*/
create index name on song
(
   name
);

/*==============================================================*/
/* Table: song_album                                            */
/*==============================================================*/
create table song_album
(
   id                   bigint not null auto_increment comment ''id'',
   song_id              bigint not null comment ''歌曲id'',
   album_id             bigint not null comment ''专辑id'',
   primary key (id)
);

alter table song_album comment ''歌曲-专辑表'';

/*==============================================================*/
/* Table: song_relative                                         */
/*==============================================================*/
create table song_relative
(
   id                   bigint not null auto_increment comment ''id'',
   source_song_id       varchar(50) not null comment ''源平台歌曲id'',
   source_platform      char(1) not null comment ''源平台代码'',
   dest_song_id         varchar(50) comment ''目标平台歌曲id'',
   dest_platform        char(1) comment ''目标平台代码'',
   primary key (id)
);

alter table song_relative comment ''各平台歌曲对应表'';

/*==============================================================*/
/* Table: song_singer                                           */
/*==============================================================*/
create table song_singer
(
   id                   bigint not null auto_increment comment ''id'',
   song_id              bigint not null comment ''歌曲id'',
   singer_id            bigint not null comment ''歌手id'',
   primary key (id)
);

alter table song_singer comment ''歌曲-歌手表'';

/*==============================================================*/
/* Table: sys_menu                                              */
/*==============================================================*/
create table sys_menu
(
   id                   bigint not null auto_increment comment ''id'',
   pid                  bigint comment ''上级菜单id'',
   name                 varchar(100) not null comment ''名称'',
   description          varchar(200) comment ''菜单描述'',
   sort                 int(5) comment ''排序'',
   url                  varchar(2000) comment ''访问url'',
   is_outside           char(1) comment ''是否外部地址'',
   type                 char(1) comment ''菜单类型(1:目录,2:菜单,3:按钮)'',
   icon                 varchar(100) comment ''图标'',
   component_name       varchar(100) comment ''组件名称'',
   component            varchar(300) comment ''组件地址'',
   is_show              char(1) default ''1'' comment ''是否显示(1:显示 0:不显示)'',
   permission           varchar(200) comment ''权限标识'',
   create_by            varchar(100) comment ''创建者'',
   create_time          datetime comment ''创建时间'',
   update_by            varchar(100) comment ''更新者'',
   update_time          datetime comment ''更新时间'',
   primary key (id)
);

alter table sys_menu comment ''菜单表'';

/*==============================================================*/
/* Index: name                                                  */
/*==============================================================*/
create index name on sys_menu
(
   name
);

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
   id                   bigint not null auto_increment comment ''id'',
   name                 varchar(100) not null comment ''角色名称'',
   description          varchar(200) comment ''描述'',
   create_by            varchar(100) comment ''创建者'',
   create_time          datetime comment ''创建时间'',
   update_by            varchar(100) comment ''更新者'',
   update_time          datetime comment ''更新时间'',
   primary key (id)
);

alter table sys_role comment ''角色表'';

/*==============================================================*/
/* Index: name                                                  */
/*==============================================================*/
create index name on sys_role
(
   name
);

/*==============================================================*/
/* Table: sys_role_menu                                         */
/*==============================================================*/
create table sys_role_menu
(
   id                   bigint not null auto_increment comment ''id'',
   role_id              bigint not null comment ''角色id'',
   menu_id              bigint not null comment ''菜单id'',
   primary key (id)
);

alter table sys_role_menu comment ''角色-菜单表'';

/*==============================================================*/
/* Index: role_id                                               */
/*==============================================================*/
create index role_id on sys_role_menu
(
   role_id
);

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
   id                   bigint not null auto_increment comment ''id'',
   username             varchar(100) not null comment ''用户名'',
   password             varchar(255) not null comment ''密码'',
   nick_name            varchar(100) comment ''昵称'',
   gender               char(1) comment ''性别'',
   email                varchar(100) comment ''邮箱'',
   phone                varchar(100) comment ''电话'',
   mobile_phone         varchar(100) comment ''手机'',
   type                 char(1) default ''0'' comment ''用户类型(0:普通用户,1:超级管理员))'',
   avatar               varchar(1000) comment ''用户头像'',
   enabled              char(1) default ''1'' comment ''状态:1:启用 0:禁用'',
   create_by            varchar(100) comment ''创建者'',
   create_time          datetime comment ''创建时间'',
   update_by            varchar(100) comment ''更新者'',
   update_time          datetime comment ''更新时间'',
   remarks              varchar(255) comment ''备注信息'',
   del_flag             char(1) not null default ''0'' comment ''删除标记(0:未删除,1已删除)'',
   primary key (id)
);

alter table sys_user comment ''用户表'';

/*==============================================================*/
/* Index: username                                              */
/*==============================================================*/
create index username on sys_user
(
   username
);

/*==============================================================*/
/* Table: sys_user_role                                         */
/*==============================================================*/
create table sys_user_role
(
   id                   bigint not null auto_increment comment ''id'',
   user_id              bigint not null comment ''用户id'',
   role_id              bigint not null comment ''角色id'',
   primary key (id)
);

alter table sys_user_role comment ''用户-角色表'';

/*==============================================================*/
/* Index: user_id                                               */
/*==============================================================*/
create index user_id on sys_user_role
(
   user_id
);

