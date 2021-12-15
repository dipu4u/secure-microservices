# Secure Microservices
This demo project contains Microservices 1.0 system.
<br/>
As per Microservices 1.0 architecture pattern it has below infrastructure related capabilities like, Discovery service and API Gateway.
<br/>
Project has below modules/applications.<br/>

##### Service Registry
This application discovers deployed micro services in eco-system. It uses Spring cloud eureka server.
<br/>
##### API Gateway
This application provides request routing, load balancing, authentication and authorization, circuit breaker etc functionalities. It uses Spring cloud gateway.
<br/>
##### Auth Server
This application provides authentication service to external and internal requests and token provides JWT token. It uses Spring boot and as backbone.
<br/>
#### How to build all applications?

Here, I have uses Apache Maven as build tool which supports profiling, multi modules build and many other features.
<br/>
\# cd secure-microservices <br/>
\# cd mvn clean install

##### Create Private key to sign JWT token
\# openssl genrsa -out latestpem.pem 2048
\# openssl pkcs8 -topk8 -inform PEM -outform PEM -in latestpem.pem -out latestpriv8.pem -nocrypt
\# openssl rsa -pubout -in latestpriv8.pem -out latest_public.pem
