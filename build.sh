#!/usr/bin/env bash
git pull origin master
mvn clean
mvn install -DskipTests=true