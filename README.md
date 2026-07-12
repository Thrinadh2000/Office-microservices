# Office Booking — Microservices Edition

Two independent Spring Boot services, each with its own database:

| Service | Port | Owns | Database |
|---|---|---|---|
| `office-service` | 8081 | Desks | `data/officedb` (H2, its own file) |
| `booking-service` | 8082 | Bookings | `data/bookingdb` (H2, its own file) |

They talk to each other only over HTTP — `booking-service` calls `office-service`'s
REST API, it never touches `office-service`'s database directly. That boundary
is the whole point of microservices: each service is free to change its own
internals (swap H2 for Postgres, change its schema, rewrite its logic) without
breaking the other, as long as the API contract stays the same.

## Run it

Start **office-service** first (booking-service depends on it being reachable):

```bash
cd office-service
mvn spring-boot:run
```

In a second terminal, start **booking-service**:

```bash
cd booking-service
mvn spring-boot:run
```

Open `http://localhost:8082` in a browser — that's the live booking feed
(booking-service owns the WebSocket layer since that's where booking events
happen).

## Try it

```bash
# List desks (from office-service directly)
curl http://localhost:8081/api/desks

# Book a desk (through booking-service — this triggers the cross-service call)
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"deskId":"D1","employeeName":"Alice"}'

# See the booking (from booking-service's own database)
curl http://localhost:8082/api/bookings
```

Any browser tab open on `http://localhost:8082` updates instantly the moment
that POST succeeds.

## What happens on a booking request (step by step)

1. Client sends `POST /api/bookings` to **booking-service**.
2. `BookingService` calls `GET http://localhost:8081/api/desks/{id}` on
   **office-service** to check the desk exists.
3. `BookingService` calls `PATCH http://localhost:8081/api/desks/{id}/status`
   on **office-service** to flip it to `BOOKED`. office-service is the one
   that rejects it with a 409 if it's already taken — booking-service doesn't
   duplicate that rule, it just relays the result.
4. Only after that succeeds does `BookingService` save a `Booking` row in its
   **own** database.
5. `BookingService` broadcasts the event over WebSocket to every connected
   browser.

If step 2 or 3 fails (office-service is down, or the desk is already booked),
booking-service never writes a booking record — so the two databases can't
drift out of sync.

## Databases

Both use H2 in file mode (`./data/officedb`, `./data/bookingdb`) — a real SQL
database, persisted to disk, but zero install required so this runs anywhere.
Each service's `application.properties` has a commented block showing exactly
what to change to point at real Postgres instead — it's a config change, not
a code change, because the Repository layer talks to Spring Data JPA, not to
H2 or Postgres specifically.

You can browse office-service's data live at `http://localhost:8081/h2-console`
(JDBC URL: `jdbc:h2:file:./data/officedb`, user `sa`, blank password).

## What's still simplified (on purpose)

- **Service discovery**: booking-service finds office-service via a hardcoded
  URL in `application.properties`. A real deployment would use something like
  Eureka, Consul, or Kubernetes DNS so services find each other automatically
  and can scale to multiple instances.
- **Sync vs async communication**: booking-service calls office-service
  synchronously (waits for the response). At larger scale you'd often use a
  message broker (Kafka/RabbitMQ) so booking-service publishes an event and
  moves on, instead of blocking on office-service being available.
- **No API gateway**: clients call each service's port directly. A gateway
  (Spring Cloud Gateway, or just nginx) would sit in front and give you one
  URL for everything.
- **No auth**: neither service checks who's calling. Add Spring Security /
  OAuth2 before this touches real users.

These are the natural "next next steps" once the two-service split itself
feels solid.
