# Building a Spring Boot container image with Cloud-Native Buildpacks

This project shows how to build a Spring Boot container image with
[Cloud-Native Buildpacks](https://buildpacks.io) (CNB) and
[Paketo Buildpacks](https://paketo.io).

You don't need to write a `Dockerfile` anymore: using CNB you get
secured up-to-date container images out of your source code.

You don't need to care about the runtime environment (JVM)
or optimizing the container (such as Java memory settings):
CNB will automatically provision dependencies and configure your container.

## How to use it?

You need to run a Docker daemon on your workstation.

Cloud-Native Buildpacks are supported out-of-the-box since
Spring Boot 2.3. All you need to do is to run the `spring-boot-maven-plugin`
with the target `build-image`:
```bash
$ ./mvnw spring-boot:build-image
```

Your container is built:
```bash
$ docker image ls
REPOSITORY                             TAG                  IMAGE ID    
myorg/cnb-springboot                   latest               b1a0d242e5ec
```

You can run this container right away:
```bash
$ docker run --rm -p 8080:8080/tcp myorg/cnb-springboot
Setting Active Processor Count to 2
Calculating JVM memory based on 4985508K available memory
Calculated JVM Memory Configuration: -XX:MaxDirectMemorySize=10M -Xmx4386956K -XX:MaxMetaspaceSize=86551K -XX:ReservedCodeCacheSize=240M -Xss1M (Total Memory: 4985508K, Thread Count: 250, Loaded Class Count: 12867, Headroom: 0%)
Adding 129 container CA certificates to JVM truststore
Spring Cloud Bindings Enabled
Picked up JAVA_TOOL_OPTIONS: -Djava.security.properties=/layers/paketo-buildpacks_bellsoft-liberica/java-security-properties/java-security.properties -agentpath:/layers/paketo-buildpacks_bellsoft-liberica/jvmkill/jvmkill-1.16.0-RELEASE.so=printHeapHistogram=1 -XX:ActiveProcessorCount=2 -XX:MaxDirectMemorySize=10M -Xmx4386956K -XX:MaxMetaspaceSize=86551K -XX:ReservedCodeCacheSize=240M -Xss1M -Dorg.springframework.cloud.bindings.boot.enable=true

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.3)

2021-02-23 11:12:44.585  INFO 1 --- [           main] f.a.demos.cnb.springboot.Application     : Starting Application v1.0.0-SNAPSHOT using Java 11.0.10 on 0717e733daf3 with PID 1 (/workspace/BOOT-INF/classes started by cnb in /workspace)
2021-02-23 11:12:44.597  INFO 1 --- [           main] f.a.demos.cnb.springboot.Application     : No active profile set, falling back to default profiles: default
2021-02-23 11:12:46.190  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-02-23 11:12:46.207  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-02-23 11:12:46.207  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.43]
2021-02-23 11:12:46.296  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-02-23 11:12:46.297  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1563 ms
2021-02-23 11:12:46.874  INFO 1 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-02-23 11:12:46.978  INFO 1 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page template: index
2021-02-23 11:12:47.229  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 4 endpoint(s) beneath base path '/actuator'
2021-02-23 11:12:47.306  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-02-23 11:12:47.352  INFO 1 --- [           main] f.a.demos.cnb.springboot.Application     : Started Application in 3.426 seconds (JVM running for 3.862)
```

## Deploying to Kubernetes

Use these Kubernetes descriptors to deploy this app to your cluster:
```bash
$ kubectl apply -f k8s
```

This app will be deployed to namespace `cnb-springboot`:
```bash
kubectl -n cnb-springboot get pod,deployment,svc
NAME                       READY   STATUS    RESTARTS   AGE
pod/app-7c7957cb94-lpmk8   1/1     Running   0          35m

NAME                        READY   UP-TO-DATE   AVAILABLE   AGE
deployment.extensions/app   1/1     1            1           35m

NAME          TYPE           CLUSTER-IP      EXTERNAL-IP      PORT(S)        AGE
service/app   LoadBalancer   10.100.200.35   35.187.115.254   80:30355/TCP   35m
```

## Contribute

Contributions are always welcome!

Feel free to open issues & send PR.

## License

Copyright &copy; 2021 [VMware, Inc. or its affiliates](https://vmware.com).

This project is licensed under the [Apache Software License version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
