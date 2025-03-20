# Roomify - Hotel Management App

Roomify is a mobile application designed to streamline hotel operations and enhance guest experiences. The app offers features such as room search, booking, reviews for guests, and room inventory management, booking management, and reporting for admins.

## Features

- **Guest Features:**
  - Room search
  - Room booking
  - Leave reviews for rooms
  
- **Admin Features:**
  - Manage room inventory
  - Manage bookings
  - View and generate reports

- **Other Features:**
  - Multi-location management
  - Dummy payment integration
  - Personalized notifications (via Firebase)

## Technologies Used

### Frontend (Mobile Application)
- **Android (Java):** The main platform for the app, developed using Java. 
- **Android Studio:** IDE for developing the Android application.
- **Firebase:** Used for handling notifications to users.
- **Material Design:** For implementing modern UI components like buttons, cards, and navigation drawers.

### Architecture
- **MVVM Pattern**: The app follows a basic MVVM architecture where the UI (View) interacts with ViewModels that handle business logic, and models represent the data.

### Features Implemented
- **Admin Dashboard**: Displays critical hotel information like total bookings, available rooms, and total revenue. 
- **Room Management**: Admins can view room details, availability, and booking status.
- **Booking Management**: Admins can manage and view bookings made by guests.
  
### Pending Integrations
- **Backend**: A backend API for handling room and booking data could be implemented using Node.js or ASP.NET Core.
  
### Future Enhancements
- Integrating a backend with a database for real-time data management.
- Further refining the UI/UX for a more intuitive experience.
