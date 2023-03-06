/*1) How much is there in total sales? */  
SELECT SUM(orderprice) FROM orders;
/*2) How many managers are there? */ 
SELECT count(*) FROM employees 
WHERE isadmin = True; 
/*3) How many regular employees are there? */
SELECT count(*) FROM employees 
WHERE isadmin = False;  
/*4) How much in sales on October 22(Gameday) ?*/
SELECT sum(orderprice) FROM orders
WHERE ordertime  >= '2022-10-22'
AND ordertime < '2022-10-23';
/*5) How many large cups are left in inventory ?*/
SELECT numitems FROM inventory
WHERE itemname = 'large cup';
/*6) How many unique customers are there ?*/
SELECT count(*) from customer;
/*7) What is the price of Apple Kiwi Kale 20oz ?*/
SELECT itemprice FROM menu
WHERE itemname = 'Apple Kiwi Kale 20oz';
/*7) How many orders did employee 1 take ?*/
SELECT COUNT(*) FROM orders
WHERE employeeid = 1;
/*7) How many were over $10 ?*/
SELECT COUNT(*) FROM orders
WHERE orderprice > 10.00;
/*8) How many were in January ?*/
SELECT COUNT(*) FROM orders
WHERE ordertime < '2022-02-01';
/*9) How many customers were Male ?*/
SELECT COUNT(*) FROM customer
WHERE gender = 'Male';
/*10) How many different items do we need to account for in inventory?*/
SELECT COUNT(*) FROM inventory;
/*11) How many unique items on menu(20oz vs 32oz are unique) ?*/
SELECT COUNT(*) FROM menu;
/*12) How many employees make under $20 an hour?*/
SELECT COUNT(*) FROM employees
WHERE hourlyrate < 20.0 ;
/*13)Is John an admin?*/
SELECT isadmin FROM employees
WHERE firstname = 'john';
/*14)Most expensive menu item?*/
SELECT MAX(itemprice) from menu;
/*15)Cheapest menu item?*/
SELECT MIN(itemprice) from menu;
/*16)sales by customer?  */
select sum(orderprice) from orders
GROUP BY customerid; 


