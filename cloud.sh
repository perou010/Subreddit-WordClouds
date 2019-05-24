#! /bin/sh
python /home/ryan/scrape.py $1

cd /home/ryan/IdeaProjects/Project3/src/

javac SubredditCloud.java
java SubredditCloud $1

cd /home/ryan/SubWordClouds
var=".png"
xdg-open "$1$var"
