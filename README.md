# Platform Provisioning API Documentation

## Overview

The Platform Provisioning API is a RESTful service that provisions infrastructure resources in Google Cloud Platform (GCP). It creates a Google Kubernetes Engine (GKE) cluster and a Cloud SQL instance based on the provided configuration parameters.

**Base URL:** `http://localhost:8080` (development)

**API Version:** `v1`

---

## Endpoints

### Provision Infrastructure

Creates a new GKE cluster and Cloud SQL instance in GCP.

**Endpoint:** `POST /v1/provision/new`

#### Request

**Headers:**
```
Content-Type: application/json
```

**Body Schema:**
```json
{
  "size": "string",
  "cluster_name": "string",
  "node_count": "integer",
  "db_engine": "string",
  "created_by": "string"
}
```

**Request Parameters:**

| Parameter | Type | Required | Description | Valid Values |
|-----------|------|----------|-------------|--------------|
| `size` | string | Yes | Environment size tier | `small`, `medium`, `large` |
| `cluster_name` | string | Yes | Name for the GKE cluster | Alphanumeric, hyphens, underscores (GCP naming rules) |
| `node_count` | integer | Yes | Number of nodes in the GKE cluster | Positive integer |
| `db_engine` | string | Yes | Database engine for Cloud SQL | `postgres`, `mysql`, `sqlserver` |
| `created_by` | string | Yes | User identifier who created the provisioning request | Alphanumeric string |

**Example Request:**
```json
{
  "size": "medium",
  "cluster_name": "production-cluster-001",
  "node_count": 3,
  "db_engine": "postgres",
  "created_by": "john.doe"
}
```

#### Response

**Success Response**

**Status Code:** `202 Accepted`

**Response Schema:**
```json
{
  "id": "uuid",
  "status": "string",
  "timestamp": "string"
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier for the provisioning request |
| `status` | string | Current status of the provisioning operation |
| `timestamp` | string | ISO 8601 timestamp of when the request was received |

**Example Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "provisioning",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Error Response**

**Status Code:** `400 Bad Request`

Returned when request validation fails (e.g., invalid parameter values, missing required fields, or malformed request body).

**Response Schema:**
```json
{
  "message": "string"
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `message` | string | Error message describing the validation failure |

**Example Response:**
```json
{
  "message": "invalid request parameters"
}
```

---

### Get Provision Status

Retrieves the current status and details of a provisioning request.

**Endpoint:** `GET /v1/provision/status/{id}`

#### Request

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | UUID | Yes | Unique identifier of the provisioning request |

**Example Request:**
```
GET /v1/provision/status/550e8400-e29b-41d4-a716-446655440000
```

#### Response

**Success Response**

**Status Code:** `200 OK`

**Response Schema:**
```json
{
  "status": "string",
  "completed_at": "string",
  "created_by": "string",
  "details": {
    "gke_cluster_id": "string",
    "cloud_sql_connection": "string",
    "vpc_name": "string",
    "vpc_subnet": "string"
  }
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `status` | string | Current status of the provisioning operation | `in progress`, `ready`, `failed` |
| `completed_at` | string | ISO 8601 timestamp when provisioning completed (null if still in progress) |
| `created_by` | string | User identifier who created the provisioning request |
| `details` | object | Provisioned infrastructure details (null if status is `in progress` or `failed`) |
| `details.gke_cluster_id` | string | GKE cluster identifier |
| `details.cloud_sql_connection` | string | Cloud SQL connection string |
| `details.vpc_name` | string | VPC network name |
| `details.vpc_subnet` | string | VPC subnet name |

**Example Response (In Progress):**
```json
{
  "status": "in progress",
  "completed_at": null,
  "created_by": "john.doe",
  "details": null
}
```

**Example Response (Ready):**
```json
{
  "status": "ready",
  "completed_at": "2024-01-15T10:45:00Z",
  "created_by": "john.doe",
  "details": {
    "gke_cluster_id": "projects/my-project/locations/us-central1/clusters/my-cluster",
    "cloud_sql_connection": "my-project:us-central1:my-db-instance",
    "vpc_name": "my-vpc-network",
    "vpc_subnet": "my-vpc-subnet"
  }
}
```

**Example Response (Failed):**
```json
{
  "status": "failed",
  "completed_at": "2024-01-15T10:40:00Z",
  "created_by": "john.doe",
  "details": null
}
```

**Error Response**

**Status Code:** `404 Not Found`

Returned when the provisioning request with the given ID is not found.

**Response Schema:**
```json
{
  "message": "string"
}
```

**Example Response:**
```json
{
  "message": "provisioning request not found"
}
```

---

## Example Usage

### Provision Infrastructure

```bash
curl -X POST http://localhost:8080/v1/provision/new \
  -H "Content-Type: application/json" \
  -d '{
    "size": "medium",
    "cluster_name": "my-cluster",
    "node_count": 3,
    "db_engine": "postgres",
    "created_by": "john.doe"
  }'
```

### Get Provision Status

```bash
curl -X GET http://localhost:8080/v1/provision/status/550e8400-e29b-41d4-a716-446655440000
```
