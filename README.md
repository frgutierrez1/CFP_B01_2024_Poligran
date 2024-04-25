Conceptos Fundamentales de Programación

Generación y clasificación de datos

Entrega_1 Semana_3

En esta entrega se diseñó e implementó un programa que al correrse genera una serie de archivos que sirvan como entrada al programa que organizará los datos mediante la implementación de la clase GenerateInfoFiles.

- La clase GenerateInfoFiles contiene arreglos como fuente de datos para los nombres, apellidos, productos y costo.
- La generación de vendedores se realiza escogiendo aleatoriamente un nombre y un apellido, la cantidad de vendedores se define en un parámetro.
- La generación de ficheros se realiza escogiendo aleatoriamente un producto y un valor (costo) aleatoriamente la cantidad de productos se define en un parámetro.
- Ambos métodos (generar vendedores y productos) contienen un hash para evitar registros duplicados.
- La generación del fichero de ventas crea un fichero para cada uno de los vendedores y le asigna aleatoriamente de 5 a 15 productos que se escriben en el fichero.

Pendiente para la Entrega_2 Semana_5

reportes de ventas especificados en los puntos 3 y 4 de la guía


Entrega_2 Semana_5

En esta entrega se diseñó e implementó la clase mainclass donde se resuelve el punto 3 de la guía, esta clase genera un fichero en /src/OutputFiles/All_Salesperson_report.csv donde se consolida el total de la venta de forma descendente por vendedor, para ello se implementó un método llamado processCSVFile donde se toman los datos del fichero individual de ventas y se realiza un VlookUp en el fichero de información de producto donde se halla el costo unitario, se hacen las respectivas operaciones matemáticas y se entrega el resultado en un fichero csv.

Pendiente para la Entrega Final:
-	Implementación del punto 4 archivo con la información de los productos vendidos por cantidad, ordenados en forma descendente.

  
  Entrega Final
  
En esta entrega se complementó la clase mainclass resolviendo el punto 4 de la guía, esta clase se incluye el método aggregateAndWriteCSV que genera un fichero en /src/OutputFiles/All_products_report.csv donde se consolida el número total de productos vendidos entre todos los vendedores de forma descendente por producto (nombre_producto;costo_unitario;cantidad_total), básicamente se toma el id del producto en el ficher base de productos y realiza un VlookUp del id del producto en todos los ficheros de ventas individuales se acumula el valor encontrado y luego se suma para obtener la cantidad total, la cual se escribe en el nuevo fichero.
