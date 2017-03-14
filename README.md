# Problem description

Coding Puzzle

We want you to program a command line based role playing game.

Here are the stories:
- As a player I want to create a character
- As a player I want to explore
- As a player I want to gain experience through fighting
- As a player I want to save and resume a game

These will be the technical constraints:
- Use Java
- Libraries and Frameworks are only allowed for testing and build pipelines
- Use best in industry agile engineering practices
- Please build for the command line

# Solution

Building: `mvn clean package`

Resulting file `target/dungeon.jar`

Running: `java -jar target/dungeon.jar`

Runs on windows not very smoothly. Need to press Enter after each key. :(  
Tested on Ubuntu Linux in POSIX compatible terminal with UTF-8 encoding.

Design outline:
- Application have main entry point - `Dungeon.java`  
This class is responsible for setting up environment for game and starting first activity.

- All work in game is done in various activities.  
Activity is an object, that receives user input and draws it's state on display.  
There is always one main active activity. It is referenced in `Dungeon.java`  

- Activity manages window layout itself. It can have child sub-activities, for example menu or fight dialog.  
Activity delegates user input to its windows or sub-activities.

- Display is abstract and may be replaced with graphical one.
