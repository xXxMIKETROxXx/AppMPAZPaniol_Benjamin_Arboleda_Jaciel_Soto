# AppMPAZPaniol_Benjamin_Arboleda_Jaciel_Soto
BACKEND_DE_PROYECTO-MPAZ

## Microservicio Inventario

Administra el inventario de equipos, sus stocks, estados y ubicaciones. La API ofrece `GET /api/inventario`, `GET /api/inventario/{equipoId}`, `POST /api/inventario`, `PUT /api/inventario/{equipoId}`, `PATCH /api/inventario/{equipoId}/estado?valor=...`, `PATCH /api/inventario/{equipoId}/stock?valor=...` y `PATCH /api/inventario/{equipoId}/baja`.

Para ejecutarlo con Docker, entra en `inventario/`, copia `.env.example` como `.env`, reemplaza el valor de ejemplo de `DB_PASSWORD` por una contraseña local y ejecuta `docker compose up --build`. La API queda disponible en `http://localhost:8081/api/inventario`; PostgreSQL se publica en el puerto `5435`.
