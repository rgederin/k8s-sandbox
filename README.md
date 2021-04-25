# K8S Sandbox
Sandbox for playing with k8s

# Table of Content

- [Kubernetes overview](#kubernetes-overview)
    * [What is k8s](#what-is-k8s)
    * [Deployments history](#deployments-history)
    * [Why use k8s](#why-use-k8s)
    * [What k8s is not](#what-k8s-is-not)
- [Kubernetes cluster](#kubernetes-cluster)
    * [K8s master components](#k8s-master-components)
    * [K8s worker node components](#k8s-worker-node-components)
    * [Kubectl](#kubectl)
    * [Basic flow](#basic-flow)
- [Self-Hosting Kubernetes or Managed Kubernetes Services](#self-hosting-kubernetes-or-managed-kubernetes-services)
    * [Self-Hosting k8s](#self-hosting-k8s)
    * [Managed Kubernetes Services](#managed-kubernetes-services)
- [Running k8s locally](#running-k8s-locally)
- [Run first container on k8s](#run-first-container-on-k8s)
- [Kubernetes objects](#kubernetes-objects)
    * [Pods](#pods)


# Kubernetes overview

In this section described some overal overview of k8s. Answers for questions `what is k8s`, `why and for what need to use k8s` and `what k8s is not`.

## What is k8s

Kubernetes is a portable, extensible, open-source platform for managing containerized workloads and services, that facilitates both declarative configuration and automation. It has a large, rapidly growing ecosystem. Kubernetes services, support, and tools are widely available.

Google open-sourced the Kubernetes project in 2014. Kubernetes combines over 15 years of Google's experience running production workloads at scale with best-of-breed ideas and practices from the community.

## Deployments history

![history](https://github.com/rgederin/k8s-sandbox/blob/master/img/history.png)

**Traditional deployment era:** Early on, organizations ran applications on physical servers. There was no way to define resource boundaries for applications in a physical server, and this caused resource allocation issues. For example, if multiple applications run on a physical server, there can be instances where one application would take up most of the resources, and as a result, the other applications would underperform. A solution for this would be to run each application on a different physical server. But this did not scale as resources were underutilized, and it was expensive for organizations to maintain many physical servers.

**Virtualized deployment era:** As a solution, virtualization was introduced. It allows you to run multiple Virtual Machines (VMs) on a single physical server's CPU. Virtualization allows applications to be isolated between VMs and provides a level of security as the information of one application cannot be freely accessed by another application.

Virtualization allows better utilization of resources in a physical server and allows better scalability because an application can be added or updated easily, reduces hardware costs, and much more. With virtualization you can present a set of physical resources as a cluster of disposable virtual machines.

Each VM is a full machine running all the components, including its own operating system, on top of the virtualized hardware.

**Container deployment era:** Containers are similar to VMs, but they have relaxed isolation properties to share the Operating System (OS) among the applications. Therefore, containers are considered lightweight. Similar to a VM, a container has its own filesystem, share of CPU, memory, process space, and more. As they are decoupled from the underlying infrastructure, they are portable across clouds and OS distributions.

Containers have become popular because they provide extra benefits, such as:

* Agile application creation and deployment: increased ease and efficiency of container image creation compared to VM image use.
* Continuous development, integration, and deployment: provides for reliable and frequent container image build and deployment with quick and efficient rollbacks (due to image immutability).
* Dev and Ops separation of concerns: create application container images at build/release time rather than deployment time, thereby decoupling applications from infrastructure.
* Observability not only surfaces OS-level information and metrics, but also application health and other signals.
* Environmental consistency across development, testing, and production: Runs the same on a laptop as it does in the cloud.
* Cloud and OS distribution portability: Runs on Ubuntu, RHEL, CoreOS, on-premises, on major public clouds, and anywhere else.
* Application-centric management: Raises the level of abstraction from running an OS on virtual hardware to running an application on an OS using logical resources.
* Loosely coupled, distributed, elastic, liberated micro-services: applications are broken into smaller, independent pieces and can be deployed and managed dynamically – not a monolithic stack running on one big single-purpose machine.
* Resource isolation: predictable application performance.
* Resource utilization: high efficiency and density.

On a very high level of abstraction k8s architecture is described below (again this is high level abstraction):

![k8s-1](https://github.com/rgederin/k8s-sandbox/blob/master/img/k8s-1.png)

## Why use k8s

Containers are a good way to bundle and run your applications. In a production environment, you need to manage the containers that run the applications and ensure that there is no downtime. For example, if a container goes down, another container needs to start. Wouldn't it be easier if this behavior was handled by a system?

That's how Kubernetes comes to the rescue! Kubernetes provides you with a framework to run distributed systems resiliently. It takes care of scaling and failover for your application, provides deployment patterns, and more. For example, Kubernetes can easily manage a canary deployment for your system.

**Kubernetes provides you with:**

* Service discovery and load balancing Kubernetes can expose a container using the DNS name or using their own IP address. If traffic to a container is high, Kubernetes is able to load balance and distribute the network traffic so that the deployment is stable.
* Storage orchestration Kubernetes allows you to automatically mount a storage system of your choice, such as local storages, public cloud providers, and more.
* Automated rollouts and rollbacks You can describe the desired state for your deployed containers using Kubernetes, and it can change the actual state to the desired state at a controlled rate. For example, you can automate Kubernetes to create new containers for your deployment, remove existing containers and adopt all their resources to the new container.
* Automatic bin packing You provide Kubernetes with a cluster of nodes that it can use to run containerized tasks. You tell Kubernetes how much CPU and memory (RAM) each container needs. Kubernetes can fit containers onto your nodes to make the best use of your resources.
* Self-healing Kubernetes restarts containers that fail, replaces containers, kills containers that don't respond to your user-defined health check, and doesn't advertise them to clients until they are ready to serve.
* Secret and configuration management Kubernetes lets you store and manage sensitive information, such as passwords, OAuth tokens, and SSH keys. You can deploy and update secrets and application configuration without rebuilding your container images, and without exposing secrets in your stack configuration.

![k8s-2](https://github.com/rgederin/k8s-sandbox/blob/master/img/k8s-2.png)

## What k8s is not

Kubernetes is not a traditional, all-inclusive PaaS (Platform as a Service) system. Since Kubernetes operates at the container level rather than at the hardware level, it provides some generally applicable features common to PaaS offerings, such as deployment, scaling, load balancing, and lets users integrate their logging, monitoring, and alerting solutions. However, Kubernetes is not monolithic, and these default solutions are optional and pluggable. Kubernetes provides the building blocks for building developer platforms, but preserves user choice and flexibility where it is important.

What Kubernetes is not
Kubernetes is not a traditional, all-inclusive PaaS (Platform as a Service) system. Since Kubernetes operates at the container level rather than at the hardware level, it provides some generally applicable features common to PaaS offerings, such as deployment, scaling, load balancing, and lets users integrate their logging, monitoring, and alerting solutions. However, Kubernetes is not monolithic, and these default solutions are optional and pluggable. Kubernetes provides the building blocks for building developer platforms, but preserves user choice and flexibility where it is important.

**Kubernetes:**

* Does not limit the types of applications supported. Kubernetes aims to support an extremely diverse variety of workloads, including stateless, stateful, and data-processing workloads. If an application can run in a container, it should run great on Kubernetes.
* Does not deploy source code and does not build your application. Continuous Integration, Delivery, and Deployment (CI/CD) workflows are determined by organization cultures and preferences as well as technical requirements.
* Does not provide application-level services, such as middleware (for example, message buses), data-processing frameworks (for example, Spark), databases (for example, MySQL), caches, nor cluster storage systems (for example, Ceph) as built-in services. Such components can run on Kubernetes, and/or can be accessed by applications running on Kubernetes through portable mechanisms, such as the Open Service Broker.
* Does not dictate logging, monitoring, or alerting solutions. It provides some integrations as proof of concept, and mechanisms to collect and export metrics.
* Does not provide nor mandate a configuration language/system (for example, Jsonnet). It provides a declarative API that may be targeted by arbitrary forms of declarative specifications.
* Does not provide nor adopt any comprehensive machine configuration, maintenance, management, or self-healing systems.
* Additionally, Kubernetes is not a mere orchestration system. In fact, it eliminates the need for orchestration. The technical definition of orchestration is execution of a defined workflow: first do A, then B, then C. In contrast, Kubernetes comprises a set of independent, composable control processes that continuously drive the current state towards the provided desired state. It shouldn't matter how you get from A to C. Centralized control is also not required. This results in a system that is easier to use and more powerful, robust, resilient, and extensible.

# Kubernetes cluster

When you deploy Kubernetes, you get a cluster.

A Kubernetes cluster consists of a set of worker machines, called nodes, that run containerized applications and one or several master nodes which are controls k8s cluster. Every cluster has at least one worker node.

![k8s-arch](https://github.com/rgederin/k8s-sandbox/blob/master/img/k8s-arch.png)

## K8s master components

Master Node is called the **Control Plane** and it has 4 further things, namely API Server, etcd, Scheduler and Controller Manager.

### API Server

The API server is a component of the Kubernetes control plane that exposes the Kubernetes API. The API server is the front end for the Kubernetes control plane. This enables all the communication b/w API, we are going to talk to Kube API Server only. It takes the request and sends it to other services.

We can use Kubectl CLI to manage the Kubernetes Cluster. Kubectl sends the request to the API server and then API Server responds back. 

The main implementation of a Kubernetes API server is `kube-apiserver`.

![k8s-api](https://github.com/rgederin/k8s-sandbox/blob/master/img/k8s-api.png)

### etcd

Consistent and highly-available key value store used as Kubernetes' backing store for all cluster data. Kube API Server stores all the information in Etcd and other services also reads and store the information in the Etcd storage. It should be backed up regularly.

It stores the current state of everything in the cluster here at the ETCD server

![etcd](https://github.com/rgederin/k8s-sandbox/blob/master/img/etcd.png)

### Kube Scheduler

It picks up the container and puts it on the right node based on different factors. Control plane component that watches for newly created Pods with no assigned node, and selects a node for them to run on.

![scheduler](https://github.com/rgederin/k8s-sandbox/blob/master/img/scheduler.png)

### Controller Manager

Control Plane component that runs controller processes.

Logically, each controller is a separate process, but to reduce complexity, they are all compiled into a single binary and run in a single process.

![controller](https://github.com/rgederin/k8s-sandbox/blob/master/img/controller.png)


## K8s worker node components

Node components run on every node, maintaining running pods and providing the Kubernetes runtime environment.

### kubelet
An agent that runs on each node in the cluster. It makes sure that containers are running in a Pod.

The kubelet takes a set of PodSpecs that are provided through various mechanisms and ensures that the containers described in those PodSpecs are running and healthy. The kubelet doesn't manage containers which were not created by Kubernetes.

### kube-proxy

kube-proxy is a network proxy that runs on each node in your cluster, implementing part of the Kubernetes Service concept.

kube-proxy maintains network rules on nodes. These network rules allow network communication to your Pods from network sessions inside or outside of your cluster.

kube-proxy uses the operating system packet filtering layer if there is one and it's available. Otherwise, kube-proxy forwards the traffic itself.

### Container runtime

The container runtime is the software that is responsible for running containers.

Kubernetes supports several container runtimes: Docker, containerd, CRI-O, and any implementation of the Kubernetes CRI (Container Runtime Interface).

![node](https://github.com/rgederin/k8s-sandbox/blob/master/img/node.png)

## Kubectl

`kubectl` is not related to the k8s architecture. The kubectl command line tool lets you control Kubernetes clusters. For configuration, kubectl looks for a file named config in the $HOME/.kube directory. You can specify other kubeconfig files by setting the KUBECONFIG environment variable or by setting the --kubeconfig flag.

![kubectl](https://github.com/rgederin/k8s-sandbox/blob/master/img/kubectl.jpeg)

## Basic flow

Lets review pod creation flow using kubectl, this what happens:

![basic-flow](https://github.com/rgederin/k8s-sandbox/blob/master/img/basic-flow.png)

1. kubectl writes to the API Server.
2. API Server validates the request and persists it to etcd.
3. etcd notifies back the API Server.
4. API Server invokes the Scheduler.
5. Scheduler decides where to run the pod on and return that to the API Server.
6. API Server persists it to etcd.
7. etcd notifies back the API Server.
8. API Server invokes the Kubelet in the corresponding node.
9. Kubelet talks to the Docker daemon using the API over the Docker socket to create the container.
10. Kubelet updates the pod status to the API Server.
11. API Server persists the new state in etcd.

# Self-Hosting Kubernetes or Managed Kubernetes Services

The most important decision facing anyone who’s considering running production workloads in Kubernetes is buy or build? Should you run your own clusters, or pay someone else to run them? Let’s look at some of the options.

**General recommendations from most of information sources is to go with Managed K8S services. With the run less software principles in mind, it is recommend that you outsource your Kubernetes cluster operations to a managed service. Installing, configuring, maintaining, securing, upgrading, and making your Kubernetes cluster reliable is undifferentiated heavy lifting, so it makes sense for almost all businesses not to do it themselves**

## Self-Hosting k8s

The most basic choice of all is self-hosted Kubernetes. By self-hosted we mean that you, personally, or a team in your organization, install and configure Kubernetes, on machines that you own or control, just as you might do with any other software that you use, such as Redis, PostgreSQL, or Nginx.

This is the option that gives you the maximum flexibility and control. You can decide what versions of Kubernetes to run, what options and features are enabled, when and whether to upgrade clusters, and so on. But there are some significant downsides

The self-hosted option also requires the maximum resources, in terms of people, skills, engineering time, maintenance, and troubleshooting. Just setting up a working Kubernetes cluster is pretty simple, but that’s a long way from a cluster that’s ready for production. You need to consider at least the following questions:

* Is the control plane highly available? That is, if a master node goes down or becomes unresponsive, does your cluster still work? Can you still deploy or update apps? Will your running applications still be fault-tolerant without the control plane?
* Is your pool of worker nodes highly available? That is, if an outage should take down several worker nodes, or even a whole cloud availability zone, will your workloads stop running? Will your cluster keep working? Will it be able to automatically provision new nodes to heal itself, or will it require manual intervention?
* Is your cluster set up securely? Do its internal components communicate using TLS encryption and trusted certificates? Do users and applications have minimal rights and permissions for cluster operations? Are container security defaults set properly? Do nodes have unnecessary access to control plane components? Is access to the underlying etcd database properly controlled and authenticated?
* Are all services in your cluster secure? If they’re accessible from the internet, are they properly authenticated and authorized? Is access to the cluster API strictly limited?
* Is your cluster conformant? Does it meet the standards for Kubernetes clusters defined by the Cloud Native Computing Foundation?
* Are your cluster nodes fully config-managed, rather than being set up by impera‐ tive shell scripts and then left alone? The operating system and kernel on each node needs to be updated, have security patches applied, and so on.
* Is the data in your cluster properly backed up, including any persistent storage? What is your restore process? How often do you test restores?
* Once you have a working cluster, how do you maintain it over time? How do you provision new nodes? Roll out config changes to existing nodes? Roll out Kuber‐ netes updates? Scale in response to demand? Enforce policies?

Distributed systems engineer and writer Cindy Sridharan has estimated that it takes around a million dollars in engineer salary to get Kubernetes up and running in a production configuration from scratch (“And you still might not get there”). That figure should give any technical leader food for thought when considering self-hosted Kubernetes.

## Managed Kubernetes Services

Managed Kubernetes services relieve you of almost all the administration overhead of setting up and running Kubernetes clusters, particularly the control plane. Effectively, a managed service means you pay for someone else (such as Google) to run the cluster for you.

### Google Kubernetes Engine (GKE)

As you’d expect from the originators of Kubernetes, Google offers a fully managed Kubernetes service that is completely integrated with the Google Cloud Platform. Just choose the number of worker nodes, and click a button in the GCP web console to create a cluster, or use the Deployment Manager tool to provision one. Within a few minutes, your cluster will be ready to use.

Google takes care of monitoring and replacing failed nodes, auto-applying security patches, and high availability for the control plane and etcd. You can set your nodes to auto-upgrade to the latest version of Kubernetes, during a maintenance window of your choice.

### Amazon Elastic Container Service for Kubernetes (EKS)

Amazon has also been providing managed container cluster services for a long time, but until very recently the only option was Elastic Container Service (ECS), Amazon’s proprietary technology.
While perfectly usable, ECS is not as powerful or flexible as Kubernetes, and evidently even Amazon has decided that the future is Kubernetes, with the launch of Elastic Container Service for Kubernetes (EKS). (Yes, EKS ought to stand for Elastic Kubernetes Service, but it doesn’t.)

It’s not quite as seamless an experience as Google Kubernetes Engine, so be prepared to do more of the setup work yourself. Also, unlike some competitors, EKS charges you for the master nodes as well as the other cluster infrastructure. This makes it more expensive, for a given cluster size, than either Google or Microsoft’s managed Kubernetes service.

If you already have infrastructure in AWS, or run containerized workloads in the older ECS service that you want to move to Kubernetes, then EKS is a sensible choice. As the newest entry into the managed Kubernetes marketplace, though, it has some distance to go to catch up to the Google and Microsoft offerings.

### Azure Kubernetes Service (AKS)

Although Microsoft came a little later to the cloud business than Amazon or Google, they’re catching up fast. Azure Kubernetes Service (AKS) offers most of the features of its competitors, such as Google’s GKE. You can create clusters from the web interface or using the Azure az command-line tool.

As with GKE and EKS, you have no access to the master nodes, which are managed internally, and your billing is based on the number of worker nodes in your cluster.

# Running k8s locally

**Docker Desktop** includes a standalone Kubernetes server and client, as well as Docker CLI integration that runs on your machine. The Kubernetes server runs locally within your Docker instance, is not configurable, and is a single-node cluster.

The Kubernetes server runs within a Docker container on your local system, and is only for local testing. Enabling Kubernetes allows you to deploy your workloads in parallel, on Kubernetes, Swarm, and as standalone containers. Enabling or disabling the Kubernetes server does not affect your other workloads.

To enable Kubernetes support and install a standalone instance of Kubernetes running as a Docker container, go to Preferences > Kubernetes and then click Enable Kubernetes.

To set Kubernetes as the default orchestrator, select Deploy Docker Stacks to Kubernetes by default.

By default, Kubernetes containers are hidden from commands like docker service ls, because managing them manually is not supported. To see these internal containers, select Show system containers (advanced). Most users do not need this option.

Click Apply & Restart to save the settings and then click Install to confirm. This instantiates images required to run the Kubernetes server as containers, and installs the /usr/local/bin/kubectl command on your machine.

![kube-enable](https://github.com/rgederin/k8s-sandbox/blob/master/img/kube-enable.png)

When Kubernetes is enabled and running, an additional status bar item displays at the bottom right of the Docker Desktop Settings dialog.

The status of Kubernetes shows in the Docker menu and the context points to docker-desktop.

![kube-context](https://github.com/rgederin/k8s-sandbox/blob/master/img/kube-context.png)

# Run first container on k8s

Lets compare docker-compose and k8s:

![comp](https://github.com/rgederin/k8s-sandbox/blob/master/img/comp.png)

And below you could find steps which are required to run simple container on local k8s cluster:

![comp2](https://github.com/rgederin/k8s-sandbox/blob/master/img/comp2.png)

### Dockerize simple React application

In  [fib-client-k8s](https://github.com/rgederin/k8s-sandbox/blob/master/fib-client-k8s) folder you could find react application which is used for demo. Docker file for this application also including nginx for serving static content:

```
FROM node:alpine as builder
WORKDIR '/app'
COPY ./package.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx
EXPOSE 3000
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/build /usr/share/nginx/html
```

Lets build docker image for this app and push it to Docker Hub:

```
docker build -t rgederin/fib-client-standalone-k8s .
docker push rgederin/fib-client-standalone-k8s
```

After this image with this application will appear on Docker Hub

![hub](https://github.com/rgederin/k8s-sandbox/blob/master/img/hub.png)

### Config file for container creation

In [k8s](https://github.com/rgederin/k8s-sandbox/blob/master/fib-client-k8s/k8s) folder you could find `fib-client-pod.yaml` which contains needful configuration:

```
apiVersion: v1
kind: Pod
metadata:
  name: fib-client-pod
  labels:
    component: web
spec:
  containers:
    - name: fib-client
      image: rgederin/fib-client-standalone-k8s
      ports:
        - containerPort: 3000
```

In order to deploy this configuration to the k8s we need to execute next command:

```
kubectl apply -f fib-client-pod.yaml
```

### Config file for networking creation

In [k8s](https://github.com/rgederin/k8s-sandbox/blob/master/fib-client-k8s/k8s) folder you could find `fib-client-node-port.yaml` which contains needful configuration:

```
apiVersion: v1
kind: Service
metadata:
  name: fib-client-node-port
  labels:
    component: web
spec:
  type: NodePort
  ports:
    - port: 3050
      targetPort: 3000
      nodePort: 31515
  selector:
    component: web
```

In order to deploy this configuration to the k8s we need to execute next command:

```
kubectl apply -f fib-client-node-port.yaml
```

After this you could check `http://localhost:31515/` and should see:

![ui](https://github.com/rgederin/k8s-sandbox/blob/master/img/ui.png)


# Kubernetes objects

Kubernetes objects are persistent entities in the Kubernetes system. Kubernetes uses these entities to represent the state of your cluster. Specifically, they can describe:

* What containerized applications are running (and on which nodes)
* The resources available to those applications
* The policies around how those applications behave, such as restart policies, upgrades, and fault-tolerance

A Kubernetes object is a "record of intent"--once you create the object, the Kubernetes system will constantly work to ensure that object exists. By creating an object, you're effectively telling the Kubernetes system what you want your cluster's workload to look like; this is your cluster's `desired state`.

To work with Kubernetes objects--whether to create, modify, or delete them--you'll need to use the Kubernetes API. When you use the kubectl command-line interface, for example, the CLI makes the necessary Kubernetes API calls for you. You can also use the Kubernetes API directly in your own programs using one of the Client Libraries.

![objects](https://github.com/rgederin/k8s-sandbox/blob/master/img/objects.png)


### Object Spec and Status 

Almost every Kubernetes object includes two nested object fields that govern the object's configuration: the object `spec` and the object `status`. For objects that have a `spec`, you have to set this when you create the object, providing a description of the characteristics you want the resource to have: its `desired state`.

The `status` describes the `current state` of the object, supplied and updated by the Kubernetes system and its components. **The Kubernetes control plane continually and actively manages every object's actual state to match the desired state you supplied.**

![loop](https://github.com/rgederin/k8s-sandbox/blob/master/img/loop.png)

For example: in Kubernetes, a Deployment is an object that can represent an application running on your cluster. When you create the Deployment, you might set the Deployment spec to specify that you want three replicas of the application to be running. The Kubernetes system reads the Deployment spec and starts three instances of your desired application--updating the status to match your spec. If any of those instances should fail (a status change), the Kubernetes system responds to the difference between spec and status by making a correction--in this case, starting a replacement instance.

### Describing a Kubernetes object

When you create an object in Kubernetes, you must provide the object spec that describes its desired state, as well as some basic information about the object (such as a name). When you use the Kubernetes API to create the object (either directly or via `kubectl`), that API request must include that information as JSON in the request body. **Most often, you provide the information to kubectl in a .yaml file.** `kubectl` converts the information to JSON when making the API request.

![obj](https://github.com/rgederin/k8s-sandbox/blob/master/img/obj.png)

Here's an example .yaml file that shows the required fields and object spec for a Kubernetes Deployment:

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2 # tells deployment to run 2 pods matching the template
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
```

One way to create a Deployment using a .yaml file like the one above is to use the kubectl apply command in the kubectl command-line interface, passing the .yaml file as an argument. Here's an example:

```
kubectl apply -f https://k8s.io/examples/application/deployment.yaml --record
```

### Required Fields

In the .yaml file for the Kubernetes object you want to create, you'll need to set values for the following fields:

* apiVersion - Which version of the Kubernetes API you're using to create this object
* kind - What kind of object you want to create
* metadata - Data that helps uniquely identify the object, including a name string, UID, and optional namespace
* spec - What state you desire for the object

![api](https://github.com/rgederin/k8s-sandbox/blob/master/img/api.png)

The precise format of the object spec is different for every Kubernetes object, and contains nested fields specific to that object. The Kubernetes API Reference can help you find the spec format for all of the objects you can create using Kubernetes. For example, the spec format for a Pod can be found in PodSpec v1 core, and the spec format for a Deployment can be found in DeploymentSpec v1 apps.

## Pods

Pods are the smallest deployable units of computing that you can create and manage in Kubernetes.

A Pod (as in a pod of whales or pea pod) is a group of one or more containers, with shared storage and network resources, and a specification for how to run the containers. A Pod's contents are always co-located and co-scheduled, and run in a shared context. A Pod models an application-specific "logical host": it contains one or more application containers which are relatively tightly coupled. In non-cloud contexts, applications executed on the same physical or virtual machine are analogous to cloud applications executed on the same logical host.

The shared context of a Pod is a set of Linux namespaces, cgroups, and potentially other facets of isolation - the same things that isolate a Docker container. Within a Pod's context, the individual applications may have further sub-isolations applied.

In terms of Docker concepts, a Pod is similar to a group of Docker containers with shared namespaces and shared filesystem volumes.

![pod1](https://github.com/rgederin/k8s-sandbox/blob/master/img/pod1.jpeg)

### Using Pods

Usually you don't need to create Pods directly, even singleton Pods. Instead, create them using workload resources such as `Deployment` or `Job`. If your Pods need to track state, consider the `StatefulSet` resource.

Pods in a Kubernetes cluster are used in two main ways:

* **Pods that run a single container.** The "one-container-per-Pod" model is the most common Kubernetes use case; in this case, you can think of a Pod as a wrapper around a single container; Kubernetes manages Pods rather than managing the containers directly.

* **Pods that run multiple containers that need to work together.** A Pod can encapsulate an application composed of multiple co-located containers that are tightly coupled and need to share resources. These co-located containers form a single cohesive unit of service—for example, one container serving data stored in a shared volume to the public, while a separate sidecar container refreshes or updates those files. The Pod wraps these containers, storage resources, and an ephemeral network identity together as a single unit.

**Note:** Grouping multiple co-located and co-managed containers in a single Pod is a relatively advanced use case. You should use this pattern only in specific instances in which your containers are tightly coupled.

Each Pod is meant to run a single instance of a given application. If you want to scale your application horizontally (to provide more overall resources by running more instances), you should use multiple Pods, one for each instance. In Kubernetes, this is typically referred to as replication. Replicated Pods are usually created and managed as a group by a workload resource and its controller.

### How Pods manage multiple containers 

Pods are designed to support multiple cooperating processes (as containers) that form a cohesive unit of service. The containers in a Pod are automatically co-located and co-scheduled on the same physical or virtual machine in the cluster. The containers can share resources and dependencies, communicate with one another, and coordinate when and how they are terminated.

For example, you might have a container that acts as a web server for files in a shared volume, and a separate "sidecar" container that updates those files from a remote source, as in the following diagram:

![pod2](https://github.com/rgederin/k8s-sandbox/blob/master/img/pod2.png)


Some Pods have init containers as well as app containers. Init containers run and complete before the app containers are started.

Pods natively provide two kinds of shared resources for their constituent containers: networking and storage.

