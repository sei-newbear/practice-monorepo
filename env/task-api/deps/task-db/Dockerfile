FROM postgres:15.1-bullseye

COPY init-db /docker-entrypoint-initdb.d
ENV POSTGRES_DB=task_db
ENV POSTGRES_USER=task_user
ENV POSTGRES_PASSWORD=task_pw