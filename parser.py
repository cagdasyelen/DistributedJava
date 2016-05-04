import re
from dateutil import parser


def parse_time(seconds):
	m, s = divmod(seconds, 60)
	h, m = divmod(m, 60)
	print "%d:%02d:%02d" % (h, m, s)


with open('out.txt', 'r') as myfile:
	single_str=myfile.read().replace('\n', '')


print "File is read"

time_str = re.findall('Time (\d+:\d+:\d+)', single_str)
time = []

for item in time_str:
	temp =  item.split(':')
	time.append(int(temp[0])*3600 + int(temp[1])*60 + int(temp[2]))


print "##### timing #####"

print "Max:"
parse_time(max(time))
print "Min:"
parse_time(min(time))
print "Average:"
parse_time(sum(time)/len(time))


print "##### solvers #####"

new_str = re.findall('Calls (\d+)', single_str)

for i in range(len(new_str)):
	new_str[i]= int(new_str[i])


print "Max:"
print max(new_str)
print "Min:"
print min(new_str)
print "Average:"
print sum(new_str)/len(new_str)
