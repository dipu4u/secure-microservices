# Auth Server

#### Get Access token for test user
\# curl -X POST http://localhost:5000/oauth/authorize -H 'Content-Type: application/json' -H 'Accept: application/json' -d '{"principal": "xxxxxxxxxx","password": "**********","twoFAToken": ""}'


#### Get User Transaction
Here request first handled by API gateway and then forwarded to transaction service. API Gateway and transaction service both validate Authorization token.<br/><br/>
\# curl -X GET http://localhost:5000/transactions/123123 -H 'Accept: application/json' -H 'Authorization: Bearer <JWT_TOKEN>'

