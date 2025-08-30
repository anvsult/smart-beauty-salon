# 💇‍♀️ SmartBeauty – Smart Beauty Salon Manager

SmartBeauty is a modern, user-friendly Android app built with **Jetpack Compose**, designed to streamline beauty salon management and appointment booking. It offers two roles: **Customers** can browse services and book appointments, while **Salon Owners** can manage services, bookings, and client preferences.


## ✨ Features

### 👤 User Roles
- **Customer**: Browse services, select dates and times, add notes, and book appointments.
- **Salon Owner**: Manage service catalog, view appointments, and handle customer data.

### 📅 Booking System
- Material date and time pickers for smooth scheduling.
- Input validation to prevent past or invalid bookings.

### 🖼️ Service Catalog
- Add and edit beauty services with name, description, price, duration, and image.
- Store and display service information with Room DB and ViewModel integration.

### 🔒 Local Data Storage
- Uses **Room Database** for persistent offline storage.
- MVVM architecture with a single-activity, composable navigation flow.

## 📷 Video Showcase

### https://github.com/user-attachments/assets/95ed67b7-ec9f-42d2-8672-0cad1a14e6dd

## 🛠️ Built With

- **Jetpack Compose** – Declarative UI framework
- **Navigation Component** – Seamless in-app routing
- **Room Database** – Local persistence
- **Material Dialogs** – Intuitive date/time pickers
- **ViewModel & LiveData** – State management
- **Kotlin Coroutines** – Asynchronous booking workflows


## 🧪 Testing

- UI tested manually with various booking scenarios.
- Handles invalid input and edge cases (e.g., past dates, missing notes).


## 🚀 Getting Started

### Prerequisites
- Android Studio Giraffe or later
- Kotlin 1.9+
- Android SDK 33+

### Run Locally

1. **Clone this repository:**

   ```bash
   git clone https://github.com/anvsult/smart-beauty-salon.git
   cd smartbeauty
   ```
2. **Open with Android Studio and let Gradle sync.**

3. **Run the app on an emulator or physical device.**
