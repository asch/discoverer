# Discoverer: Deep Relational Learning with Predicate Invention

Discoverer is a proof-of-concept of novel machine learning algorithm described in [my master thesis](https://github.com/asch/discoverer/raw/master/thesis/2013-thesis.pdf) and further extended in [Neurologic](https://github.com/asch/Neurologic) project.

* Run `java -jar discoverer.jar -h` to see the usage.

* Unpack datasets from `../in/{muta,ptc}/{examples,rules}.bz2`

* Run e.g. `java -jar discoverer.jar -e ../in/muta/examples -r ../in/muta/rules -s 999999 -le 30 -ls 30 -f 10`

* Run e.g. `java -jar discoverer.jar -e ../in/ptc/examples  -r ../in/ptc/rules  -s 999999 -le 30 -ls 30 -f 10`
