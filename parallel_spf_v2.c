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

   /* Dynamically assigning jobs to available slaves */
   else if(size<N){
 
      int tag = 1;
      int job_status = -1;
      char str[80];
      char number[2];
      
      /* Distribute the initial jobs(Ranges from 0 to size-1) to all slaves */
      if(rank != 0){
         sprintf(number, "%d", rank-1);
         strcpy(str, "./run_slave.sh ");
         strcat(str, argv[1]);
         strcat(str, " ");
         strcat(str, number);  
         job_status =  system(str);
      }

      int job_count = size-1;

      /* Node 0 is the master node, which manages the job distribution over the slaves */
      if(rank == 0){
         int dest;
         int job;
         int exit_sum = 0;
         int done_check = 0;
  
         /* Until all slaves report that they are done, distribute the jobs */
         while(done_check == 0){

            /* Receiving the rank of the next available slave */
            MPI_Recv(&dest, 1, MPI_INT, MPI_ANY_SOURCE,tag,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
            
            /* If the all jobs are done(no jobs in the queue), slave reports -1 */
            if(dest==-1){

               /* Increment the exit_sum as the slaves reports -1
                * If the overall sum equals to number of slaves*-1, exit the outher while loop  
                */   
               exit_sum = exit_sum -1;
               if(exit_sum == (size-1)*-1){
                  done_check = -1;
               }
            }

            /* Send the next job in the queue to available slave */
            else{
               job = job_count;
               MPI_Send(&job, 1, MPI_INT, dest, tag, MPI_COMM_WORLD);
               job_count++;
            }
         } 
      } 
      
      
      /* Slave nodes */
      else{
         int num;
         int loop_check =0;

         /* As long as there is a job in queue(job_num < N), stay in the while loop*/
         while(loop_check ==0){

            /* job_status is the return value of the system call
             * If it is 0, that means the slave is available and
             * can ask for a new job.
             */
            if(job_status == 0){
               job_status = -1;
               int my_rank = rank;
               
               /* The slave sends its rank to the master to indicate that it is available */
               MPI_Send(&my_rank, 1, MPI_INT, 0, tag, MPI_COMM_WORLD);

               /* Job number is being received from the master */
               MPI_Recv(&num, 1,MPI_INT, 0, tag, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
               
               /* If the job number exceeds the total number of jobs, report "done" */
               if(num >= N){
                  int done = -1;
                  MPI_Send(&done, 1, MPI_INT, 0 ,tag, MPI_COMM_WORLD);
                  loop_check = -1;
               }

               /* Otherwise, perform the job */ 
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

