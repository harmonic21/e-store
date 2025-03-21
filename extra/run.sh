cd ../simple-electronic-store
mvn clean install
cd ../extra
docker-compose build
docker-compose up
