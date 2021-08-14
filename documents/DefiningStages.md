### Defining stages

After configuring the main project files there is a time to table some moments about stages. 
This project already has a template stage for configuring and copy-pasting in the future.

NOTE: We strongly recommend copy-pasting modules instead of creating using the EduTools plugin, 
because this guarantees that copied module is well configured by our team and you, 
and already have a solution from the previous stage that only requires an upgrade.

#### task.html
This file is pretty simple: it contains the current stage and task description that the user 
should solve to complete this stage.

#### task-info.yaml
This file contains a files that will be visible in project (Alt + 1) panel for a course view.

NOTE: Usually, the EduTools plugin can manage this file itself, but to be sure check it sometimes.

#### task-remote-info.yaml
This file is not for manual editing, but we think you should at least understand its simple content.
The only value we are interested in is an `id` that is equal to the current stepik stage id in 
stepik lesson.

#### build.gradle
In this file, we are interested in the `applicationId` value. Please, define it using the following 
examples: `org.hyperskill.stopwatch`,`org.hyperskill.cinema` and so on. The packages that contain 
more than 3 nested names aren't welcomed. 

Proceed to the next step immediately without rebuilding project(it already broken at this point, trust us)

#### other project files
At this point you should change all `hyperskill.android.project.template` occurrences. IDE can help
you here by Find All action. The checklist of files that should be fixed:
 - AndroidManifest.xml
 - nested packages in src/main/java folder and `package` in the kotlin files as well.
 - same as previous item but for src/test/java
