# court-booking

This is a small Java Swing application that allows an admin at a sports club book courts for members.

The program connects to a MySQL database, for the purposes of data storage and retrieval.


## How to Run

1. Enter your database details in `src/main/resources/config.properties` 

2. Run the SQL script in mysql using 
```
source creation-script.sql
```

3. The project can then be built using the Maven command line tool by navigating to the directory and calling
```
mvn package
```

4. Application can then be ran via 
```
java -cp target/court-booking-1.0-SNAPSHOT.jar
```

