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
  "db_engine": "string"
}
```

**Request Parameters:**

| Parameter | Type | Required | Description | Valid Values |
|-----------|------|----------|-------------|--------------|
| `size` | string | Yes | Environment size tier | `small`, `medium`, `large` |
| `cluster_name` | string | Yes | Name for the GKE cluster | Alphanumeric, hyphens, underscores (GCP naming rules) |
| `node_count` | integer | Yes | Number of nodes in the GKE cluster | Positive integer |
| `db_engine` | string | Yes | Database engine for Cloud SQL | `postgres`, `mysql`, `sqlserver` |

**Example Request:**
```json
{
  "size": "medium",
  "cluster_name": "production-cluster-001",
  "node_count": 3,
  "db_engine": "postgres"
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

## Example Usage

### cURL
```bash
curl -X POST http://localhost:8080/v1/provision/new \
  -H "Content-Type: application/json" \
  -d '{
    "size": "medium",
    "cluster_name": "my-cluster",
    "node_count": 3,
    "db_engine": "postgres"
  }'
```
