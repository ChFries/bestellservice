mvn clean package -DskipTests=true
docker build . --no-cache -t bestellservice:latest