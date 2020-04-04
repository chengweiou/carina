./gradlew bootJar
cp build/libs/carina-0.0.1-SNAPSHOT.jar ~/Desktop/docker/universe/carina/ser.jar
cp src/main/resources/application-uat.yml ~/Desktop/docker/universe/carina/config/
cp src/main/resources/log4j2.xml ~/Desktop/docker/universe/carina/config/
cd ~/Desktop/docker/universe/carina
docker stop carina
docker run --rm -it -d --name carina -p 60006:8906 --network net -v /Users/chengweiou/Desktop/docker/universe/carina:/proj/ -w /proj/ openjdk java -jar ser.jar