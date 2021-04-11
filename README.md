# project-android-template

This project is a template for any hyperskill android project you want to create. 
The wizard will guide you to initialize a your custom project.

## Getting Started

### Cloning repository

Cloning via SSH: ssh://git@git.jetbrains.space/hyperskill/ad/template-android-project.git

This requires a ssh credentials. It can be defined in the MyProfile -> Security -> Git Keys

Cloning via HTTP: https://git.jetbrains.space/hyperskill/ad/template-android-project.git

### Defining project

#### `settings.gradle`
Change the `rootProject.name = 'your-project-name'` 

#### `course-info.yaml`
Also change the `title` value and `content` value 

#### `lesson-info.yaml` in your project
Change the `content` value. This value is a set of stages that you project contains. 
You can set as much stages as you want. For this template there will be only 1 stage.
 
 
### Defining stage

You can define a new stage using EduTools plugin or copy-paste the previous stage and refactor some files.
Each stage should contain: 
- `task.html` is a stage description;
- `task-info.yaml` is a file that setup stage files;
- `build.gradle` is a file for stage building and testing. It have an installed robolectric dependency for tests.
- `src` is a folder with sources.

*Note*: Don't forget to refactor packages and `AndroidManifest.xml` as well. 
You can find all `hyperskill.android.template.project` occurrences by Find action in Intellij IDEA or Android Studio.