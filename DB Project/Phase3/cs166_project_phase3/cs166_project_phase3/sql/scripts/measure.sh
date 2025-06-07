# Created by ChatGPT using old measure.sh "Modify existing code to perform direct speedup comparison 
# for each SQL query first without indexes then with indexes"

#!/bin/bash
# cs166_psql $USER'_project_phase_3_DB' < sql/scripts/create_tables.sql
source sql/scripts/create_db.sh
sleep 5

# Run queries without indexes and capture timings
cat <(echo '\timing') sql/src/queries.sql | \
psql -h localhost -p $PGPORT $USER'_project_phase_3_DB' | \
awk '/Time:/ {print $2, $3}' > /tmp/times_no_idx.txt

source sql/scripts/create_db.sh
psql -h localhost -p $PGPORT $USER'_project_phase_3_DB' < sql/src/create_indexes.sql > /dev/null

# Run queries with indexes and capture timings
cat <(echo '\timing') sql/src/queries.sql | \
psql -h localhost -p $PGPORT $USER'_project_phase_3_DB' | \
awk '/Time:/ {print $2, $3}' > /tmp/times_idx.txt

# Print side-by-side comparison
echo "Query | Time w/o Indexes | Time w/ Indexes | Speedup"
echo "------------------------------------------------------"

paste /tmp/times_no_idx.txt /tmp/times_idx.txt | nl -w1 -s'. ' | while read -r line; do
    qnum=$(echo "$line" | awk '{print $1}')
    t1=$(echo "$line" | awk '{print $2 " " $3}')
    t2=$(echo "$line" | awk '{print $4 " " $5}')
    # Extract time in ms
    t1ms=$(echo "$t1" | awk '{if ($2 == "ms") print $1; else if ($2 == "s") print $1*1000; else print "0"}')
    t2ms=$(echo "$t2" | awk '{if ($2 == "ms") print $1; else if ($2 == "s") print $1*1000; else print "0"}')
    # Calculate speedup
    if [[ -n "$t1ms" && -n "$t2ms" && $(echo "$t2ms > 0" | bc) -eq 1 ]]; then
        speedup=$(echo "scale=2; $t1ms / $t2ms" | bc)
        if (( $(echo "$speedup > 1" | bc -l) )); then
            echo "Q$qnum | $t1 | $t2 | $speedup x | 1" 
        else
            echo "Q$qnum | $t1 | $t2 | $speedup x | 0"
        fi
    else
        speedup="N/A"
        echo "Q$qnum | $t1 | $t2 | $speedup x | 0"
    fi
done > /tmp/query_results.txt

# Print results and count
awk -F'|' '{print $1 "|" $2 "|" $3 "|" $4}' /tmp/query_results.txt

total=$(cat /tmp/query_results.txt | wc -l)
faster=$(awk -F'|' '{gsub(/ /,"",$5); sum+=($5=="1")} END {print sum}' /tmp/query_results.txt)

echo
echo "Queries faster with indexes: $faster/$total"
