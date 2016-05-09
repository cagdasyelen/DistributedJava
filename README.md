# README #

Distributed Java: A prototype of distributed Symbolic PathFinder (SPF).

###Contributors###

Cagdas Yelen
Neil Klenk

### How do I get set up? ###

* Clone the repo.
* Go to jpf-symbc-distributed directory
* Modify the script build.sh according to your machine. You can comment out the line that contains the variable RUNJAVAC and update it to your system's javac command. Note that only JDK 1.7 is supported for now.
* Run the script (./build.sh).
* The script will build the project and put all the compiled class files into jpf-symbc/dist directory.
* Go to settings directory.
* Change the CERT_FILE path to ........../DirectoryYouClonedTheRepo/temp_certf/atemp.cert
* Run distributedSPF:
Go to jpf-symbc directory. Modify the script run.sh according to your machine. You can comment out the line that contains the variable RUNJAVA and update it to your system's java command. Note again that only JDK 1.7 is supported. Then simply run this script and if everything works correctly you should see some outputs saying that "Running Symbolic PathFinder" etc.
