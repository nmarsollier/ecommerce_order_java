### Si queres sabes mas sobre mi:
[Nestor Marsollier](https://github.com/nmarsollier/profile)

# Order Service en Java

Order Service en Java reemplaza la version realizada en Python del proyecto  [ecommerce](https://github.com/nmarsollier/ecommerce).

El motivo de este proyecto es para poder modelar de otra forma el mismo proyecto para poder mostrar ejemplos académicos.

Es un proyecto que implementa el patron Event Sourcing y Command and Query Responsibility Separation para almacenar los distintos eventos que se producen en las ordenes. Maneja algunas proyecciones como ejemplos.

[Documentación de API](./README-API.md)

La documentación de las api también se pueden consultar desde el home del microservicio
que una vez levantado el servidor se puede navegar en [localhost:3004](http://localhost:3004/)

## Dependencias

### Auth

Las ordenes sólo puede usarse por usuario autenticados, ver la arquitectura de microservicios de [ecommerce](https://github.com/nmarsollier/ecommerce).

### Catálogo

Las ordenes tienen una fuerte dependencia del catalogo:

- Se validan los artículos contra el catalogo.
- Se descuentan los artículos necesarios.
- Se puede devolver articulos si la orden se cancela.

Ver la arquitectura de microservicios de [ecommerce](https://github.com/nmarsollier/ecommerce).

### Cart

Las ordenes comienza en el cart, cart emite un evento place-order, a través de rabbit.

### MongoDb

Ver tutorial de instalación en [ecommerce](https://github.com/nmarsollier/ecommerce) en la raíz.

### RabbitMQ

La comunicación de eventos es a través de rabbit en la mayoría de los casos.

Ver tutorial de instalación en [ecommerce](https://github.com/nmarsollier/ecommerce) en la raíz.

### Java

Puede ser open jdk 11 o oracle 1.8+
Java JDK 1.8  [oracle.com](http://www.oracle.com/technetwork/es/java/javase/downloads/index.html)

Gradle [gradle.org](https://gradle.org/install/).

Establecer las variables de entorno sujeridas en las instalaciones.
Tanto los ejecutables de java, como gradle deben poder encontrarse en el path.

## Ejecución del servidor

Se clona el repositorio en el directorio deseado.

Nos paramos en la carpeta donde se encuentra el archivo build.gradle y ejecutamos :

```bash
gradle run
```

La primera vez que ejecute descarga las dependencias, puede tardar un momento.

## Apidoc

Apidoc es una herramienta que genera documentación de apis para proyectos node (ver [Apidoc](http://apidocjs.com/)).

El microservicio muestra la documentación como archivos estáticos si se abre en un browser la raíz del servidor [localhost:3004](http://localhost:3004/)

Ademas se genera la documentación en formato markdown.

Para que funcione correctamente hay que instalarla globalmente con

```bash
npm install apidoc -g
npm install -g apidoc-markdown2
```

La documentación necesita ser generada manualmente ejecutando la siguiente linea en la carpeta raíz :

```bash
apidoc -o www
apidoc-markdown2 -p www -o README-API.md
```

Esto nos genera una carpeta www con la documentación, esta carpeta debe estar presente desde donde se ejecute el proyecto, aunque se puede configurar desde el archivo de properties.

## Configuración del servidor

Este servidor se configura con variables de entorno

SERVER_PORT = Puerto del servidor (3004)
AUTH_SERVICE_URL = Servidor Auth (http://localhost:3000)
RABBIT_URL = Rabbit (localhost)
MONGO_URL = Url de mongo (localhost)
WWW_PATH = Path documentación (www)

Este archivo permite configurar parámetros del servidor, ver ejemplos en config-example.json.

## Docker

### Build

```bash
docker build --no-cache -t dev-order-java .
```

### El contenedor

```bash
# Mac | Windows
docker run -it --name dev-order-java -p 3004:3004 -v $PWD:/app dev-order-java

# Linux
docker run -it --add-host host.order.internal:172.17.0.1 --name dev-order-java -p 3004:3004 -v $PWD:/app dev-order-java
```
