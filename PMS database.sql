use pmsdb;

create table company(c_no varchar(10) not null,c_name varchar(255),
address varchar(500),phone varchar(12));

alter table company add primary key(c_no);

create table drug(d_code varchar(10) not null,d_name varchar(255),
d_type varchar(50),barcode char(13),c_price decimal(50,2),
s_price decimal(50,2),m_date date,ex_date date,qty int,
primary key(barcode));

create table customer(cust_no char(10) not null,phone char(12),dob date,
address varchar(500),c_name varchar(255), primary key(cust_no));

create table em_user(emp_no char(5) not null,u_name varchar(255),phone char(12),
u_type varchar(20),address varchar(500), primary key(emp_no));

alter table em_user add column emp_password varchar(30) not null;

create table purchase(p_no char(20) not null, batch_no varchar(50) not null,
c_no varchar(10),barcode char(13), emp_no char(5), qty int, p_time time,
p_date date, primary key(p_no),
constraint FK_UserCompany foreign key(c_no) references company(c_no),
constraint FK_UserDrug foreign key(barcode) references drug(barcode),
constraint FK_UserEmp foreign key(emp_no) references em_user(emp_no));

create table bill(bill_no char(20) not null, tot_ammount decimal(10,2),
barcode char(13), cust_no char(10), primary key(bill_no),
constraint FK_BillDrug foreign key(barcode) references drug(barcode),
constraint FK_BillCust foreign key(cust_no) references customer(cust_no));

create table sale(qty int, b_date date, amount decimal(10,2),
b_time time, barcode char(13), cust_no char(10), primary key(barcode,cust_no),
constraint FK_SaleDrug foreign key(barcode) references drug(barcode),
constraint FK_SaleCust foreign key(cust_no) references customer(cust_no));