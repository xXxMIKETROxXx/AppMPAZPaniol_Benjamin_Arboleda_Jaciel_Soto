# AppMPAZPaniol_Benjamin_Arboleda_Jaciel_Soto
BACKEND_DE_PROYECTO-MPAZ

## Microservicio Equipos

Administra el catálogo de equipos y permite consultarlos por identificador o categoría. La API ofrece `GET /api/equipos`, `GET /api/equipos/{id}`, `GET /api/equipos/categoria/{categoria}`, `POST /api/equipos` y `PUT /api/equipos/{id}`.

Para ejecutarlo con Docker, entra en `equipos/`, copia `.env.example` como `.env`, reemplaza el valor de ejemplo de `DB_PASSWORD` por una contraseña local y ejecuta `docker compose up --build`. La API queda disponible en `http://localhost:8082/api/equipos`; PostgreSQL se publica en el puerto `5436`.
