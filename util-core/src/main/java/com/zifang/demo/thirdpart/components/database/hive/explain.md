# 一些有用的指令
````
hive -e 'set hive.cli.print.header=true; select * from ruibang.xx' | sed 's/[\t]/,/g'  > xx.csv
````

````
create external table gdpc(

realtime_start string,
realtime_end string,
dates string,
value string
)
row format delimited fields terminated by ','
lines terminated by '\n'
stored as textfile
location '/user/piday/zctest/gdpc'
tblproperties("skip.header.line.count"="1");

````