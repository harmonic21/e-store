cd ../simple-electronic-store
mvn clean install -DskipTests
cd ../payment-service
mvn clean install -DskipTests
cd ../extra
docker-compose build
docker-compose up
