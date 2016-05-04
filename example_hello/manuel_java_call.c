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
   strcpy(c1[0], "java Hello"); 
   strcpy(c1[1], "java Hello"); 
   strcpy(c1[2], "java Hello"); 
   strcpy(c1[3], "java Hello"); 
   
   if(rank%8 == 0){
     system(c1[rank/8]); 
   }


   MPI_Finalize();

}
