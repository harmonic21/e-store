cd ../
docker build -t e-store .
docker run -detach -p 8080:8080 e-store
