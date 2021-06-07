/*==============================================================*/
/* Table: album                                                 */
/*==============================================================*/
create table album
(
   id                   varchar(32) not null comment 'id',
   album_id             varchar(32) not null comment '专辑id',
   album_name           varchar(100) not null comment '专辑名称',
   platform             char(1) not null comment '所属歌曲平台',
   primary key (id)
);

alter table album comment '专辑信息表';

/*==============================================================*/
/* Table: singer                                                */
/*==============================================================*/
create table singer
(
   id                   varchar(32) not null comment 'id',
   singer_id            varchar(32) not null comment '歌手id',
   singer_name          varchar(100) not null comment '歌手名称',
   platform             char(1) not null comment '所属歌曲平台',
   primary key (id)
);

alter table singer comment '歌手信息表';

/*==============================================================*/
/* Table: song                                                  */
/*==============================================================*/
create table song
(
   id                   varchar(32) not null comment '内部id',
   song_id              varchar(50) not null comment '歌曲id',
   name                 varchar(100) not null comment '歌曲名称',
   pic_url              varchar(500) comment '歌曲图片',
   duration             bigint comment '时长',
   url                  varchar(1000) comment '歌曲url',
   br                   varchar(10) comment '音质',
   lyric                text comment '歌词',
   platform             char(1) comment '所属歌曲平台',
   primary key (id)
);

alter table song comment '歌曲信息表';
/*==============================================================*/
/* Table: song_album                                            */
/*==============================================================*/
create table song_album
(
   id                   varchar(32) not null comment 'id',
   song_id              varchar(32) not null comment '歌曲id',
   album_id             varchar(32) not null comment '专辑id',
   primary key (id)
);

alter table song_album comment '歌曲-专辑表';

/*==============================================================*/
/* Table: song_relative                                         */
/*==============================================================*/
create table song_relative
(
   id                   varchar(32) not null comment 'id',
   source_id            varchar(32) not null comment '源平台歌曲内部id',
   dest_id              varchar(32) not null comment '目标平台歌曲内部id',
   primary key (id)
);

alter table song_relative comment '各平台歌曲对应表';

/*==============================================================*/
/* Table: song_singer                                           */
/*==============================================================*/
create table song_singer
(
   id                   varchar(32) not null comment 'id',
   song_id              varchar(32) not null comment '歌曲id',
   singer_id            varchar(32) not null comment '歌手id',
   primary key (id)
);

alter table song_singer comment '歌曲-歌手表';

/*==============================================================*/
/* Table: sys_menu                                              */
/*==============================================================*/
create table sys_menu
(
   id                   varchar(32) not null comment 'id',
   pid                  varchar(32) comment '上级菜单id',
   name                 varchar(100) not null comment '名称',
   description          varchar(200) comment '菜单描述',
   sort                 int(5) comment '排序',
   url                  varchar(2000) comment '访问url',
   is_outside           char(1) comment '是否外部地址',
   type                 char(1) comment '菜单类型(1:目录,2:菜单,3:按钮)',
   icon                 varchar(100) comment '图标',
   component_name       varchar(100) comment '组件名称',
   component            varchar(300) comment '组件地址',
   is_show              char(1) default '1' comment '是否显示(1:显示 0:不显示)',
   permission           varchar(200) comment '权限标识',
   create_by            varchar(100) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(100) comment '更新者',
   update_time          datetime comment '更新时间',
   primary key (id)
);

alter table sys_menu comment '菜单表';

/*==============================================================*/
/* Index: albumName                                                  */
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
   id                   varchar(32) not null comment 'id',
   name                 varchar(100) not null comment '角色名称',
   description          varchar(200) comment '描述',
   create_by            varchar(100) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(100) comment '更新者',
   update_time          datetime comment '更新时间',
   primary key (id)
);

alter table sys_role comment '角色表';

/*==============================================================*/
/* Index: albumName                                                  */
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
   id                   varchar(32) not null comment 'id',
   role_id              varchar(32) not null comment '角色id',
   menu_id              varchar(32) not null comment '菜单id',
   primary key (id)
);

alter table sys_role_menu comment '角色-菜单表';

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
   id                   varchar(32) not null comment 'id',
   username             varchar(100) not null comment '用户名',
   password             varchar(255) not null comment '密码',
   nick_name            varchar(100) comment '昵称',
   gender               char(1) comment '性别',
   email                varchar(100) comment '邮箱',
   phone                varchar(100) comment '电话',
   mobile_phone         varchar(100) comment '手机',
   type                 char(1) default '0' comment '用户类型(0:普通用户,1:超级管理员))',
   avatar               varchar(1000) comment '用户头像',
   enabled              char(1) default '1' comment '状态:1:启用 0:禁用',
   create_by            varchar(100) comment '创建者',
   create_time          datetime comment '创建时间',
   update_by            varchar(100) comment '更新者',
   update_time          datetime comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) not null default '0' comment '删除标记(0:未删除,1已删除)',
   primary key (id)
);

alter table sys_user comment '用户表';

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
   id                   varchar(32) not null comment 'id',
   user_id              varchar(32) not null comment '用户id',
   role_id              varchar(32) not null comment '角色id',
   primary key (id)
);

alter table sys_user_role comment '用户-角色表';

/*==============================================================*/
/* Index: user_id                                               */
/*==============================================================*/
create index user_id on sys_user_role
(
   user_id
);

/*==============================================================*/
/* Table: user_config                                           */
/*==============================================================*/
create table user_config
(
   id                   varchar(32) not null comment '用户id',
   play_mode            char(1) not null comment '播放方式',
   primary key (id)
);

alter table user_config comment '用户配置表';

/*==============================================================*/
/* Table: songlist_song                                         */
/*==============================================================*/
create table songlist_song
(
   id                   varchar(32) not null comment 'id',
   songlist_id          varchar(32) not null comment '歌单id',
   song_id              varchar(32) not null comment '歌曲id',
   primary key (id)
);

alter table songlist_song comment '歌单-歌曲表';

/*==============================================================*/
/* Index: songlist_id                                           */
/*==============================================================*/
create index songlist_id on songlist_song
(
   songlist_id
);

/*==============================================================*/
/* Table: user_playlist                                         */
/*==============================================================*/
create table user_playlist
(
   id                   varchar(32) not null comment 'id',
   user_id              varchar(32) not null comment '用户id',
   song_id              varchar(32) not null comment '歌曲id',
   primary key (id)
);

alter table user_playlist comment '用户播放列表记录';

/*==============================================================*/
/* Index: user_id                                               */
/*==============================================================*/
create index user_id on user_playlist
(
   user_id
);

/*==============================================================*/
/* Table: user_songlist                                         */
/*==============================================================*/
create table user_songlist
(
   id                   varchar(32) not null comment 'id',
   user_id              varchar(32) not null comment '用户id',
   list_name            varchar(100) not null comment '歌单名称',
   list_description     varchar(200) comment '歌单描述',
   pic_url              varchar(200) DEFAULT NULL COMMENT '歌单封面url',
   song_count           int default 0 comment '歌曲数量',
   type                 char(1) default '2' comment '歌单类型(1:我喜欢,2:自定义歌单)',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             char(1) default '0' comment '删除标记',
   primary key (id)
);

alter table user_songlist comment '用户歌单';

/*==============================================================*/
/* Index: user_id                                               */
/*==============================================================*/
create index user_id on user_songlist
(
   user_id
);