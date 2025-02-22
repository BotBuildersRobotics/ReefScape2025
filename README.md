
# FRC-BotBuilders-Reefscape-Public

This is Team 10002's 2025 FRC robot code for our robot, Sidon. This years code code is written in Java and is based off of WPILib's Java Command system.

# Code Highlights
Autonomous Modes:

In autonomous, the robot uses PathPlanner for various 3-piece Coral autons, as well as an auton mode to guarantee the Auto RP. We guarantee consitency through the following:

- 2 x LimeLight 3G to auto align to the reef and human player station

***

Auto Aligning to Reef:

We use our vision subsystems to constantly update our postion on the field. Then, we can generate a path to the closest reef segment.
