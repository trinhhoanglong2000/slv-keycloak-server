# 🔐 SLV Keycloak Server

A customized Keycloak server with extended functionality, including custom REST APIs, UI enhancements, and core SPI extensions.

---

## 📦 Submodules

This project is organized into four submodules:

| Submodule            | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| `slv-keycloak-core`  | Core SPI extensions for Keycloak, including validation, authentication, etc. |
| `slv-keycloak-rest`  | Custom REST APIs to extend Keycloak functionality.                          |
| `slv-keycloak-ui`    | Custom themes, tabs, and pages for the Keycloak Admin UI.                   |

---

## 🚀 Getting Started
>This project is built on and compatible with Keycloak _**26.3.2**_.  
Refer to [Keycloak documentation](https://www.keycloak.org/docs/latest/) for version-specific features and setup.
### ⚙️ System Requirements

- **Java Development Kit (JDK) 21**  
  Ensure JDK 21 is installed and configured in your environment. You can verify with:
  ```bash
  java -version
  ```

- **Docker**  
  Required for building and running the Keycloak server container. Recommended version: Docker Engine 20.10+


### 1️⃣ Build the Docker Image

Before running the project, build the Docker image once:

```bash
docker build -t slv-keycloak-server .
```
Or
```bash
docker compose build
```
> ⚠️ ***__This step is only required once for image creation.__***  
> ***__Skip it for subsequent runs unless changes are made to the Dockerfile.__***

### 2️⃣ Package the Application

Build the JAR files using Maven:

```bash
mvn clean package
```
**Manually copy the JAR files to the Keycloak provider folder located in:**
> **/keycloak-providers/**

**Or run the following script to copy JAR files**

* __Windows__ : Run the .bat script:
```bash
copyJar.bat
```
* __macOS/iOS__: Run the .sh script:
```bash
copyJar.sh
```

## 3️⃣ Start the Server
Use Docker Compose to start the Keycloak server:
```bash
docker-compose up
```

## 🐞 Debugging

- ## **IntelliJ**
  
    To enable remote debugging:

    Open Run > Edit Configurations in IntelliJ.
  Add a new configuration:
    - Type: Remote JVM Debug
    - Port: 5005
  
    Start the server with debugging enabled (ensure port 5005 is exposed in your Docker setup).
