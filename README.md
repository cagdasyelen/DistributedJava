# README #

Distributed Java: A prototype of distributed Symbolic PathFinder (SPF).

###Contributors###

* Cagdas Yelen
* Neil Klenk

### How do I get set up? ###

* Clone the repo.
* Go to jpf-symbc-distributed directory
* Modify the script build.sh according to your machine. You can comment out the line that contains the variable RUNJAVAC and update it to your system's javac command. Note that only JDK 1.7 is supported for now.
* Run the script (./build.sh).
* The script will build the project and put all the compiled class files into jpf-symbc/dist directory.
* Go to settings directory.
* Change the CERT_FILE path to ........../DirectoryYouClonedTheRepo/temp_certf/atemp.cert
* Create directory named temp_certf in the same directory you cloned the repo
* Compile parallel_spf_v2.c by typing "make"
* Run main.sh by supplying one of the examples(quicksort, wbs, redblacktree) under settings and number of jobs(Partitions or Ranges) as arguments. Note that if you try to give more than 100 jobs, master execution may produce an exception since the INITIAL_DEPTH under setting configurations are arranged to give small number of jobs for performance. For larger number of jobs, INITIAL_DEPTH should be changed.
* Example run: "./main.sh redblacktree 50"
