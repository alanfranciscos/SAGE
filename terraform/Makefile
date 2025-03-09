MODULE_NAME=artifact-registry

run-terraform:
	docker run -it -v $(shell pwd)/modules/$(MODULE_NAME) :/app -w /app --entrypoint "" hashicorp/terraform:1.11.1 sh


# export GOOGLE_APPLICATION_CREDENTIALS=gcp_cred.json