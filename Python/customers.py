import csv
import random

# define the data to write to the CSV file
 

# open a new CSV file for writing
with open('customers.csv', 'w', newline='') as csvfile:
    
    # create a CSV writer object
    writer = csv.writer(csvfile)
    
    for i in range(1, 2000):
            val = random.randint(0,1)
            if(val ==1):
                gender = "'Male'"
            else:
                gender = "'Female'"   
            writer.writerow([i+1, gender ])
          
         
print("CSV file created successfully!") 