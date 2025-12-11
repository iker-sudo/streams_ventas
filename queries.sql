-- queries.sql
-- Consultas SQL equivalentes a los streams de Java implementados en VentasApplicationTests.java

-- TEST 1: Devuelve un listado de todos los pedidos que se realizaron durante el año 2017,
-- cuya cantidad total sea superior a 500€.
SELECT *
FROM pedido
WHERE YEAR(fecha) = 2017 AND total > 500;

-- Contar pedidos
SELECT COUNT(*)
FROM pedido
WHERE YEAR(fecha) = 2017 AND total > 500;

-- IDs de pedidos
SELECT id
FROM pedido
WHERE YEAR(fecha) = 2017 AND total > 500
ORDER BY id;


-- TEST 2: Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido.
SELECT c.id
FROM cliente c
LEFT JOIN pedido p ON c.id = p.id_cliente
WHERE p.id IS NULL
ORDER BY c.id;


-- TEST 3: Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
SELECT MAX(comision) AS comision_maxima
FROM comercial;


-- TEST 4: Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL.
-- El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
SELECT id, nombre, apellido1
FROM cliente
WHERE apellido2 IS NOT NULL
ORDER BY apellido1, apellido2, nombre;


-- TEST 5: Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o".
-- Tenga en cuenta que se deberán eliminar los nombres repetidos.
SELECT DISTINCT nombre
FROM comercial
WHERE nombre LIKE '%el' OR nombre LIKE '%o';


-- TEST 6: Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017,
-- cuya cantidad esté entre 300 € y 1000 €.
SELECT DISTINCT c.*
FROM cliente c
INNER JOIN pedido p ON c.id = p.id_cliente
WHERE YEAR(p.fecha) = 2017 AND p.total BETWEEN 300 AND 1000;


-- TEST 7: Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
SELECT AVG(p.total) AS media_pedidos
FROM pedido p
INNER JOIN comercial c ON p.id_comercial = c.id
WHERE c.nombre = 'Daniel' AND c.apellido1 = 'Sáez';


-- TEST 8: Devuelve un listado con todos los pedidos que se han realizado.
-- Los pedidos deben estar ordenados por la fecha de realización, mostrando en primer lugar los pedidos más recientes
SELECT *
FROM pedido
ORDER BY fecha DESC;


-- TEST 9: Devuelve todos los datos de los dos pedidos de mayor valor.
SELECT *
FROM pedido
ORDER BY total DESC
LIMIT 2;


-- TEST 10: Devuelve un listado con los identificadores de los clientes que han realizado algún pedido.
-- Tenga en cuenta que no debe mostrar identificadores que estén repetidos.
SELECT DISTINCT id_cliente
FROM pedido
ORDER BY id_cliente;


-- TEST 11: Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.
SELECT nombre, apellido1, apellido2
FROM comercial
WHERE comision BETWEEN 0.05 AND 0.11;


-- TEST 12: Devuelve el valor de la comisión de menor valor que existe para los comerciales.
SELECT MIN(comision) AS comision_minima
FROM comercial;


-- TEST 13: Devuelve un listado de los nombres de los comerciales que
-- empiezan por A y terminan por n y también los nombres que empiezan por P.
-- El listado deberá estar ordenado alfabéticamente.
SELECT DISTINCT nombre
FROM comercial
WHERE (nombre LIKE 'A%' AND nombre LIKE '%n') OR nombre LIKE 'P%'
ORDER BY nombre;


-- TEST 14: Devuelve un listado de los nombres de los clientes
-- que empiezan por A y terminan por n y también los nombres que empiezan por P.
-- El listado deberá estar ordenado alfabéticamente.
SELECT DISTINCT nombre
FROM cliente
WHERE (nombre LIKE 'A%' AND nombre LIKE '%n') OR nombre LIKE 'P%'
ORDER BY nombre;


-- TEST 15: Devuelve un listado de los clientes cuyo nombre no empieza por A.
-- El listado deberá estar ordenado alfabéticamente por nombre y apellidos.
SELECT *
FROM cliente
WHERE nombre NOT LIKE 'A%'
ORDER BY nombre, apellido1, apellido2;


-- TEST 16: Devuelve un listado con el identificador, nombre y los apellidos de todos
-- los clientes que han realizado algún pedido.
-- El listado debe estar ordenado alfabéticamente por apellidos y nombre y se deben eliminar los elementos repetidos.
SELECT DISTINCT c.id, c.nombre, c.apellido1, c.apellido2
FROM cliente c
INNER JOIN pedido p ON c.id = p.id_cliente
ORDER BY c.apellido1, c.apellido2, c.nombre;


-- TEST 17: Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente.
-- El resultado debe mostrar todos los datos del cliente primero junto con un sublistado de sus pedidos.
-- El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre y apellidos.
SELECT c.*, p.*
FROM cliente c
LEFT JOIN pedido p ON c.id = p.id_cliente
ORDER BY c.nombre, c.apellido1, c.apellido2;


-- TEST 18: Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial.
-- El resultado debe mostrar todos los datos de los comerciales y el sublistado de pedidos.
-- El listado debe mostrar los datos de los comerciales ordenados alfabéticamente por apellidos.
SELECT co.*, p.*
FROM comercial co
LEFT JOIN pedido p ON co.id = p.id_comercial
ORDER BY co.apellido1, co.apellido2, co.nombre;


-- TEST 19: Devuelve el nombre y los apellidos de todos los comerciales que ha participado
-- en algún pedido realizado por María Santana Moreno.
SELECT DISTINCT co.nombre, co.apellido1, co.apellido2
FROM comercial co
INNER JOIN pedido p ON co.id = p.id_comercial
INNER JOIN cliente c ON p.id_cliente = c.id
WHERE c.nombre = 'María' AND c.apellido1 = 'Santana' AND c.apellido2 = 'Moreno';


-- TEST 20: Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.
SELECT co.*
FROM comercial co
LEFT JOIN pedido p ON co.id = p.id_comercial
WHERE p.id IS NULL;


-- TEST 21: Calcula el número total de comerciales distintos que aparecen en la tabla pedido
SELECT COUNT(DISTINCT id_comercial) AS total_comerciales
FROM pedido;


-- TEST 22: Calcula el máximo y el mínimo de total de pedido
SELECT MAX(total) AS maximo, MIN(total) AS minimo
FROM pedido;


-- TEST 23: Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en cliente
SELECT ciudad, MAX(categoria) AS categoria_maxima
FROM cliente
WHERE ciudad IS NOT NULL AND categoria IS NOT NULL
GROUP BY ciudad;


-- TEST 24: Calcula cuál es el máximo valor de los pedidos realizados
-- durante el mismo día para cada uno de los clientes.
SELECT c.id, c.nombre, c.apellido1, c.apellido2, p.fecha, MAX(p.total) AS max_total
FROM cliente c
INNER JOIN pedido p ON c.id = p.id_cliente
GROUP BY c.id, c.nombre, c.apellido1, c.apellido2, p.fecha;


-- TEST 25: Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes,
-- teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.
SELECT c.id, c.nombre, c.apellido1, c.apellido2, p.fecha, MAX(p.total) AS max_total
FROM cliente c
INNER JOIN pedido p ON c.id = p.id_cliente
WHERE p.total > 2000
GROUP BY c.id, c.nombre, c.apellido1, c.apellido2, p.fecha;


-- TEST 26: Devuelve un listado con el identificador de cliente, nombre y apellidos
-- y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.
SELECT c.id, c.nombre, c.apellido1, c.apellido2, COUNT(p.id) AS total_pedidos
FROM cliente c
INNER JOIN pedido p ON c.id = p.id_cliente
WHERE YEAR(p.fecha) = 2017
GROUP BY c.id, c.nombre, c.apellido1, c.apellido2
HAVING COUNT(p.id) > 0;


-- TEST 27: Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año.
-- El listado debe mostrarse ordenado por año.
SELECT YEAR(fecha) AS anio, id, MAX(total) AS max_total
FROM pedido
GROUP BY YEAR(fecha)
ORDER BY anio;

-- Alternativa más completa que muestra el pedido específico
SELECT p.*
FROM pedido p
INNER JOIN (
    SELECT YEAR(fecha) AS anio, MAX(total) AS max_total
    FROM pedido
    GROUP BY YEAR(fecha)
) max_pedidos ON YEAR(p.fecha) = max_pedidos.anio AND p.total = max_pedidos.max_total
ORDER BY YEAR(p.fecha);


-- TEST 28: Devuelve el número total de pedidos que se han realizado cada año.
SELECT YEAR(fecha) AS anio, COUNT(*) AS total_pedidos
FROM pedido
GROUP BY YEAR(fecha)
ORDER BY anio;


-- TEST 29: Devuelve los datos del cliente que realizó el pedido más caro en el año 2019.
SELECT c.*
FROM cliente c
INNER JOIN pedido p ON c.id = p.id_cliente
WHERE YEAR(p.fecha) = 2019
ORDER BY p.total DESC
LIMIT 1;


-- TEST 30: Calcula la estadísticas de total de todos los pedidos.
SELECT
    COUNT(*) AS count,
    SUM(total) AS sum,
    MIN(total) AS min,
    AVG(total) AS average,
    MAX(total) AS max
FROM pedido;

