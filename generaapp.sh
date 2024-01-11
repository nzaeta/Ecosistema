rm -rf ./war/ROOT
rm -rf ./target
#mvn clean package -DskipTests
#mvn spring-boot:run -Dspring.profiles.active=production
mvn spring-boot:run clean package -Dspring.profiles.active=production
#mvn clean package -Dspring.profiles.active=production
unzip ./target/*.war -d ./war/ROOT
