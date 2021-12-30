CREATE TABLE order_item (
  id SERIAL PRIMARY KEY,
  requester_id varchar(100),
  description varchar(255),
  deadline date,
  money_value numeric (20, 2),
  time_value numeric (20, 2)
);