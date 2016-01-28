upstream ${userName}-${confName}{
    ip_hash;
    server ${serverIP1}:${port};
    server ${serverIP2}:${port};
}

server {
  	listen        ${port};
  	server_name   localhost;

  	location / {
  		proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Server $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://${confName}/;
  	}
}