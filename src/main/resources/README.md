#### Создание БД на локальной машине разработчика

Установить PostgreSQL (достаточно версии 11)

Выполнить скрипты

- create database mayakenergy;
 
- create user mayakenergy with password 'mayakenergy';
 
- grant all privileges on database mayakenergy to mayakenergy;

mvn flyway:migrate -DDB_HOST=localhost -DDB_PORT=5432 -DDB_BASE=mayakenergy -DDB_USERNAME=mayakenergy -DDB_PASSWORD=mayakenergy