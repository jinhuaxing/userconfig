create table mt_config_group(
groupId char(100) not null primary key,
name char(100),
description char(255)
);

create table mt_config_param(
groupId char(100) not null,
name char(100) not null,
descriptiveName char(100),
description char(255),
defaultValue char(255),
value char(255),
readonly int
);

alter table mt_config_param add constraint pk_mt_config_param primary key (groupId, name);