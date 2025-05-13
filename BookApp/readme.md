CoverToCover
It is a comprehensive application for Android that allow the users to organiza their personal library and discover new books 

FEATURES

-User Authentication: Email/password and Google Sign-In options
-Book Management: Add, view, edit, and delete books
-Library Organization: Categorize books and mark favorites
-Search: Find books by title, author, or category
-User Profiles: Manage account settings and preferences
-Bookstore Locator: Map integration showing nearby bookstores
-Offline Support: Access your library without internet connection
-Cloud Sync: Synchronize data across multiple devices

TECHNICAL OVERVIEW

Architecture 

![Untitled Diagram.drawio.png](Untitled%20Diagram.drawio.png)


TECHNOLOGY STACK

-UI: Jetpack Compose with Material 3 design components
-Navigation: Jetpack Navigation Compose
-Local Database: Room Persistence Library
-Remote Database: Firebase Firestore
-Authentication: Firebase Authentication
-Maps: Google Maps API
-Logging: Timber

KEY COMPONENTS

Data Models

Table Name:books
The Book entity represents a digital or physical book stored in the app's local database.

| Field Name     | Type      | Description                                    | Constraints                 |
| -------------- | --------- | ---------------------------------------------- | --------------------------- |
| `id`           | `Int`     | Unique identifier for each book.               | Primary Key, Auto-Generated |
| `title`        | `String`  | Title of the book.                             | Required                    |
| `author`       | `String`  | Author of the book.                            | Required                    |
| `description`  | `String?` | A short description or summary of the book.    | Optional                    |
| `category`     | `String`  | Genre or category of the book (e.g., Fiction). | Required                    |
| `isFavorite`   | `Boolean` | Indicates if the book is marked as a favorite. | Defaults to `false`         |
| `isDownloaded` | `Boolean` | Indicates if the book has been downloaded.     | Defaults to `false`         |

This should be able to support both online and offline book collection 
Suitable  for personalized features like favorites and downloads

Table Name:users
The User entity stores information about users who have accounts within the app.

| Field Name  | Type     | Description                         | Constraints                    |
| ----------- | -------- | ----------------------------------- | ------------------------------ |
| `userId`    | `Int`    | Unique identifier for each user.    | Primary Key, Auto-Generated    |
| `firstName` | `String` | User's first name.                  | Required                       |
| `lastName`  | `String` | User's last name.                   | Required                       |
| `email`     | `String` | User's email address.               | Required, Unique (recommended) |
| `password`  | `String` | Hashed password for authentication. | Required                       |

This supports user user authentication and personalization 
Email should be unique and validated for proper format 

ViewModels

The app implements several ViewModels to manage UI state and business logic:
BookViewModel: Manages book data operations
UserViewModel: Handles user authentication and profile management
LibraryViewModel: Controls the organization of books in the library
MapViewModel: Manages bookstore location data

Repositories

BookRepository: Interface between local/remote book data and ViewModels
UserRepository: Manages user data operations
FirebaseAuthManager: Handles Firebase authentication operations

User Interface

Built entirely with Jetpack Compose for modern, declarative UI
Material 3 design system for cohesive look and feel
Responsive layouts supporting various screen sizes
Dark mode support

┌───────────────────────────┐         ┌───────────────────────────┐
│           Book            │         │           User            │
├───────────────────────────┤         ├───────────────────────────┤
│ - id: Int                 │         │ - userId: Int             │
│ - title: String           │         │ - firstName: String       │
│ - author: String          │         │ - lastName: String        │
│ - description: String?    │         │ - email: String           │
│ - category: String        │         │ - password: String        │
│ - isFavorite: Boolean     │         │ - phoneNumber: String?    │
│ - isDownloaded: Boolean   │         │ - profileImagePath: String?│
│ - imageUrl: String?       │         │ - preferences: String?    │
│ - publisher: String?      │         │ - isActive: Boolean       │
│ - publishedDate: String?  │         │ - dateCreated: Long       │
│ - pageCount: Int?         │         │ - lastLogin: Long?        │
│ - isbn: String?           │         │                           │
│ - language: String?       │         │                           │
│ - rating: Float?          │         │                           │
│ - dateAdded: Long         │         │                           │
│ - lastModified: Long      │         │                           │
└───────────────────────────┘         └───────────────────────────┘

UX/DX APPROACH

User Experience(UX)
The application adopts a modern Material Design 3 approach with a fully Compose-based UI implementation:

Consistent Design Language: Material 3 components throughout the app
Fluid Animations: Smooth transitions between screens and states
Reactive UI: Automatic UI updates in response to state changes
Component Reusability: Numerous reusable UI components

Key UX features include:

Bottom navigation for main sections
Card-based UI for book listings
Tab-based navigation for content organization
Advanced search with filtering options
Personalized profile management
Interactive maps for discovering bookstores

DEVELOPER EXPERIENCE(DX)

The development approach focuses on maintainability, testability, and modern Android practices:

MVVM Architecture: Clear separation of concerns
Repository Pattern: Abstraction layer for data sources
State Management: Using StateFlow for reactive programming
Coroutines: For asynchronous operations
Single Activity Architecture: With Compose-based navigation

Challenges:

Implementing synchronization between Room and Firestore
Transitioning to Jetpack Compose's declarative paradigm
Managing authentication with multiple providers

REFERENCES

Android Developer Documentation
Jetpack Compose Documentation
Room Persistence Library
Firebase Documentation
Google Maps Platform
Material Design Guidelines