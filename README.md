# **Alpha Tech coding task**

## The task
The original task document is "Alpha-Coding homework.pdf"

Formal task definition:
Given array of integer output values and their probabilities, write a random generator which in long run generates output values with defined frequences.
Input:
int  [] randomNums: possible output values 
float[] probabilities: probabilities each output value occur in long run

int nextNum(): method, that generates random values.

Limitations:
* both arrays randomNums and probabilities must be non null
* both arrays must have equal lengths,greater than 0
* randomNums must contain different values, not necessary ordered
* probabilities must contain non-negative values
* the sum of values in probabilities array must be equal to 1.0 within some precision

Notes:
* I preserve the float data format because of the task definition, the "native" floating point format in Java is double
* precision of floating point definition of probabilities os arbitrary chosen to 1.0e-6. 

## Algorithm

I create an array of *accumulated* probabilities, where each element contains the sum of probabilities from the beginning up to this index included. This array contains non-decreasing sequence of numbers starting from the first value of *probailities* array and ending with 1.0.
Then I use this array to choose which of possible output values (in *randomNums* array) to return in each call. Here are the steps:
1. Generate an equally distributed random number in [0.0 - 1.0) interval using built-in math function
2. Find the index of the first element in *accumulated* probabilities array that contains a higher value than generated random
3. Return the *randomNum* value, corresponding to that index. 
 
## Implementation details
The class ***RandomGen*** has a single constructor that receives the necessary arguments: 
- *randomNum*: array of possible output values
- *probabilities*: array of probabilities each member of randomNums will occur in each call of nextNum

Inside the class implementation a validity check of input arguments is performed. In case of illegal arguments is detected, an *IlelgalArgumentException* is thrown with appropriate text message. 

Method *nextNum* returns one of randomNum values with corresponding probabilities. A lazy inittialization of all required internals is performed during the first call of the method. 

## Testing
**JUnit5** is used for unit testing. Two test set were implemented
**TestWrongArgs**
Tests for input arguments validation.

**TestDistributions**
A suite of four easily checked distributions. All test are performed with the same five *randomNums* values and different probabilities. Probabilities are choosen in such a way, that the corresponding results can be visually investigated. Every test case runs *N* consecutive calls of *nextNum* method and the results are collected in a map with *randomNum* values as keys and number of occurrences as values. Then this map is printed out on console or log.   
- equal distribution: all five probabilities are equal to 0.2
- singular distribution: all probabilities are small numbers (0.01) but one which is 0.96
- step distribution: first three probabilities are 0.25, last two are 0.125
- linear distribution: probabilities follow linear function: 0.10, 0.15, 0.20, 0.25, 0.30.

Every test case initializes a *RandomGen* object, performs N calls of *nextNum* method, accumulates the results in a map and prints them. It is easy to check the conformance to the corresponding distribution. In addition, the chi-squared statistics is calculated.

Here is a typical output (N = 10 000 000, randomNums: [-10, 2, 5, 40, 300])

Linear distribution

* -10: 1000435
* 2: 1498442
* 5: 1999673
* 40: 2500328
* 300: 3001122

Chi-2 = 2.3236251213591603

----------------------

Singual distribution

* -10: 100041
* 2: 9600649
* 5: 99736
* 40: 99586
* 300: 99988

Chi-2 = 2.473045164439603

----------------------

Equal distribution

* -10: 1999031
* 2: 1999342
* 5: 2000169
* 40: 1999305
* 300: 2002153

Chi-2 = 3.2594599536506346

----------------------

Step distribution

* -10: 2498538
* 2: 2500332
* 5: 2500652
* 40: 1249833
* 300: 1250645

Chi-2 = 1.4242400000000877


## Building
Jar file can be built with command "*mvn package*"



 
