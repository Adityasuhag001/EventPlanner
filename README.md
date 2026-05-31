# Event Planner App

My SIT305 Task 4.1 assignment. It's a personal event planner Android app where you can add upcoming events, edit them, and delete them. Data is saved locally so it stays even after closing the app.

Built using Kotlin in Android Studio.

## What it does

- Add an event with a title, category, location and date/time
- See all your events in a list, sorted by date
- Tap an event to edit it
- Delete an event (with an UNDO option)
- Saves everything to a local database so nothing is lost when you close the app

## Tech stuff I used

- Kotlin
- Room (for the database)
- Fragments + Navigation Component
- Bottom Navigation Bar
- LiveData + ViewModel (MVVM pattern)

## How to run it

1. Clone this repo
2. Open the folder in Android Studio
3. Wait for Gradle to sync (took a while for me the first time)
4. Run it on an emulator or a real phone (min Android 7.0)

## Subtasks done

- [x] CRUD operations (add, view, edit, delete)
- [x] Room database for storing events
- [x] Bottom Navigation with two fragments
- [x] Validation (title can't be empty, can't pick a past date, shows snackbars on errors)


## Folder structure

```
app/src/main/java/com/adityasuhag/eventplannerapp/
├── data/           (Room stuff - Entity, DAO, Database)
├── viewmodel/      (ViewModel + Repository)
├── ui/             (Fragments + RecyclerView adapter)
└── MainActivity.kt
```

## Author

Aditya Suhag
Deakin University - SIT305
