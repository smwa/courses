# Courses
This is a java-based web server.
It was developed for a graduate app-development course, by myself, Ryan Corrigan, and Justin Madlock.
It uses a built-in webserver, thanks to the Java Spark library, and a file-based MySQLite instance.

The application lets users define college courses, with prerequisite courses, and run a report to find which students are eligible for their upcoming courses. It is typically ran at the end of the semester, after students grades have come in and they have enrolled in the next semester.

##Installation
Use gradle to install the packages and build the project.
There's a fatjar gradle build step that produces a single .jar for use in the docker container.
