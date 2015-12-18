# rock-paper-scissors-gui

A Rock-Paper-Scissors game using the Server-Client architecture
to simulate a human playing against a computer.

The game has two difficulties: beginner and veteran. If the user chooses beginner, the
computer starts with no patterns in its memory. Otherwise, the computer will use
the moves saved from previous games to predict the user's moves. 

The computer's move prediction consists of checking the user's last three moves and
checking the pattern history to see what move was most likely to occur next.

It takes approximately 15-20 turns before the computer can reliably predict the
user's move.

A GUI is also provided to keep score and print any related info.

![image](https://cloud.githubusercontent.com/assets/10593907/11893834/34a09c52-a528-11e5-89ca-b7473a2a3be9.png)
