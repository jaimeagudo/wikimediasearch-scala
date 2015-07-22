#!/bin/sh
wrk -t12 -c50 -d10s --latency -s loadtestconfig $1
#http://localhost:8088