# Building a webchat on Microservices


This is a collection of microservices that run a full stack web chat messenger. This includes : 
* Eureka Server (Naming server)
* ZUUL Gateway (Load Balancer and Distributor)
* Front End Client on WebSocket (Javascript)
* Back End Server on Spring Framework (Java)



## Deploying

* Import pom.xml file from each service (4 of them in total)
* Launch them in this order : 
  
1) Run **Eureka Server**
   * Check that your instance is running at [http://localhost:8761](http://localhost:8761/)
2) Run **chat-websocket**
   * Check that you instance is listed at [http://localhost:8761](http://localhost:8761/)


3) **Preparing the Database**

    ###  Using Docker to set up the database instance

    For more information check the Postgres tutorial on [DockerHub](https://hub.docker.com/_/postgres)



   ```bash
   # Create a Postgres container
   docker run --name postgres-0 -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres

   # Obtain a shell to the docker container

   # Windows :
   winpty docker exec -it postgres-0 bash  

   # Linux / MacOS
   docker exec -it postgres-0 bash

   # Log into PostgresSQL
   psql -U postgres    

   # Create the database (only the first time)
   CREATE DATABASE thesischat

   \c thesischat       # Connect to database called thesischat
   ```

4) Run **chat-db**
   * Make sure that you followed the previous steps correctly, and have a postgres instance running.
   * There is a file in `resources/db.migration` that will take care of creating the tables automatically.
   * **Don't tinker with the tables** unless you add a new file named `resources/db.migration/V*__MODIFIED.sql` with * being last number +1. Otherwise the app **won't run**. 
   * In case you mess things up you can **just delete the database** and start over. (You don't have to change anything in the container.)
   * Check that you instance is listed at [http://localhost:8761](http://localhost:8761/) You should now have 2 instances.

5) Run **ZUUL Gateway**
   * Check that you instance is listed at [http://localhost:8761](http://localhost:8761/) You should now have 3 instances.
   * You should now be able to check both the websocket client http://localhost:8762/chat
   * And the backend server (just the past chat log) http://localhost:8762/api/chatlog


### Notes : 
* The **Eureka Server** that you can monitor all your services is at http://localhost:8761/
* To access the actual services you go through the **ZUUL Gateway** at http://localhost:8762/ + the name of the service. /chat is the websocket client, whereas /api/chatlog is the api endpoint for the backend database.