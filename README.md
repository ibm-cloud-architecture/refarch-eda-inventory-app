# Refarch EDA Inventory app project

This project uses [Quarkus](https://quarkus.io/), Hibernate with Panache and DB2 JDBC driver and support the CRUD operations for the following integrated entities:

* Inventory
* Stores
* Items

The application is used to support different labs for DB2 and Kafka integration.

Updated 11/04/2020: Quarkus 1.9.1 with new db2 datasource.

The code is a based on [the SIMPLIFIED HIBERNATE ORM WITH PANACHE guide](https://quarkus.io/guides/hibernate-orm-panache).

## Create a DB2 service on IBM Cloud

To be able to run this application, defines a DB2 instance with free plan and get service credentials with manager role. Then define environment variables to match the passworkd, ssldsn and user, in a `.env` file

```
export DBPWD=...t
export SSLJDBCURL=jdbc:db2://da.....1.services.dal.bluemix.net:50001/BLUDB:sslConnection=true;
export DBUSER=wsp...
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell
source .env
./mvnw quarkus:dev
```

This will create the database schema and insert store and item records that you can access via the APIs


## Access to the API

Using the OpenApi definition at the following URL: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/) you can use the GET on /stores or GET on /items to see existing content.


## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `inventory-app-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/inventory-app-1.0.0-SNAPSHOT-runner.jar`.

## Build and run with docker

Under the root folder of this project do:

```shell
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t ibmcase/eda-inventory-app:1.0.0 .
docker run -i --rm -p 8080:8080 ibmcase/eda-inventory-app:1.0.0
docker push ibmcase/eda-inventory-app:1.0.0 
```

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/inventory-app-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.