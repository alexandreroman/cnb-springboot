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

[Download and install the `pack` CLI](https://github.com/buildpacks/pack/releases).
You'll need a Docker daemon running to build container images.

Use the Paketo builder:
```bash
$ pack set-default-builder gcr.io/paketo-buildpacks/builder:base
```

You're now ready to use CNB with Paketo.

Run this command to build a container image:
```bash
$ pack build myorg/cnb-springboot
...
Successfully built image myorg/cnb-springboot
```

A lot of dependencies are downloaded in the first run.
These dependencies will be cached so that next runs are faster.

After a couple of minutes, your container image will be published 
to your local Docker daemon:
```bash
$ docker image ls
REPOSITORY                             TAG                  IMAGE ID    
myorg/cnb-springboot                   latest               b1a0d242e5ec
```

You can run this container right away:
```bash
$ docker run --rm -p 8080:8080/tcp myorg/cnb-springboot
Calculated JVM Memory Configuration: -XX:MaxDirectMemorySize=10M -XX:MaxMetaspaceSize=87457K -XX:ReservedCodeCacheSize=240M -Xss1M -Xmx68718877278K (Head Room: 0%, Loaded C
lass Count: 13027, Thread Count: 250, Total Memory: 70368744177664)

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.4.RELEASE)

2020-01-28 15:03:12.372  WARN 1 --- [           main] pertySourceApplicationContextInitializer : Skipping 'cloud' property source addition because not in a cloud
2020-01-28 15:03:12.391  WARN 1 --- [           main] nfigurationApplicationContextInitializer : Skipping reconfiguration because not in a cloud
2020-01-28 15:03:12.424  INFO 1 --- [           main] f.a.demos.cnb.springboot.Application     : Starting Application on b087eee7b53a with PID 1 (/workspace/BOOT-INF/classes started by cnb in /workspace)
2020-01-28 15:03:12.432  INFO 1 --- [           main] f.a.demos.cnb.springboot.Application     : No active profile set, falling back to default profiles: default
2020-01-28 15:03:14.878  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2020-01-28 15:03:14.907  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-01-28 15:03:14.909  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.30]
2020-01-28 15:03:15.058  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-01-28 15:03:15.058  INFO 1 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2506 ms
2020-01-28 15:03:15.972  INFO 1 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-01-28 15:03:16.313  INFO 1 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page template: index
2020-01-28 15:03:16.597  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoint(s) beneath base path '/actuator'
2020-01-28 15:03:16.800  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2020-01-28 15:03:16.810  INFO 1 --- [           main] f.a.demos.cnb.springboot.Application     : Started Application in 5.746 seconds (JVM running for 10.604)
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

Copyright &copy; 2020 [VMware, Inc. or its affiliates](https://vmware.com).

This project is licensed under the [Apache Software License version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
