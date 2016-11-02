# grails-spring-integration
prueba de concepto de integración de servicios con spring integration

# estructura
el proyecto consiste en 3 subproyectos:

## cancerbero.
grails basica con spring security rest y un usuario (jorge/aguilera) creado en el inicio

## documents
grails basica que ofrece un recurso Document vía rest. Crea 10 documentos en el inicio

## proxyweb
grails que hará de front-end con el usuario. Contiene un Service que deberían consumir los controllers (no hay)
y que simplemente delega en un Gateway de Spring Integration la coordinacion de llamadas.

En este proyecto existe un Configuration donde se definen los flows:

- login

- validate token

- dashboard

