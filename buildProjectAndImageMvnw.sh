chmod 777 mvnw
./mvnw clean package -DskipTests=true
docker build . --no-cache -t bestellservice:latest