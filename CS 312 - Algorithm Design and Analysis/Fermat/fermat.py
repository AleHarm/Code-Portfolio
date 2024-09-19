import math
import random

# This is main function that is connected to the Test button. You don't need to touch it.
def prime_test(N, k):
	return fermat(N,k), miller_rabin(N,k)

# You will need to implement this function and change the return value.
def mod_exp(x, y, N): 

	if y == 0:
		return 1

	z = mod_exp(x, math.floor(y/2), N)

	if (y % 2) == 0:
		return pow(z, 2) % N
	else:
		return (x * pow(z, 2)) % N
	
# You will need to implement this function and change the return value.   
def fprobability(k):
    return 1 - pow(.5, k)

# You will need to implement this function and change the return value.   
def mprobability(k):
    return 1 - pow(.25, k)

# You will need to implement this function and change the return value, which should be
# either 'prime' or 'composite'.
#
# To generate random values for a, you will most likely want to use
# random.randint(low,hi) which gives a random integer between low and
# hi, inclusive.
def fermat(N,k):

	for i in range(k):

		randomA = random.randint(2, N - 1)

		if(mod_exp(randomA,N-1, N) != 1):
			return 'composite'

	return 'prime'

# You will need to implement this function and change the return value, which should be
# either 'prime' or 'composite'.
#
# To generate random values for a, you will most likely want to use
# random.randint(low,hi) which gives a random integer between low and
#  hi, inclusive.
def miller_rabin(N,k):
	
	for _ in range(k):
		a = random.randint(1, N - 1)
		exponent = N - 1

		ret = mod_exp(a, exponent, N)
		if ret == N - 1:
			return 'prime'
		elif ret != 1:
			return 'composite'

		while True:
			if exponent % 2 == 0:
				exponent /= 2
			else:
				break

			ret = mod_exp(a, exponent, N)
			if ret == N - 1:
				return 'prime'
			elif ret != 1:
				return 'composite'

	return 'prime'