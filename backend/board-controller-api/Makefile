install:
	mvn clean install

PROTOC_GEN_GRPC_JAVA=$(shell which protoc-gen-grpc-java)
PROTO_DIR=src/main/proto
OUT_DIR=src/main/java

gen-proto:
	mkdir -p $(OUT_DIR)
	protoc --plugin=protoc-gen-grpc-java=$(PROTOC_GEN_GRPC_JAVA) \
	       --grpc-java_out=$(OUT_DIR) \
		   --java_out=$(OUT_DIR) \
	       --proto_path=$(PROTO_DIR) \
	       $(PROTO_DIR)/*.proto

run-api:
	docker build -t board-controller-api -f Dockerfile .
	docker run -it -e PORT=8080 -p 8080:8080 board-controller-api

