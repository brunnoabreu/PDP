import random
from sys import argv

n_elem = int(argv[1])

X = [random.randint(100,10000)/100.0 for i in range(n_elem)]

with open("mat.txt","w") as f_out:
	print >> f_out, " ".join([str(x) for x in X])
