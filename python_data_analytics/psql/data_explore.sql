-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) FROM retail;

--Q3: Number of clients(unique customer_id)
SELECT count(DISTINCT customer_id) FROM retail;

--Q4:invoice date range
SELECT max(invoice_date), min(invoice_date ) FROM retail;

--Q5:number of SKU/merchants (e.g unique stock code)
SELECT count(DISTINCT stock_code) FROM retail;

--Q6: Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT AVG(invoice_amount)
FROM (
		SELECT invoice_no, SUM(quantity * unit_price) AS invoice_amount
		FROM retail
			GROUP BY invoice_no
			HAVING SUM(quantity * unit_price) > 0
	) r;

--Q7: Calculate total revenue
SELECT sum(unit_price * quantity) FROM retail;

--Q8: Calculate total revenue by YYYYMM
SELECT (cast(extract(year from invoice_date) as integer) * 100 +
			cast(extract (month from invoice_date) as integer)) AS yyyymm,
		sum(unit_price * quantity)
		FROM retail
		GROUP BY yyyymm
		ORDER BY yyyymm;


