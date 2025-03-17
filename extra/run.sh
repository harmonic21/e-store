cd ../simple-electronic-store
mvn clean install -Dmaven.test.skip
cd ../extra
docker-compose build
docker-compose up
