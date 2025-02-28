# PinApp Prueba Cliente

## Descripción

Un breve proyecto de pruebas para implementar una arquitectura hexagonal, patrones como el repository y rest, y principios como el solid
Se busca poder realizar algunas operaciones tipo CRUD, ademas de algun calculo derivado de los datos de negocio de Cliente, como un promedio de la edad de los clientes guardados asi como una posible esperanza de vida restante

Se tienen contempladas la posibilidad de enviar la solicitud de creacion del cliente a traves de un topico, y su posterior escucha y ejecucion con un consumer, esto usando Kafka
Se utiliza un JWT como metodo de autorizacion para acceder a los servicios, donde en este caso, la kley, el usuario, y el password se tienen en el archivo properties, tema que en un entorno mas productivo seria derivado al uso de secrets, mismo caso que con el uso de la BDD h2 que en su eventual pase de entorno seria configurado en un RDS tambien con el uso de secrets para la autenticacion y el host
Se documenta con swagger(openapi)
Se dejo contemplada la configuracion para un eventual monitoreo en Grafana, junto con prometheus
Se realizaron los tests unitarios e integrales pertinentes

Se utilizo la arquitectura hexagonal dado el contexto de microservicio, se realizan los mapeos con mapStruct y no con un patron builder dada la simplesa de las operaciones a realizar con los objetos
Se contempla un eventual despliegue en EC2 basico, la configuracion de bdd y de la cola de mensajeria se podria eventualmente adaptar a un RDS y Lambda(con version paga de aws), la asignacion de claves y grupo de seguridad se realizo para un entorno abierto, es decir, no solo localhost sino  que cualquiera lo pueda ejecutar ( host 0.xxx) y tambien se realizaron las pruebas de este despliegue a travez de WinSCP para la gestion del .jar, y putty para el levantamiento del servidor


## Instalación

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com/kpinedaperez2/PinApp.git
   ```

2. Levanta las dependencias gradle con ".gradlew build"
3. Ejecuta el proyecto
4. Con postman ( u otra herramienta para la consulta y ejecucion de curls) ejecutar los curl correspondientes

Login:
   ```
curl --location 'http://localhost:8080/api/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=FA22C5C1562217DC36046CA4B4D91642' \
--data '{"username": "user", "password": "userpass"}'
```

   Creacion:
   ```
curl --location 'http://localhost:8080/api/registrar' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzQwNzExMTM5LCJleHAiOjE3NDA3NDcxMzl9.WsDAcCLzWyggFk0Sp_V9NaurzjQOHiDrI0_WGRtlfsM' \
--header 'Cookie: JSESSIONID=FA22C5C1562217DC36046CA4B4D91642' \
--data '{
  "nombre": "Juan",
  "apellido": "Pérez",
  "edad": 30,
  "fechaNacimiento": "1994-02-27"}'
  ```

   Consulta de general:
```
curl --location 'http://localhost:8080/api/listar' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzQwNzExMTM5LCJleHAiOjE3NDA3NDcxMzl9.WsDAcCLzWyggFk0Sp_V9NaurzjQOHiDrI0_WGRtlfsM' \
--header 'Cookie: JSESSIONID=FA22C5C1562217DC36046CA4B4D91642'
```

   Consulta de metricas:
```
curl --location 'http://localhost:8080/api/metricas' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzQwNzExMTM5LCJleHAiOjE3NDA3NDcxMzl9.WsDAcCLzWyggFk0Sp_V9NaurzjQOHiDrI0_WGRtlfsM' \
--header 'Cookie: JSESSIONID=FA22C5C1562217DC36046CA4B4D91642'
```

5. En caso de querer levantar la instancia en EC2 ( Ya teniendo usuario AWS free ):
  * Descargar e instalar Putty
  * Descargar e instalar WinSCP
  * Levantar la instancia EC2 y copiar la ip publica
  * Iniciar WinSCP y colocar el host anterior, si se creo la Key de la instancia con un password, colocarlo en la ventana inicial y aceptar, en caso de un mensaje de advertencia tambien aceptar
  * Asociar el .jar del proyecto con host
  * En la raiz de Putty, ejecutar puttygen y asociar la clave .pem al host, con esto se generara un archivo .ppk
  * Ejecutar putty, colocar el host, y en la parte de autenicaciones cargar el archivo .ppk generado, luego pulsar en "open"
  * Ejecutar ```java -jar <jar del proyecto>``` en el bash abierto
  * Realizar las consultas a los curl adaptandolo de la siguiente forma ```https://<host publico de la instancia>:8080```

<!-- CONTRIBUTING -->
## Contribuciones

Contribuciones y recomendaciones para ayudar a crecer **Muy apreciadas**.

1. Realiza el fork del proyecto
2. Crea una rama feature (`git checkout -b feature/rama1`)
3. Realiza el Commit de los cambios (`git commit -m 'Cambios en la rama1'`)
4. Push a la rama (`git push origin feature/rama1`)
5. Realiza el pull request

