# AI Code Impact Analysis Platform

## Overview

AI Code Impact Analysis Platform is a Java Spring Boot application that performs multi-repository impact analysis using static code analysis, dependency graph generation, and AI-assisted change planning.

The platform helps engineering teams understand the impact of:

* Field renames
* Method renames
* API endpoint changes
* Entity changes
* Service-to-service contract changes

across multiple repositories before making code changes.

The platform can:

* Index Java repositories
* Build dependency graphs
* Discover service dependencies
* Track method calls
* Track field references
* Detect affected repositories
* Detect affected files
* Generate migration plans
* Generate deployment order recommendations
* Generate code patches
* Generate full updated Java files using Claude AI

---

# Architecture

```text
Git Repositories
       |
       v
Repository Indexer
       |
       v
Java Parser (JavaParser)
       |
       v
Metadata Database

java_classes
method_calls
field_references
service_dependencies
variable_types

       |
       v
Impact Analysis Engine
       |
       v
Claude AI
       |
       v
Migration Plan
Deployment Plan
Code Changes
Updated Files
```

---

# Prerequisites

* Java 21+
* Maven 3.9+
* PostgreSQL
* Claude API Key
* Git

---

# Database Setup

Create PostgreSQL database:

```sql
create database codeagent;
```

Configure:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/codeagent
spring.datasource.username=postgres
spring.datasource.password=password
```

Run application:

```bash
mvn clean install
mvn spring-boot:run
```

---

# Step 1 - Index Repositories

Every repository must be indexed before impact analysis can be performed.

Example:

```bash
curl -X POST "http://localhost:8080/repositories/index" \
-H "Content-Type: application/json" \
-d '{
  "repoUrl":"https://github.com/company/customer-service.git"
}'
```

Index all repositories:

```bash
customer-service
billing-service
order-service
notification-service
customer-client
```

Example:

```bash
curl -X POST http://localhost:8080/repositories/index \
-d '{"repoUrl":"https://github.com/company/customer-service.git"}'

curl -X POST http://localhost:8080/repositories/index \
-d '{"repoUrl":"https://github.com/company/billing-service.git"}'

curl -X POST http://localhost:8080/repositories/index \
-d '{"repoUrl":"https://github.com/company/customer-client.git"}'
```

The platform will:

* Clone repository
* Parse Java files
* Extract classes
* Extract fields
* Extract methods
* Extract method calls
* Extract service dependencies
* Store metadata in PostgreSQL

---

# Step 2 - Verify Repository Discovery

Check indexed classes:

```sql
select *
from java_classes;
```

Check method calls:

```sql
select *
from method_calls;
```

Check service dependencies:

```sql
select *
from service_dependencies;
```

---

# Step 3 - Field Rename Impact Analysis

Example scenario:

Customer service changes:

```java
private String lastName;
```

to

```java
private String surname;
```

Request:

```bash
curl -X POST \
"http://localhost:8080/impact/service-field-change" \
-H "Content-Type: application/json" \
-d '{
  "service":"customer-service",
  "entity":"Customer",
  "oldField":"lastName",
  "newField":"surname"
}'
```

Response:

```json
[
  {
    "repo":"CustomerClient",
    "file":"CustomerMapper.java",
    "oldCode":"getLastName(",
    "newCode":"getSurname("
  },
  {
    "repo":"BillingService",
    "file":"BillingMapper.java",
    "oldCode":"getLastName(",
    "newCode":"getSurname("
  }
]
```

---

# Step 4 - Method Rename Impact Analysis

Example:

```java
getCustomer()
```

becomes:

```java
fetchCustomer()
```

Request:

```bash
curl -X POST \
"http://localhost:8080/impact/service-method-change" \
-H "Content-Type: application/json" \
-d '{
  "className":"CustomerClient",
  "oldMethod":"getCustomer",
  "newMethod":"fetchCustomer"
}'
```

Response:

```json
[
  {
    "repo":"CustomerClient",
    "file":"CustomerGateway.java",
    "oldCode":"getCustomer(",
    "newCode":"fetchCustomer("
  }
]
```

---

# Step 5 - API Endpoint Impact Analysis

Example:

```text
/customer
```

becomes:

```text
/customer/v2
```

Request:

```bash
curl -X POST \
"http://localhost:8080/impact/service-api-change" \
-H "Content-Type: application/json" \
-d '{
  "service":"customer-service",
  "oldEndpoint":"/customer",
  "newEndpoint":"/customer/v2"
}'
```

Response:

```json
[
  {
    "repo":"CustomerClient",
    "file":"CustomerGateway.java"
  }
]
```

---

# Step 6 - AI Change Analysis

Natural language request:

```bash
curl -X POST \
"http://localhost:8080/analysis/ai/chat" \
-H "Content-Type: application/json" \
-d '{
  "prompt":"Rename Customer.lastName to surname in customer-service"
}'
```

Response:

```json
{
  "summary":"...",
  "risk":"...",
  "migrationPlan":"...",
  "deploymentOrder":"...",
  "impacts":[]
}
```

The platform uses Claude to:

* Understand change request
* Determine change type
* Generate migration plan
* Generate deployment strategy
* Assess risk

---

# Step 7 - AI Patch Generation

Generate exact code modifications.

Request:

```bash
curl -X POST \
"http://localhost:8080/analysis/ai/generate-patch" \
-H "Content-Type: application/json" \
-d '{
  "prompt":"Rename Customer.lastName to surname in customer-service"
}'
```

Response:

```json
[
  {
    "repo":"BillingService",
    "file":"BillingMapper.java",
    "originalCode":"getLastName(",
    "updatedCode":"getSurname("
  }
]
```

---

# Step 8 - Full File Generation

Generate complete updated source files.

Request:

```bash
curl -X POST \
"http://localhost:8080/analysis/ai/generate-full-patch" \
-H "Content-Type: application/json" \
-d '{
  "prompt":"Rename CustomerClient.getCustomer to fetchCustomer"
}'
```

Response:

```json
[
  {
    "repo":"CustomerClient",
    "file":"CustomerGateway.java",
    "originalFile":"...",
    "updatedFile":"..."
  }
]
```

---

# Example Enterprise Workflow

Repositories:

```text
customer-service
billing-service
order-service
notification-service
customer-client
```

Developer request:

```text
Rename Customer.lastName to surname
```

Platform automatically:

1. Finds impacted repositories
2. Finds impacted files
3. Finds impacted methods
4. Generates migration plan
5. Generates deployment order
6. Generates code changes
7. Generates updated Java files

Output:

```text
Impacted Repositories

- CustomerClient
- BillingService

Files

- CustomerMapper.java
- BillingMapper.java

Changes

getLastName()
 ->
getSurname()
```

---

# Current Features

✅ Multi-repository indexing

✅ Java dependency graph generation

✅ Service dependency analysis

✅ Field rename impact analysis

✅ Method rename impact analysis

✅ API endpoint impact analysis

✅ AI-assisted migration planning

✅ AI-assisted deployment planning

✅ AI-generated code changes

✅ AI-generated full source files

---

# Future Roadmap

* Git Diff Generation
* Automatic Patch Application
* Git Commit Automation
* GitHub Pull Request Creation
* CI/CD Integration
* Kubernetes Deployment Impact Analysis
* OpenAPI Contract Impact Analysis
* Event-Driven Architecture Impact Analysis



Demo 1 — Field Rename (Strongest Current Demo)
Scenario

In customer-service:

private String lastName;

becomes:

private String surname;
Ask AI
curl -X POST "http://localhost:8080/analysis/ai/chat" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename Customer.lastName to surname in customer-service\"}"
Expected
CustomerClient
CustomerMapper.java

BillingService
BillingMapper.java

getLastName()
→
getSurname()
Generate patch
curl -X POST "http://localhost:8080/analysis/ai/generate-full-patch" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename Customer.lastName to surname in customer-service\"}"

This is your best current multi-repo demo.

Demo 2 — Method Rename
Scenario

In CustomerClient:

getCustomer()

becomes:

fetchCustomer()
Ask
curl -X POST "http://localhost:8080/analysis/ai/chat" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename CustomerClient.getCustomer to fetchCustomer\"}"
Expected
CustomerGateway.java

getCustomer()
→
fetchCustomer()
Generate full patch
curl -X POST "http://localhost:8080/analysis/ai/generate-full-patch" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename CustomerClient.getCustomer to fetchCustomer\"}"

Expected:

return customerClient
.fetchCustomer();
Demo 3 — API Contract Change
Scenario

customer-service changes:

/customer

to

/customer/v2
Ask
curl -X POST "http://localhost:8080/impact/service-api-change" ^
-H "Content-Type: application/json" ^
-d "{\"service\":\"customer-service\",\"oldEndpoint\":\"/customer\",\"newEndpoint\":\"/customer/v2\"}"

Expected:

CustomerGateway.java
BillingService
CustomerClient

affected.

Demo 4 — Add a New Consumer Repository

This is the best way to show multi-repo power.

Create a new repo:

OrderService

Add:

@FeignClient("customer-service")
public interface CustomerClient {
}

and:

customer.getLastName();

Index it:

curl -X POST "http://localhost:8080/repositories/index" ^
-H "Content-Type: application/json" ^
-d "{\"repoUrl\":\"https://github.com/you/OrderService.git\"}"

Now rerun:

curl -X POST "http://localhost:8080/analysis/ai/chat" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename Customer.lastName to surname in customer-service\"}"

Expected:

BillingService
CustomerClient
OrderService

all impacted.

This is a true multi-repo demonstration.

Demo 5 — Breaking Change Analysis

Ask:

curl -X POST "http://localhost:8080/analysis/ai/chat" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename Customer.lastName to surname in customer-service\"}"

Claude should return:

Risk: Medium

Migration Plan:
1. Update customer-service
2. Update CustomerClient
3. Update BillingService

Deployment Order:
customer-service
CustomerClient
BillingService

This demonstrates AI planning, not just code search.

Demo 6 — Enterprise Scenario

Imagine 10 repositories:

customer-service
billing-service
order-service
payment-service
notification-service
reporting-service
analytics-service
customer-client
mobile-api
web-api

All consume:

Customer.lastName

Ask:

Rename Customer.lastName to surname

Expected output:

10 repositories impacted
23 files impacted
17 methods impacted
Deployment order generated
Migration plan generated
Patches generated

This is the vision your platform is heading toward.

The Most Impressive Demo Today

Run:

curl -X POST "http://localhost:8080/analysis/ai/generate-full-patch" ^
-H "Content-Type: application/json" ^
-d "{\"prompt\":\"Rename Customer.lastName to surname in customer-service\"}"

and show:

Natural language request
Multi-repo impact detection
Exact affected files
Generated code changes
Full updated Java files

That demonstrates the entire value of the platform end-to-end.