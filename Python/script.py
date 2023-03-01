import csv
import random

arr = [5.99, 5.99, 6.29, 6.69 , 6.29, 5.99 ,6.49
    ,7.19
    ,7.19
    ,7.69
    ,7.89
    ,7.49
    ,7.19
    ,7.69
    ,8.49
    ,8.49
    ,8.69
    ,8.89
    ,8.69
    ,8.49
    ,8.69
    ,5.99
    ,7.19
    ,8.49
    ,5.99
    ,7.19
    ,8.49
    ,5.99
    ,7.19
    ,8.49
    ,5.99
    ,7.19
    ,8.49
    ,2.99
    ,2.99
    ,2.99
    ,2.19
    ,2.89
    ,6.09
    ,8.49
    ,10.09
    ,6.09
    ,8.49
    ,10.09
    ,6.09
    ,8.49
    ,10.09
    ,6.09
    ,8.49
    ,10.09]
 

# open a new CSV file for writing
with open('data.csv', 'w', newline='') as csvfile:
    
    # create a CSV writer object
    writer = csv.writer(csvfile)
    orderid = 1
    for i in range(1, 366):
        #find the month
        if i <= 31:
            month = '01'
            day_of_month = i
        elif i <= 59:
            month = '02'
            day_of_month = i - 31
        elif i <= 90:
            month = '03'
            day_of_month = i - 59
        elif i <= 120:
            month = '04'
            day_of_month = i - 90
        elif i <= 151:
            month = '05'
            day_of_month = i - 120
        elif i <= 181:
            month = '06'
            day_of_month = i - 151
        elif i <= 212:
            month = '07'
            day_of_month = i - 181
        elif i <= 243:
            month = '08'
            day_of_month = i - 212
        elif i <= 273:
            month = '09'
            day_of_month = i - 243
        elif i <= 304:
            month = '10'
            day_of_month = i - 273
        elif i <= 334:
            month = '11'
            day_of_month = i - 304
        else:
            month = '12'
            day_of_month = i - 334 

        for j in range(1, 240 ):
            hour = random.randint(8,20)
            minute = random.randint(0,59)  
            second = random.randint(0,59)  
            employeeid = random.randint(1, 22)
            customerid = random.randint(1, 2000)
            item1 = random.randint(1,50)
            item2 = random.randint(1,50)
            orderlist = "{}, {}".format(item1, item2)
            cost = round((arr[item1-1 ] + arr[item2 -1] ) * 1.0625, 2)
            # example '2016-06-22 19:10:25'
            ordertime = "2022-{}-{} {}:{}:{}".format(month,day_of_month,hour,minute,second) 
            writer.writerow([orderid, ordertime, employeeid, customerid, orderlist, cost])
            orderid += 1
        for k in range(1, 140 ):
            hour = random.randint(8,20)
            minute = random.randint(0,59)  
            second = random.randint(0,59)  
            employeeid = random.randint(1, 22)
            customerid = random.randint(1, 2000)
            item1 = random.randint(1,50) 
            orderlist = "{}".format(item1 )
            cost = round((arr[item1-1 ] ) * 1.0625, 2)
            # example '2016-06-22 19:10:25'
            ordertime = "2022-{}-{} {}:{}:{}".format(month,day_of_month,hour,minute,second) 
            writer.writerow([orderid, ordertime, employeeid, customerid, orderlist, cost])
            orderid += 1  
        if(i == 295 or i == 316 ):
            for p in range(1, 140 ):
                hour = random.randint(8,20)
                minute = random.randint(0,59)  
                second = random.randint(0,59)  
                employeeid = random.randint(1, 22)
                customerid = random.randint(1, 2000)
                item1 = random.randint(1,50) 
                orderlist = "{}".format(item1 )
                cost = round((arr[item1-1 ] ) * 1.0625, 2)
                # example '2016-06-22 19:10:25'
                ordertime = "2022-{}-{} {}:{}:{}".format(month,day_of_month,hour,minute,second) 
                writer.writerow([orderid, ordertime, employeeid, customerid, orderlist, cost])
                orderid += 1
print("CSV file created successfully!") 