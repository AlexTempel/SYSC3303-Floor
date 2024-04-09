SYSC 3303 Group 8 Floor Subsystem

File Descriptions:
Floor.java: data about a floor
FloorSubsystem.java: Logic for the floor system, used for reading in the input file, parsing the data into timedrequest and request object, then checks request
and if their is a request at the current local time coverts the request to a packet and sends it to the scheduler.
FloorSubsystemTest.java: used for testing the methods in floor subsystem
Main.java: Starts and configures the Floor subsystem
Request.java: Construct of what is sent to scheduler, as well as getters and setters for each variable.
TimedRequest.java: Associates timestamp with the request so it can check when to send it

Responsibilities:
Jake Calder was primarily responsible for the Floor Subsystem
Nick Nemec created the readCSV method
