# Gamified Micro-Learning Personal Finance Application

A native Android application built with Kotlin, combining disciplined personal budgeting tools with interactive, gamified micro-learning quizzes. Engineered with an offline-first architecture powered by Android Jetpack and Room SQLite persistence.

---

## Key Features

* **Gamified Financial Quizzes:** Interactive micro-learning modules that reward users with XP, badges, and progression tracking upon mastering budgeting concepts.
* **Offline-First Persistence:** Complete offline functionality powered by Android Room ORM and SQLite data caching.
* **Modern UI & Dark Mode:** Clean layout with adaptive UI styling, custom progression states, and full Dark Mode support.
* **Budget & Expense Tracking:** Structured category management and spending overview tools to reinforce healthy personal financial habits.

---

## Tech Stack & Architecture

* **Language:** Kotlin
* **Framework:** Android SDK
* **Architecture:** MVVM (Model-View-ViewModel), Repository Pattern
* **Database & ORM:** Room (SQLite)
* **Build System:** Gradle (Kotlin DSL — `.gradle.kts`)
* **UI Toolkit:** Android Jetpack, Material Design

---

## Project Structure

```text
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/        # Kotlin source files (UI, ViewModels, Room DAOs)
│   │   │   └── res/         # Layouts, themes, drawables, and values
│   │   └── test/            # Unit testing suites
├── gradle/                  # Gradle wrapper configurations
├── build.gradle.kts         # Root build configuration
└── settings.gradle.kts      # Project dependency and module settings
