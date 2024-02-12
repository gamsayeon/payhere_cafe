# Dockerfile-mysql

FROM mysql:5.7

ENV MYSQL_ROOT_PASSWORD=0000
ENV MYSQL_DATABASE=cafe_server

# 초기화 스크립트를 복사하여 컨테이너가 시작될 때 실행되도록 설정
COPY ./init.sql /docker-entrypoint-initdb.d/