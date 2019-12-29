create user if not exists 'writory'@'%' identified by 'my-secret-pw';
create database if not exists `writory_test`;
grant all on `writory_test`.* to 'writory'@'%';
