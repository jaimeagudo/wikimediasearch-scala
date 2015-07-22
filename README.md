
An Scala port of [wikimediasearch](https://github.com/jaimeagudo/wikimediasearch) to benchmark REST api performance

Initially based on [_Spray_ Template Project](http://github.com/spray/) 


## Bootstrap


1. Git-clone this repository.

        $ git clone <<<git repo>>>

2. Change directory into your clone:

        $ cd wikimediasearch

3. Launch SBT:

        $ sbt


4. * Start the application:

	        > re-start
        
	* Alternatively to get Hot code reload while developing 
	
    	    > ~re-start

5. Browse to [http://localhost:8090](http://localhost:8090/)

6. Stop the application:

        > re-stop



## Some performance test using AB or WRK

Server running with

* Passing these JVM Command Line Arguments or un-comment `project.clj` lines accordingly if using lein

`-Dfile.encoding=UTF-8 -Xms3G -Xmx3G -XX:+AggressiveOpts -XX:+UseCompressedOops -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:+FlightRecorder -XX:StartFlightRecording=delay=60s,duration=60s,filename=myrecording2.jfr -Dclojure.compile.path=/Users/jaime/Github/wikimediasearch/target/classes -Dwikimediasearch.version=0.0.1-SNAPSHOT -Dclojure.debug=false`

* Passing `-t 200` option and default `queue-size`


Using [ApacheBench](https://httpd.apache.org/docs/2.2/programs/ab.html). Included on Mac OS (nothing to install).  On Debian `sudo apt-get install apache2-utils`

```
    $ cd benchmark
    $ ./test_ab.sh
```



###Typical output on my laptop (Mac i5 2,6GHz)

```
This is ApacheBench, Version 2.3 <$Revision: 1604373 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 10000 requests
Completed 20000 requests
Completed 30000 requests
Completed 40000 requests
Completed 50000 requests
Completed 60000 requests
Completed 70000 requests
Completed 80000 requests
Completed 90000 requests
Completed 100000 requests
Finished 100000 requests


Server Software:        spray-can/1.3.2
Server Hostname:        localhost
Server Port:            8090

Document Path:          /search?q=Kosmos
Document Length:        1693 bytes

Concurrency Level:      10
Time taken for tests:   6.006 seconds
Complete requests:      100000
Failed requests:        0
Keep-Alive requests:    100000
Total transferred:      186700000 bytes
HTML transferred:       169300000 bytes
Requests per second:    16651.15 [#/sec] (mean)
Time per request:       0.601 [ms] (mean)
Time per request:       0.060 [ms] (mean, across all concurrent requests)
Transfer rate:          30359.08 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     0    1   1.5      0      82
Waiting:        0    1   1.5      0      82
Total:          0    1   1.5      0      82

Percentage of the requests served within a certain time (ms)
  50%      0
  66%      1
  75%      1
  80%      1
  90%      1
  95%      1
  98%      1
  99%      2
 100%     82 (longest request)

```

The performance looks awesome dealing with over 16.000 request/second. Will investigate why we have this huge difference compared to the [Clojure implementation](https://github.com/jaimeagudo/wikimediasearch) which was able to deal with just 3.000 request/second
