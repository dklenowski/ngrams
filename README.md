ngrams
======

Java API to parse google ngrams http://storage.googleapis.com/books/ngrams/books/datasetsv2.html into redis based on the type of word.

Note, Requires https://github.com/dklenowski/jedisutil

### To Build

    gradle build


### To run

- Configure a redis store to run on `127.0.0.1:7000`
- Generate the `setclasspath.sh` file:

        gradle writecp

- You should now be able to run the parser using:

        $ . ./setclasspath.sh
        $ java -server -Xmx2048m com.orbious.google.ngrams.Parser
        you must specify a <inputfile>?
        Usage: Parser [-h] -i <inputfile>
           -h               Print this help message and exit.
           -i <inputfile>   The inputfile to process.

### To deploy to a local maven repository

    gradle uploadArchives
