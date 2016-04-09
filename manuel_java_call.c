#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argc, char *argv[]){
   char name[35];
   int namelen;
   int size,rank;

   MPI_Init(&argc, &argv);
   MPI_Comm_size(MPI_COMM_WORLD,&size);
   MPI_Comm_rank(MPI_COMM_WORLD,&rank);
   MPI_Get_processor_name(name,  &namelen);
   char c1[4][200]; 
   strcpy(c1[0], "java -cp build:lib/junit.jar:lib/javassist.jar:lib/commons-cli-1.0.jar:lib/alloy4viz.jar korat.Korat --class korat.examples.binarytree.BinaryTree --args 2 2 2 --cvStart 0 --cvEnd 4"); 
   strcpy(c1[1], "java -cp build:lib/junit.jar:lib/javassist.jar:lib/commons-cli-1.0.jar:lib/alloy4viz.jar korat.Korat --class korat.examples.binarytree.BinaryTree --args 2 2 2 --cvStart 4 --cvEnd 8");
   strcpy(c1[2], "java -cp build:lib/junit.jar:lib/javassist.jar:lib/commons-cli-1.0.jar:lib/alloy4viz.jar korat.Korat --class korat.examples.binarytree.BinaryTree --args 2 2 2 --cvStart 8 --cvEnd 12");
   strcpy(c1[3], "java -cp build:lib/junit.jar:lib/javassist.jar:lib/commons-cli-1.0.jar:lib/alloy4viz.jar korat.Korat --class korat.examples.binarytree.BinaryTree --args 2 2 2 --cvStart 12");
   if(rank%8 == 0){
     system(c1[rank/8]); 
   }


   MPI_Finalize();

}
