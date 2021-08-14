### Defining project

Now, when your future project was loaded you should properly configure it. In this step, we will 
change several root files.

*NOTE*: Before configuring you can open your terminal (Alt + F12 for Android Studio and IntelliJ IDEA) 
and run gradlew clean build command. This command runs a project build and if the project is 
configured success (it should be) your build result will meet you with green words BUILD SUCCESSFUL. 
Otherwise, please, contact me or someone, because there is something not good in our repository 
at now, that should be fixed ASAP.

*NOTE*: After each step/file you can also run build by the previous command or using a green hammer 
button in the top IDE panel.


#### `settings.gradle`
At first, find a `settings.gradle` file in your root project directory. Find a 5 line and change 
the `rootProject.name = 'hyperskill-android-project-template'`. Please, replace 
`hyperskill-android-project-template` with your project title. You can check our previous projects 
and select a similar naming format, like `calculator-tip`, `cinema-room-manager`, and so on.


#### `course-info.yaml`
This file also exists in the root project directory and describes a project for hyperskill servers.
At first, let's change the `title` value placed in the 2 line. Here you should also select a proper 
project title name because this title will be displayed in the hyperskill. So, you also can check 
our previous projects for examples. Here is one of them: `Cinema Room Manager`

`summary` value contains a short project description that will be also displayed
in the hyperskill, so here you can bravely go to the hyperskill and check several projects summary.
This field also supports an html, so you can use a hyperlink and other stuff as well.

NOTE: You can configure previous values later, but don't forget about it.

The last value we should change is `content`. Here we should also rename `hyperskill-android-project-template`.
This value is linked with the folder in our project root. At now, they have the same titles and hyperskill
can define that our project lies in this folder. If their title will be different the EduTools plugin might not
work properly, so feel free to change and this folder title too.

#### `lesson-info.yaml` in your project
This file you can find in the freshly renamed folder from the previous step. In this file, we define a
set of stages that our project will use. Feel free to create as many stages as your project requires
inside the `content` value.

NOTE: For this template, there will be only 1 stage. You also can check stage names from our
previous projects, but here is one of them: `Input changes`.

NOTE: This file also may contain a `current_task` value that defines in which stage the user is located now.
This value should be managed by the EduTools plugin.
