#!/bin/sh
curl -v  "http://localhost:8090/search?q=Kosmos"
curl -s  "http://localhost:8089/metrics/metrics"
curl -s  "http://localhost:8089/metrics/ping"
curl -s  "http://localhost:8089/metrics/healthcheck"