# mutants

## Instructions
Compile
>mvn clean package

run
>java -Dserver.port=7880 -jar mutants-1.5.2.RELEASE.jar &


## Get statistics
** GET http://132.255.70.132:7880/stats **

## Querying

** POST http://132.255.70.132:7880/mutant **
{"dna":["ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"]}