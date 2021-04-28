# Practice assigment #2

In order to get some practice with passing secrets and config maps from k8s to Spring boot application I created simple app with several endpoints, dockerized it and upload image to Docker Hub.

### Task #1 - Secrets

Lets hit simple endpoint of checking secrets in application which is running locally:

```
GET http://localhost:8080/api/secrets

{
    "username": "default-username",
    "password": "default-password"
}
```

Now lets run this application on Docker using next docker compose config:

```
version: "3"
services:

  spring-boot-k8s-service:
    image: "rgederin/spring-boot-k8s-config:0.0.1-SNAPSHOT"
    ports:
      - 8080:8080
    environment:
      SECRETS_USERNAME: docker-compose-username
      SECRETS_PASSWORD: docker-compose-password

```

After this lets hit secrets endpoint one more time:

```
GET http://localhost:8080/api/secrets

{   
    "username": "docker-compose-username",
    "password": "docker-compose-password"
}
```

Now lets try to use k8s Secrets. Lets create needful Secret first:

```
apiVersion: v1
kind: Secret
metadata:
  name: spring-app-secret
type: Opaque
data:
  SECRETS_USERNAME: azhzLXNlY3JldC11c2VybmFtZQ==
  SECRETS_PASSWORD: azhzLXNlY3JldC1wYXNzd29yZA==
```

![secret1](https://github.com/rgederin/k8s-sandbox/blob/master/k8s-basics-lohika/img/secret1.png) 

Now lets create Deployment which will use created Secret

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-app-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      component: spring-app
  template:
    metadata:
      labels:
        component: spring-app
    spec:
      containers:
        - name: spring-app
          image: rgederin/spring-boot-k8s-config:0.0.1-SNAPSHOT
          ports:
            - containerPort: 8080
          env:
            - name: SECRETS_USERNAME
              valueFrom:
                secretKeyRef:
                  name: spring-app-secret
                  key: SECRETS_USERNAME
            - name: SECRETS_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: spring-app-secret
                  key: SECRETS_PASSWORD
```

![secret2](https://github.com/rgederin/k8s-sandbox/blob/master/k8s-basics-lohika/img/secret2.png) 

And finally lets expose pod using Service (NodePort) object:

```
apiVersion: v1
kind: Service
metadata:
  name: spring-app-service
  labels:
    component: spring-app
spec:
  type: NodePort
  ports:
    - port: 8080
      targetPort: 8080
      nodePort: 31515
  selector:
    component: spring-app
```

After this we could access our application using exposed NodePort and check that our secrets were successfully passed to our application:

![secret3](https://github.com/rgederin/k8s-sandbox/blob/master/k8s-basics-lohika/img/secret3.png) 
