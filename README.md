# FingerTrip
Social Media App based on Firebase

## Screenshots

<p>
<img src="https://user-images.githubusercontent.com/78906777/215409946-9715d859-c6e4-4897-858e-d014caa97bd6.png" height="400" alt="home"/>
<img src="https://user-images.githubusercontent.com/78906777/215410335-db87cb6f-f7a6-4528-a1aa-c655a23c7149.png" height="400" alt="fg1"/>
<img src="https://user-images.githubusercontent.com/78906777/215410103-21040a8f-46d4-4c7a-bf90-905020fec85f.jpg" height="400" alt="fg2"/>
<img src="https://user-images.githubusercontent.com/78906777/215410181-d48d028b-674f-4697-9ae8-5eb53c31f6eb.jpg" height="400" alt="fg3"/>
<img src="https://user-images.githubusercontent.com/78906777/215410228-d1289aa6-eca2-44d3-8437-e56dd3a93a29.jpg" height="400" alt="fg4"/>
</p>

## Features

* Built a Social Media App that performs User Authentication using Firebase Authentication and stores User data on Firebase Real-time
database and Firebase Storage.
* Implemented Fragments, ViewModels to persist data and Async Tasks to work on background thread.
* Implemented HomeFeed, ExploreScreen, UserProfileScreen, Create Posts, Edit Profile, etc. for easy browsing, posting & profile management.
* Users can follow/unfollow and like, comment, and save others’ posts.

## Tech Stack

**Client:** Java, XML, Android Studio

**Server-side:** Firebase

## Requirements

*   [Android Studio](https://developer.android.com/studio) (installed on a Linux, Mac or Windows machine)

*   Android device in
    [developer mode](https://developer.android.com/studio/debug/dev-options)
    with USB debugging enabled

*   USB cable (to connect Android device to your computer)

## Build and run

### Step 1. Clone App source code

Clone the GitHub repository to your computer to get the
application.

```
https://https://github.com/adiupd123/FingerTrip
```

Open the App's source code in Android Studio. To do this, open Android
Studio and select `Open an existing project`, going to the folder containing the Project folder

### Step 2. Build the Android Studio project

Select `Build -> Make Project` and check that the project builds successfully.
You will need Android SDK configured in the settings. You'll need at least SDK
version 23. The `build.gradle` file will prompt you to download any missing
libraries.

### Step 3. Install and run the app

Connect the Android device to the computer and be sure to approve any ADB
permission prompts that appear on your phone. Select `Run -> Run app.` Select
the deployment target in the connected devices to the device on which the app
will be installed. This will install the app on the device.
