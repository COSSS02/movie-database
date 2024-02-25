# Movie Database

This is a project meant to help me understand OOP core principles in Java
and how to use an object-oriented design when writing code, which was partially
accomplished, the biggest problem of the current implementation being classes
with too much functionality and lots of duplicated code that needs to be refactored.

The project represents an app with both a CLI and GUI meant to manage a database
for movies, TV shows and actors. The app contains three different types of users
(regulars, contributors and admins) with different functionalities (for example,
regulars can leave a rating for a movie/actor, contributors can add an item to
the system, admins can solve user requests). The login information for the
current users is stored in the accounts.json file.

Another motivation when making this project was applying some commonly used
design patterns. The chosen design patterns are singleton for the IMDB class,
builder for creating the personal information of a new user, factory for choosing
the type of the new user, strategy for increasing user experience on their profile
and observer for sending notifications to users when different events happen (for
example, when one of their favorite movies gets a new rating).