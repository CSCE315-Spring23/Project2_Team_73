import csv
import random

# define the data to write to the CSV file
 

# open a new CSV file for writing
with open('data.csv', 'w', newline='') as csvfile:
    
    # create a CSV writer object
    writer = csv.writer(csvfile)
    
    for i in range(0, 110000):
        
        writer.writerow('DEFAULT, ')
print("CSV file created successfully!") 