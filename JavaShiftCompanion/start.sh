#!/bin/bash
mvn -Pdev package exec:java -Dmaven.test.skip=true -Dexec.mainClass=shionn.ShiftCompanion -Dexec.args="-d -p /dev/ttyUSB0"