### How to Git this template

While cloning this template project, the remote url maps with "origin". 
The first what you should do is to rename it to "template". Use the following 
commands in the terminal (Alt + F12 for IDE):
`git remote rename origin template`

Verify that our remote has changed:
`git remote -v`

This feature allows monitoring and management repository updates. For example, we update 
a robolectric library version and/or target sdk version. All you should do to keep your 
project actual is just pull changes from template remote and fix possible git conflicts.