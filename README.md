# Read Me First
The following was discovered as part of building this project:

## What this project does
A simple hostel manager built with Spring Boot and Thymeleaf. It lets you:
- Browse hostels, see details, and view currently booked rooms for each hostel.
- Create, list, and manage room allocations (guests staying in a hostel room).
- Capture guest info (name, address, DOB), contact (email), identity document type, payment method.
- Record stay details: hostel, room number, number of beds, check-in and check-out with date+time.
- Enjoy a responsive, modern UI (flex-based layout, dark mode toggle, compact header) with a client-side table search on the hostels list.

## Quick start
```bash
./gradlew clean build
./gradlew bootRun
```
Then open:
- Home page: http://localhost:8080/
- Hostels list: http://localhost:8080/hostels
- Allocations: http://localhost:8080/allocations
- Create allocation: http://localhost:8080/allocations/create

## Key routes
- GET / — home page
- GET /hostels — list all hostels
- GET /hostels/{id} — view hostel details + booked rooms
- GET /allocations — list allocations
- GET /allocations/create — allocation form
- POST /allocations/create — save allocation

## Data model (simplified)
- Hostel: id, name, address, capacity, pricePerNight, available
- Allocation: id, fullName, address, dob, email, identityDoc, paymentMethod,
  hostelId, hostelName, hostelRoomNumber, numberOfBed, checkIn (datetime), checkOut (datetime)

## Tech stack
- Spring Boot, Spring MVC, Thymeleaf 3
- H2 (in-memory) by default; see application.properties
- Gradle build (see build.gradle.kts)
- Responsive CSS in src/main/resources/static/css/style.css

## Troubleshooting
- Thymeleaf template not found: ensure the view name matches a file under src/main/resources/templates (e.g., return "hostels/list" resolves templates/hostels/list.html).
- H2 schema: Hibernate will create/update tables automatically; if you use data.sql, omit explicit id values for identity columns.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.2/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.2/gradle-plugin/packaging-oci-image.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/4.0.2/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.2/reference/using/devtools.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## Pages at a glance
Click a thumbnail to open the page when the app is running.

- [Home](http://localhost:8080/)
- [Hostels list](http://localhost:8080/hostels)
- [Hostel detail](http://localhost:8080/hostels/1)
- [Allocations list](http://localhost:8080/allocations)
- [Create allocation](http://localhost:8080/allocations/create)

| Hostels list | Hostel detail | Allocations list | Create allocation |
| --- | --- | --- | --- |
| [![Hostels list](/images/view-hostel.png)](http://localhost:8080/hostels) | [![Hostel detail](/images/view-full-hostel-details.png)](http://localhost:8080/hostels/1) | [![Allocations list](/images/allocation.png)](http://localhost:8080/allocations) | [![Create allocation](/images/add-new-allocation.png)](http://localhost:8080/allocations/create) |
