### uat 环境
network
docker network create net

mysql 8
docker run --rm -it --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql/mysql-server
pgsql
docker run --rm -it --name pgsql -p 5432:5432 --network net -v /Users/chengweiou/Desktop/docker/pgsql/data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=123456 -d postgres

redis
docker run --rm --name redis -p 6379:6379 --network net -d redis

#### 上传
```
./uat.sh
```
first time:
```
change active profile to uat
mkdir -pv ~/Desktop/docker/universe/config
cp src/main/resources/application.yml ~/Desktop/docker/universe/config/
chmod +x uat.sh
./uat.sh
```
