# task-scheduling

Welcome to Project 131 - Parallel Task Scheduling with Communication Delays using Column Generation

It should be noted that this implementation does not include true column generation as it was found that our specific problem definition did not
easily lend itself to the method. This implementation uses a variety of heuristics to generate new columns instead.


To Run:
###
CPLEX must be installed to run this program and requires Java 11.

With CPLEX installed please also ensure that the cplex.jar file is included in the class path. Additionally, CPLEX comes with a native library that
is required in runtime. To include it something such as "-Djava.library.path=/Applications/CPLEX_Studio221/cplex/bin/x86-64_osx" can be included as a VM
argument. The path given here being an example of a path for OSX users. 

Additional Note:
It has been found that CPLEX does not yet support Apple Silicon Chips, therefore this program will not work on Macs with M1 or M2 chips.

To Customise Runtime:
Most customisation options are found in the Main class. Here, problem sets to run and exclude can be determined through commenting and uncommenting.
The name of the output file is also determined here. 

In HeurisitcSolver, all the high level heuristic logic is set here. Here variables can be set to alter the behaviour of the 
program, such as randomShakeLimit for example, which is currently set to 1 (meaning after 1 attempt to kick restart the program will terminate)

In the ScheduleList class, most of the heuristic based mutations can be found. The limits here can be set to generate more or less columns within each iteration.
For example, through testing problem sets of size 20-50, it was found that the upper bounds for mixing schedules performed best at 12, which is its current value.
