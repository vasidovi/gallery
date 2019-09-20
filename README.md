# Gallery
Spring Boot based backend of gallery project

## Demo
https://gallery-view.herokuapp.com/

## User registration

First to register user will be asigned roles 'USER' and 'ADMIN'. 
Other users will be asigned role 'USER'.
User's role can be changed in USER_ROLE table.

## DB access

To access DB go to:

http://localhost:8080/h2-console

<p>Driver class:  org.h2.Driver</p>
<p>JDBC URL: jdbc:h2:./demo_db/gallery2DB</p>
<p>User Name: sa</p>
<p>Password: </p>

## Changing DB 

In <code>application.properties</code> change spring.datasource.url

For demonstration purposes DB is now set to <code>./demo_db/gallery2DB</code>

<code> spring.datasource.url=jdbc:h2:./demo_db/gallery2DB </code>

To change db simply write another path and new db will be created
i.e. <code>./demo_db/new_galleryDB</code>

http://www.h2database.com/html/cheatSheet.html
