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
   fclose(fp);
   int N = atoi(line);

   /* In this case there is no dynamic work scheduling needed*/
   if(rank< N && N<=size){

      char str[80];
      char number[2];
      sprintf(number, "%d", rank);
      strcpy(str, "./run_slave.sh ");
      strcat(str, argv[1]);
      strcat(str, " ");
      strcat(str, number);  
      system(str);

   }


   else if(size<N){
      int tag = 1;
      int job_status = -1;
      char str[80];
      char number[2];
      if(rank != 0){
         sprintf(number, "%d", rank-1);
         strcpy(str, "./run_slave.sh ");
         strcat(str, argv[1]);
         strcat(str, " ");
         strcat(str, number);  
         job_status =  system(str);
      }

      int job_count = size-1;
      if(rank == 0){
         int dest;
         int job;
         int exit_sum = 0;
         int check = 1;
         while(check == 1){
            MPI_Recv(&dest, 1, MPI_INT, MPI_ANY_SOURCE,tag,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
            if(dest==-1){
               exit_sum = exit_sum -1;
               if(exit_sum == (size-1)*-1){
                  check = -1;
               }
            }
            else{
               job = job_count;
               MPI_Send(&job, 1, MPI_INT, dest, tag, MPI_COMM_WORLD);
               job_count++;
            }
         } 
      } 
      
      
      
      else{
         int num;
         int loop_check =0;
         while(loop_check ==0){
            if(job_status == 0){
               job_status = -1;
               int my_rank = rank;
               MPI_Send(&my_rank, 1, MPI_INT, 0, tag, MPI_COMM_WORLD);
               MPI_Recv(&num, 1,MPI_INT, 0, tag, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
               if(num >= N){
                  int abort = -1;
                  MPI_Send(&abort, 1, MPI_INT, 0 ,tag, MPI_COMM_WORLD);
                  loop_check = -1;
               }
               else{
                  sprintf(number, "%d", num);
                  strcpy(str, "./run_slave.sh ");
                  strcat(str, argv[1]);
                  strcat(str, " ");
                  strcat(str, number);  
                  job_status =  system(str);
               }
            }
   	 }
     } 
 } 


   MPI_Finalize();
}


