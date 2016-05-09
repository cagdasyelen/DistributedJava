CC := mpicc
#Rules
run: parallel_spf_v2.c
		$(CC) -o $@ $^
clean: 
		rm -rf run
