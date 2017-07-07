#!/bin/bash
javac -cp ".:lib/*" UnirestRunner.java
java -cp ".:lib/*" UnirestRunner $1