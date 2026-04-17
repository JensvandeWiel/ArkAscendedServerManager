# Server Clustering

Provide a short outline for the tutorial.
In this tutorial, you will learn how to:
* Setup a server cluster
* View cluster configuration options

## Before you start
Make sure that:
- You have at least 2 servers set up and installed
## Part 1: Create a cluster

### Step 1: Open the clusters page:
![clusters_page_menu.png](clusters_page_menu.png)

### Step 2: Click on the "Create Cluster" button:
![clusters_page_new_cluster.png](clusters_page_new_cluster.png)

### Step 3 (optional): Change the cluster name:
> The cluster name is used to identify the cluster in the UI and nothing else, the id is used to identify the cluster for the servers.

![clusters_page_cluster_name.png](clusters_page_cluster_name.png)

## Part 2: Assign servers to the cluster
### Step 1: Open the server and go to the general tab and look for the "Cluster Configuration"
![server_general_cluster_config.png](server_general_cluster_config.png)

### Step 2: Open the cluster dropdown and select the cluster you created in the previous step
![server_general_cluster_config_dropdown.png](server_general_cluster_config_dropdown.png)

> Cluster ID and Directory Override are read only fields that are automatically set when you select a cluster.

### Step 3: Inspect cluster tribute configuration
Inspect the cluster tribute configuration and change the values if needed.
![server_general_cluster_tribute_config.png](server_general_cluster_tribute_config.png)

> The default server config does not allow any downloads or uploads, you can change these settings in the following sections:
> - `Enable Tribute Downloads`
> - `Tribute Upload Options`
> - `Cluster Tribute Options`
>
{style="warning"}

### Step 4: Save the server configuration
Save the server configuration, and you are done!


## What you've learned {id="what-learned"}
In this tutorial you have learned how to:
- Create a server cluster
- Assign servers to a cluster
- Change cluster tribute configuration options