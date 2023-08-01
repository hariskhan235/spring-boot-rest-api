-- Drop the database and user (if they exist)
-- Be cautious when running this in production as it will delete the database and user
-- drop database if exists expensetrackerdb;
-- drop user if exists expensetracker;

-- Create the user and database
drop database expensetrackerdb;
drop user expensetracker;
create user expensetracker with password 'password';
create database expensetrackerdb with owner = expensetracker;

-- Connect to the newly created database
\c expensetrackerdb;

-- Set default privileges for the user
alter default privileges in schema public grant all on tables to expensetracker;
alter default privileges in schema public grant all on sequences to expensetracker;

-- Create the test_users table
create table test_users (
  user_id serial primary key,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  email varchar(50) not null,
  password text not null
);

-- Create the test_categories table
create table test_categories (
  category_id serial primary key,
  user_id integer not null,
  title varchar(20) not null,
  descriptionn varchar(50) not null,
  foreign key (user_id) references test_users(user_id)
);

-- Create the test_transactions table
create table test_transactions (
  transation_id serial primary key,
  category_id integer not null,
  user_id integer not null,
  amount numeric(10,2) not null,
  note varchar(50) not null,
  transaction_date bigint not null,
  foreign key (category_id) references test_categories(category_id),
  foreign key (user_id) references test_users(user_id)
);

-- Create the sequences
create sequence test_users_seq start 1;
create sequence test_categories_seq start 1;
create sequence test_transactions_seq start 1000;

