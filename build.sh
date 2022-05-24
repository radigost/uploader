mvn clean install -DskipTests;

docker build . -t uploader --platform linux/amd64

