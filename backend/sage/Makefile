install:
	mvn clean install

run-application:
	docker compose -f docker-compose-run-application.yaml down
	docker compose -f docker-compose-run-application.yaml stop
	docker compose -f docker-compose-run-application.yaml build --force-rm
	docker compose -f docker-compose-run-application.yaml up

run-database:
	docker compose -f docker-compose-run-database.yaml down
	docker compose -f docker-compose-run-database.yaml stop
	docker compose -f docker-compose-run-database.yaml build --force-rm
	docker compose -f docker-compose-run-database.yaml up

run-api:
	docker build -t sage-api -f Dockerfile .
	docker run -it -e PORT=8080 -p 8080:8080 sage-api