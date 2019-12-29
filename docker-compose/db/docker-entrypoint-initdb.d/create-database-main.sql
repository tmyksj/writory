create user if not exists 'writory'@'%' identified by 'my-secret-pw';
create database if not exists `writory_main`;
grant all on `writory_main`.* to 'writory'@'%';
