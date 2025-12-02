```
mkdir -p /root/mysql/data
mkdir -p /root/mysql/conf
mkdir -p /root/mysql/init

docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_ROOT_PASSWORD=123456 \
  --network dev-net \
  -v /root/mysql/data:/var/lib/mysql \
  -v /root/mysql/conf:/etc/mysql/conf.d \
  -v /root/mysql/init:/docker-entrypoint-initdb.d \
  mysql:8.4.7
  
  
  docker exec -it mysql mysql -uroot -p
  
```



```
#!/bin/bash

# 创建目录
mkdir -p /root/nginx/html
mkdir -p /root/nginx/conf

# 创建默认网页
echo "Hello Nginx" > /root/nginx/html/index.html

# 创建 Nginx 配置文件
cat > /root/nginx/conf/nginx.conf <<EOF
events {}

http {
    server {
        listen 80;
        root /usr/share/nginx/html;
        index index.html;
    }
}
EOF

# 启动 Nginx 容器（若已存在则先删除）
docker stop nginx 2>/dev/null
docker rm nginx 2>/dev/null

docker run -d \
  --name nginx \
  -p 80:80 \
  -v /root/nginx/html:/usr/share/nginx/html \
  -v /root/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
  nginx:1.13.8

echo "Nginx 部署完成！访问服务器 IP 即可看到页面。"

docker exec -it nginx /bin/bash
nginx -v

```



- 其他环境见文件夹

