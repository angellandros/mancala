# Mancala
[![Build Status](https://travis-ci.org/angellandros/mancala.svg?branch=master)](https://travis-ci.org/angellandros/mancala)

Mancala is a two-player turn-based strategy board game. This project provides a basic server for this game.

## Run Server
The server is a Maven-based Java application. To run it, do:
```
$ mvn spring-boot:run
```
You can alternatively build the JAR and run it:
```
$ mvn clean install
$ java -jar target/mancala-0.0.1-SNAPSHOT.jar
```
To verify the server is up on port 8080, you can do:
```
$ curl -XPOST "localhost:8080/start/?player1=p1&player2=p2"
``` 

## Run Client
To test the web server there is a limited Python client implemented.
To run this client you must have the package `requests` installed.
```
$ python3 client.py
```

## Run Tests
To run the unit tests you can do:
```
$ mvn test
```
The two following commands also check for style problem and dependency problems, respectively:
```
$ mvn fmt:check
$ mvn dependecy:analyze
```
Travis-CI also runs all these tests for every push.

## Custome Client
The server exposes two endpoints:
- POST `/start`
    - `player1` : String
    - `player2` : String
- PUT `/play`
    - `player` : String
    - `index` : Integer
    
The result for both of them is a game board JSON:
```
{
    "board":{
        "Ludwig":[
            {
                "index":0,
                "stones":6,
                "lastUpdated":false,
                "bigPit":false
            },
            ...
        ],
        "Wolfgang":[
            {
                "index":0,
                "stones":6,
                "lastUpdated":false,
                "bigPit":false
            },
            ...
            {
                "index":6,
                "stones":0,
                "lastUpdated":false,
                "bigPit":true
            }
        ]
    },
    "dran":"Ludwig",
    "length":6,
    "finished":false
}
```
The attribute `dran` indicates whose turn is it at the moment, 
and `finished` indicates if the game is over (in which case `dran` shows the winner).

The current implementation is not distributable, but supports online multiplaying and multiple simultaneous games,
with the restriction that each player can play one game at a time.
Also, it is possible to have multiple instances of the application, only if a
load-balancer redirects a user always to the same server.