#!/bin/bash
(cd .. ; java -jar target/CinemaTicketBookingApp-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > scripts/log.txt 2>&1 &
echo $! > scripts/pid.file)