create table excel_upload_status (
                                     is_excel_uploaded bit,
                                     created_at datetime(6),
                                     excel_upload_status_id bigint not null auto_increment,
                                     updated_at datetime(6),
                                     user_id varchar(255),
                                     year_month_str varchar(255),
                                     primary key (excel_upload_status_id)
) engine=InnoDB;

alter table if exists excel_upload_status
    add constraint FKeas338yr7k715yf07wgqv2ypy
    foreign key (user_id)
    references users (user_id);