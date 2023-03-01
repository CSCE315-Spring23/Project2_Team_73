-- SELECT * FROM customer

-- how many orders employee 1 took
SELECT count (*) FROM orders
WHERE employeeid = 1;

-- how many orders are over $10
SELECT count (*) FROM orders
WHERE orderprice >= 10.00;

-- how many customers are male
SELECT count (*) FROM customer
WHERE gender = 'Male';





