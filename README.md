# yService -> μService -> MicroService

## About yService
_yService_ is a java framework to work using microservices. yService allows you to manage your microservices easier.

## How it works?

### Service Management Server
It's a java project web based responsible for register/unregister the microservices and to find the right microservice to be called.

### MicroService
Is a small unit of work, responsible for one single service, when deployed it has to register itself in the Service Discovery. MicroServices are java based, using Spark framework with Jetty embeded.
