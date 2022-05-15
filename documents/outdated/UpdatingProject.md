### Updating the project

There might be bugs for updating and loading a project to the hyperskill. We will try to warn you 
as much as possible, but be ok if something will be broken or not working properly.

NOTE: Before hyperskill, all project descriptions also load to Stepik service. There is a 
possibility to fix the tasks description using Stepik, but **we highly recommend do not do that 
and use project repository**, because after fixing on Stepik, when someone changes tests or updates 
the project these changes will be overwritten with files from this repository. tl;dr: repository > stepik

#### Updating the project using EduTools plugin

NOTE: Check that you have the latest plugin version, because sometimes IDE can "forgot" to offer
a plugin update for you.

##### Preparations

First of all you should connect your EduTools plugin with your Stepik account. You can connect it 
from File -> Settings (or Ctrl + Alt + S)-> Tools -> Education -> Stepik.

The second step is to check that you able to edit the lesson on Stepik (in browser). Also check 
that EduTools plugin can edit lessons too. You can switch this feature from 
Help -> Enable Course Creator Features. 

After that open the finding tool for whole IDE actions (Ctrl + Shift + A) and start to type the 
`Experimental features`. In the selected list find `edu.course.hyperskill` and enable it by 
checking the corresponding box.

##### Update project files for Stepik

This is not a big deal, but we decide to move it to another paragraph because all hyperskill actions 
require updating a stepik project as well. All you should do is to complete the Preparations paragraph 
and just use EduTools plugin for updating the project. In the project tree (Alt + 1) select `Course`, 
right-click on a project name (an icon with 4 squares), and click `Course Creator` -> 
`Update Hyperskill lesson on Stepik`

After that and a short chilling time, while project updates you should see a pop-up window that 
"Hyperskill lesson updated". If you see “Failed to publish” or something similar once again, 
repeat point 7 until you see “Hyperskill lesson updated”. 

It’s not a joke, really, sometimes you aren’t able to publish your fixes for multiple consecutive tries. 
This is a really annoying behavior for a plugin, but we have what we have(deserves, lol).

##### First upload to the Hyperskill

This process is most obscurity. First of all, you should update a stepik lesson as following in above.
For the moving you should process to the [following link](https://stepik.org/course/47503/syllabus), 
and find `Android` section. Here you should have a several permissions: being able to see all 
Hyperskill projects and able to create a new one.

I don't have one of them. That all, folks.

##### Update Hyperskill project

You already know how to do that. Just follow the `Update project files for Stepik`. 

You also can do a project hot fix without a repository. To do that you should press `Ctrl` + `Shift` + `A`,
start to type a `Get Hyperskill lesson from Stepik` and click on a tip from IDE. The dialog window will be opened,
where we should enter a lesson id from stepik. For example, for the lesson https://stepik.org/lesson/247701/ 
the ID would be 247701. 

After that, all project files will be uploaded as a project. Starting from this point you are able to 
edit a project files and that uploading them back. 

**Note**: when you update a project this way, the repository files will not be updated, so this 
is only for exclude cases.


