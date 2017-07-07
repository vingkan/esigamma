#!/bin/bash
echo "Starting SIR Model Server"
javac -cp ".:lib/*" SIRServer.java
java -cp ".:lib/*" SIRServer
echo "Ending SIR Model Server"
