# IdentityHub — Webapp

Spring Boot REST API for user management with email verification, AI-powered risk scoring, and zero-downtime deployments on GCP.

## What It Does

- User registration with input validation and BCrypt password hashing
- Profile retrieval and update with Basic Auth
- Email verification with token-based flow (2-minute expiry)
- Resend verification for expired or missed emails
- Async AI risk scoring via Vertex AI Gemini on every registration
- Health check endpoint for load balancer and auto-healing
- Structured JSON logging shipped to Cloud Logging via Ops Agent

## API Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/v5/user` | None | Register a new user |
| GET | `/v5/user/self` | Basic Auth | Get user profile |
| PUT | `/v5/user/self` | Basic Auth | Update profile |
| POST | `/v5/user/resend-verification` | Basic Auth | Resend verification email |
| GET | `/v1/user/verify?token=` | None | Verify email with token |
| GET | `/healthz` | None | Health check (DB connectivity) |

## Tech Stack

- Java 17, Spring Boot 3.2, Maven
- Spring Security (Basic Auth, BCrypt, stateless sessions)
- Spring Data JPA + Hibernate (MySQL, auto DDL)
- Spring Cloud GCP Pub/Sub
- Vertex AI SDK (`google-cloud-vertexai`)
- Logback with Logstash Encoder (structured JSON logs)

## Project Structure

```
webapp/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/identityhub/webapp/
│   │   │   ├── IdentityHubApplication.java        # Entry point (@EnableAsync)
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java             # User CRUD endpoints
│   │   │   │   ├── UserVerification.java           # Email verification
│   │   │   │   ├── Health.java                     # Health check
│   │   │   │   └── advice/                         # Global exception handlers
│   │   │   ├── Service/
│   │   │   │   ├── UserService.java                # Interface
│   │   │   │   ├── PubSubPublisherService.java     # Interface
│   │   │   │   ├── RiskScoringService.java         # Interface
│   │   │   │   ├── ConnectionCheck.java            # Interface
│   │   │   │   └── Impl/
│   │   │   │       ├── UserServiceImpl.java        # User business logic
│   │   │   │       ├── PubSubPublisherServiceImpl.java  # @Async Pub/Sub + risk scoring
│   │   │   │       ├── RiskScoringServiceImpl.java # Vertex AI risk assessment
│   │   │   │       ├── ConnectionCheckImpl.java    # DB connectivity check
│   │   │   │       └── CustomeUserDetailsService.java   # Spring Security user loader
│   │   │   ├── entities/
│   │   │   │   ├── User.java                       # User table (UUID PK)
│   │   │   │   ├── VerificationToken.java          # Token table
│   │   │   │   └── TrackEmail.java                 # Email tracking table
│   │   │   ├── dto/
│   │   │   │   ├── UserDto.java                    # Registration request
│   │   │   │   └── UserUpdateDto.java              # Update request
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java             # JPA (findByUsername)
│   │   │   │   └── VerificationTokenRepository.java # JPA (findByToken)
│   │   │   ├── filter/
│   │   │   │   ├── ResponseFilter.java             # Cache-Control, security headers
│   │   │   │   ├── NoQueryParamFilter.java         # Reject query params
│   │   │   │   ├── NoRequestBodyFilter.java        # Health check validation
│   │   │   │   ├── NoAuthForOpenUrlsFilter.java    # Block auth on public endpoints
│   │   │   │   └── GetUserFilter.java              # Reject body on GET
│   │   │   ├── config/
│   │   │   │   ├── FilterConfig.java               # Filter registration + ordering
│   │   │   │   └── DatabaseBootstrapConfig.java    # CREATE DATABASE IF NOT EXISTS
│   │   │   └── security/
│   │   │       └── SecurityConfig.java             # Auth rules, BCrypt, stateless
│   │   └── resources/
│   │       ├── application.properties              # DB, Pub/Sub, Vertex AI config
│   │       └── logback-spring.xml                  # JSON logging config
│   └── test/
│       └── java/com/identityhub/webapp/
│           ├── IdentityHubApplicationTests.java
│           ├── IntegrationTests/
│           │   └── UserIntegrationTests.java       # End-to-end API tests
│           ├── Service/Impl/
│           │   └── ConnectionCheckTest.java
│           └── filter/
│               ├── NoRequestBodyFilterTest.java
│               └── ResponseFilterTest.java
├── packer-dir/
│   ├── image.pkr.hcl                              # Build steps (provisioners)
│   ├── sources.pkr.hcl                            # GCE builder config
│   ├── variables.pkr.hcl                          # Packer variables
│   ├── packer.pkr.hcl                             # Plugin requirements
│   ├── scripts/
│   │   ├── prepare_opt.sh                         # Create /opt/webapp directories
│   │   ├── install_dependencies.sh                # Install Java 17
│   │   ├── configure_application.sh               # Create systemd service
│   │   ├── create_user.sh                         # Create identityhub user
│   │   └── install-ops-agent.sh                   # Install Google Ops Agent
│   └── files/
│       └── ops-agent-config.yaml                  # Ops Agent log parsing config
└── .github/workflows/
    ├── integration-tests.yml                      # PR: Maven tests
    ├── packer.yml                                 # PR: Packer validate + format check
    ├── maven.yml                                  # PR: Maven build
    └── image-build.yml                            # Main: Full CI/CD pipeline
```

## CI/CD Workflows

### On Pull Request
- `integration-tests.yml` — Spins up MySQL, runs Maven integration tests
- `packer.yml` — Validates Packer config, checks formatting
- `maven.yml` — Maven build check

All must pass before merge is allowed (branch protection).

### On Merge to Main
- `image-build.yml` — Full pipeline:
  1. Maven test + build JAR (`webapp-1.0.0.jar`)
  2. Packer builds GCE image (Java 17, JAR, Ops Agent, systemd)
  3. Creates new instance template (CMEK encrypted boot disk)
  4. Triggers rolling update on MIG (max-surge=2, max-unavailable=0)
  5. Monitors until all instances updated

## Configuration

All config via environment variables (injected by VM startup script from `.env` file):

| Variable | Description |
|----------|-------------|
| `DB_HOSTNAME` | Cloud SQL private IP |
| `DB_NAME` | Database name |
| `DB_USERNAME` | Database user |
| `DB_PASSWORD` | Database password |
| `GCP_PROJECTID` | GCP project ID |
| `TOPIC_NAME` | Pub/Sub topic name |
| `CREDS_JSON` | Path to publisher SA key file |
| `LOGFILE_PATH` | Log file directory |

## Packer Image

Packer bakes a custom GCE image with everything pre-installed:

| What | How |
|------|-----|
| OS | CentOS Stream 9 (base image) |
| Java | JDK 17 via dnf |
| App | `webapp-1.0.0.jar` at `/opt/webapp/` |
| Service | systemd unit `javaapp.service` (auto-restart, runs as `identityhub` user) |
| Logging | Google Ops Agent with JSON log parsing config |
| User | `identityhub` (non-login, owns /opt/webapp and /var/logs/webapp) |

Images are tagged with `webapp-image-family` — Terraform always picks the latest.

## Running Locally

```bash
# Prerequisites: Java 17, Maven, MySQL running on localhost

# Set environment variables
export DB_HOSTNAME=localhost
export DB_NAME=webapp
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
export LOGFILE_PATH=/tmp/logs

# Build and run
mvn clean install
mvn spring-boot:run
```

API available at `http://localhost:8080`.

## Testing

```bash
# Unit + integration tests (requires MySQL)
mvn test

# Test registration
curl -X POST http://localhost:8080/v5/user \
  -H "Content-Type: application/json" \
  -d '{"first_name":"Test","last_name":"User","username":"test@email.com","password":"pass1234"}'

# Test profile (Basic Auth)
curl http://localhost:8080/v5/user/self -u "test@email.com:pass1234"

# Health check
curl http://localhost:8080/healthz
```

## Key Design Decisions

- **@Async Pub/Sub** — User gets 201 instantly, AI scoring + email happen in background
- **Servlet filters** — Input validation before controllers (no body on GET, no query params, security headers)
- **Stateless sessions** — No server-side session state, works with load balancer without sticky sessions
- **UUID primary keys** — Globally unique, no sequential guessing
- **Structured JSON logging** — Enables Cloud Logging queries by severity, logger, and message
