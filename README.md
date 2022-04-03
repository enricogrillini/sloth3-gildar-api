# Spring-boot-rest-api

## Link principali
- **Swagger UI** - http://localhost:8082/swagger-ui.html
- **Api doc**
    - http://localhost:8082/v3/api-docs
    - http://localhost:8082/v3/api-docs.yaml

## ENV
      LC_ALL: en_US.UTF-8
      TZ: Europe/Rome


## Curl per provare il servizio (su windows usare gitbash)

```shell
# getDocuments 
curl "http://localhost:8082/api/v1/document" -s

# getDocuments 
curl "http://localhost:8082/api/v1/document/doc-1" -s

# delete 
curl -X DELETE "http://localhost:8082/api/v1/document/doc-1" -s

# post
curl -X POST "http://localhost:8082/api/v1/document" -d "{\"id\":\"doc-4\",\"name\":\"Appendice\",\"description\":\"Appendice 2\"}" -H "accept: application/json" -H "Content-Type: application/json" -s 

# put
curl -X PUT "http://localhost:8082/api/v1/document" -d "{\"id\":\"doc-4\",\"name\":\"Appendice\",\"description\":\"Appendice 2 - correzione\"}" -H "accept: application/json" -H "Content-Type: application/json" -s
```
