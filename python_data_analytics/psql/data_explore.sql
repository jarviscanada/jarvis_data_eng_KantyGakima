-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) FROM retail;

--Q3: Number of clients(unique customer_id)
select count(DISTINCT customer_id) from retail;


--Q4:invoice date range
select max(invoice_date), min(invoice_date ) from retail;

--Q5:number of SKU/merchants (e.g unique stock code)
select count(DISTINCT stock_code) from retail;

--Q6: Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)  # to do
select avg(retail.unit_price * retail.quantity) from retail
group by retail.invoice_no
having retail.unit_price >= 0;

--Q7: Calculate total revenue
select sum(unit_price * quantity) from retail;

--Q8: Calculate total revenue by YYYYMM
select (cast(extract(year from invoice_date) as integer) * 100 +
			cast(extract (month from invoice_date) as integer)) as yyyymm,
		sum(unit_price * quantity)
		from retail
		group by yyyymm
		order by yyyymm;


