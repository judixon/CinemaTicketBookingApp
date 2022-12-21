# CinemaTicketBookingApp

## Table of contents

* [Additional assumptions](#additional-assumptions)
* [Building and running the application](#building-and-running-the-application)
  * [System requirements](#make-sure-that-the-system-on-which-you-want-to-run-the-application-meets-the-following-requirements)
  * [How to start the application?](#how-to-start-the-application) 
* [Potential questions and answers](#potential-questions-and-answers)
## Additional assumptions
* Reservation expiration time is equal to 3 days.
* When choosing a particular screening, system should return schema of free and reserved seats of the screening room.
* User must choose type of ticket for each seat before submitting reservation (number of tickets must be equal to the number of chosen seats).
* In case of two part surname, both parts should be at least 3 characters long and start with capital letter.
* There cannot be single place left over also at the beginning and at the end of the row.
* You can't reserve already reserved seat.

## Building and running the application
#### Make sure that the system on which you want to run the application meets the following requirements:
1. You must have JAVA 17 version in your system. If You don't have that one, You can install it from the following link:
`https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html`.
2. Environmental variable JAVA_HOME must point to location of JDK with version > 17.
3. You must have installed Apache Maven in your system. If You don't have that one, You can install it from the following link:
`https://maven.apache.org/download.cgi`.

#### How to start the application?
1. Go to `script` directory (path from main directory of the application: `./scripts`).
2. Open bash in the above directory.
3. Build the application by running the build.sh script with the following command `./build.sh`.
4. After build is done, You can run the application by running the run.sh script with the following command `./run.sh`.
5. **You can also build and run application at one go by running the build-and-run.sh script with the following command `./build-and-run.sh`.
6. You can shut down the application by running the shutdown.sh script with the following command `./shutdown.sh`.
## Potential questions and answers
* Q: Why optimistic lock is implemented in the Seat class?
  * A: When requesting "@GET /screening/{id}" You can see the list (and also the schema) of free seats for the particular screening. You can choose few of these free seats and manage to create reservation by requesting "@POST /reservations" with required data. In the vast majority of cases everything should be fine, but there is a corner case when two users (threads in application) want to make a reservation for at least one the same seat. In this situation when two transactions are opened in parallel and read the same record (seat in this case) they recognize it as free and after committing one of these transaction, the second transaction won't know that the data in database changed so the second transaction will be also committed and that will result in creating two reservations for the same seats for the same screening. If these transactions were opened sequentially, the program code would check if the seats chosen to reserve in the second transaction are not already reserved. Optimistic lock prevents write/update/delete operations to be executed in simultaneous opened transactions - in this case when two transaction want to update the record they read the record at the beginning, then they check the value of 'version' field and before committing they read the value of 'version' field again. If the value change, there is a rollback of transaction. The value of version field is changed after every transaction commit, so in this case only one of simultaneous transaction will make reservation for the seats - the other would be rollbacked because it would read different value of 'version' field before committing from the value read at the beginning. Optimistic lock is used in this case because in opposition to pessimistic lock, optimistic lock allows reading particular record(s) from database when there is any opened transaction which want to insert/update/delete any record protected with lock. So even if the user makes the reservations for particular seats, another user can read this data for example in purpose to check the schema of screening room seats. 
* Q: Why exceptions are handled by the @ControllerAdvice?
  * A: In my opinion it's clearer and neater way to handle exceptions than when doing that in Controllers or Services classes. Thanks to this solution, the code in methods where Runtime Exceptions could be potentially thrown is easier to read and to understand. Moreover it is possible to avoid code duplication when some exceptions are handled in the same way in different methods.
* Q: Why not all methods have been unit tested?
  * A: I made unit tests only for methods with significant logic. Simple methods (e.g. methods that map from entities to dto and vice versa in DtoMappers classes) doesn't require unit tests. These simple methods have been tested during checking the whole flow in the integration tests. 
  
**Feel free to ask me questions about the application, I will be happy to try to explain my view on my solution.**