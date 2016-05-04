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
   int num_nodes = size/16;

   FILE *fp;
   char *line = NULL;
   size_t len=0;
   ssize_t read; 

   fp = fopen("rangeSize.txt", "r");
   if(fp == NULL)
      return -1; 
   read = getline(&line, &len, fp);
   if(read == -1){
      printf("Number of ranges are not found in the file\nExiting...\n");
      return -1;
   }

   int N = atoi(line);

   if(rank< N){

      char str[80];
      char number[2];
      sprintf(number, "%d", rank);
      strcpy(str, "./run_slave.sh ");
      strcat(str, argv[1]);
      strcat(str, " ");
      strcat(str, number);  
      system(str);

   }

   MPI_Finalize();


}


