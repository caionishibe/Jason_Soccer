+-----------------------------------------------------------------------------------------------+
|                                             $TEWNTA                                           |
|                                                                                               |
|                                  Robotic Soccer Simulator for                                 |
|                                    the F180 Robocup League                                    |
|                                                                                               |
|                                         Release 1.0                                           |
|                                                                                               |
|                       Created by Gabriel Girardello Detoni, Brazil 2008.                      |
|																								|
|						           Maintained by Tewnta team									|
|								http://code.google.com/p/tewnta                       			|
|																								|
|   This project is hosted and distributed under Google Code terms and conditions.              |
|   All feedback is welcome as well as new members. If you want to be a part of it check the    |
| project home at Google code.																	|
|                                                                                               |
+-----------------------------------------------------------------------------------------------+

1. LICENSE
 ---------
  This program can be freely copied and distributed according to LGPL license terms.

2. INSTALLING AND RUNNING
 ------------------------
  1) Make sure to have JRE 1.6 or higher installed and JAVA_HOME environment variable correctly 
set as well as JRE \bin directory in the PATH environment variable.
    Ex: 
        JAVA_HOME=C:\Program Files\Java\jre1.6.0_01
        PATH=%PATH%;C:\Program Files\Java\jre1.6.0_01\bin
  2) Make sure to have JAXB 2.1 in the endorsed classpath (%JAVA_HOME%\lib\endorsed).
    Ex:
        Download the latest JAXB from https://jaxb.dev.java.net and put the jaxb-api-2.1.jar into 
        C:\Program Files\Java\jre1.6.0_01\lib\endorsed
  3) Get the latest $TEWNTA binary release from http://code.google.com/p/tewnta/downloads/list 
and extract all files.
  4) Execute the simulator.cmd file.

  2.1 Demonstration 
   -----------------
   Inside this package there is a demonstration team that can be used as a basis to understand 
 the APIs as well as the game dynamics. 
   To execute the demo run the demo.cmd file.

3. API REFERENCE
 ---------------
  $TEWNTA runs a SOAP web service to allow upcoming connections. Check the project wiki at 
http://code.google.com/p/tewnta/w/list to get the latest API documentation.
    