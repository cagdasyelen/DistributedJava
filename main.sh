STARTTIME=$(date +%s)
#command block that takes time to complete...
./run_master.sh $1 $2

MASTER_END=$(date +%s)

ibrun run $1

ENDTIME=$(date +%s)



echo "It takes $(($ENDTIME - $MASTER_END)) seconds for master execution(Overhead)..."
echo "It takes $(($MASTER_END - $ENDTIME)) seconds for workers..."
