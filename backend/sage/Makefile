install:
	mvn clean install

run-api:
	docker build -t sage-api -f Dockerfile .
	docker run -it -e PORT=8080 -p 8080:8080 sage-api

