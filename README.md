# Slack Notification Scheduler

This repository contains a Spring Boot-based Slack notification scheduler. The app is intended to receive scheduling requests, persist them to MySQL, and dispatch Slack notifications at the appropriate time.

## Current status

This codebase currently includes configuration and build setup (Spring Boot + JPA + WebMVC + Thymeleaf) but does not yet include the application source code. Start by adding a Spring Boot main class and the scheduler/Slack integration modules.

## Key configuration

- Slack bot token is read from `SLACK_BOT_TOKEN`.
- MySQL is configured at `jdbc:mysql://localhost:3306/primebot` with user `root` (empty password by default).

See `src/main/resources/application.properties` for full settings.
