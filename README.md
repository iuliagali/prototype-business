# prototype-business application
It has an API that provides a Place entity by Id and shows information about its name, address and opening hours.

The master data is fetched from an external service: https://storage.googleapis.com/coding-session-rest-api/{place_id} and procesed in order to comply with the agreed business logic.

# Backend service
Prototype application using ports and adapter architecture.
It has error handling for the client side, in case of server error or client errors.

There is in place a configuration for error handling designed for application specific errors.
