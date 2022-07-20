### Instructions
- This app is can be used using Java 8/11
- This is running using H2 Database, an In-Memory database which means that restarting the app will restart the data
- to run, first execute "mvn clean install" then "mvn spring-boot:run"
- I have added a postman collection for authentication and task endpoint: https://github.com/plurantee/togo/blob/master/postman%20collection/Togo%20Collection.postman_collection.json
- default user is "florante" and password is "password"
- Sample curl command for user registration:
  ``
  curl --location --request POST 'http://localhost:8080/register' \
  --header 'Content-Type: application/json' \
  --header 'Cookie: JSESSIONID=07446863A868BC86414DAFC270DAC644' \
  --data-raw '{
  "username": "user123",
  "password": "password",
  "limit": 2
  }'
  ``
- Sample login - this will return a token response, and it must be used for the endpoints:
  ``
  curl --location --request POST 'http://localhost:8080/login' \
  --header 'Content-Type: application/json' \
  --header 'Cookie: JSESSIONID=07446863A868BC86414DAFC270DAC644' \
  --data-raw '{
  "username": "florante",
  "password":  "password"
  }'
  ``