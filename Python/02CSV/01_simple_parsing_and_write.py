# -*- coding: utf-8 -*-
"""
Created on Mon Dec 10 12:17:35 2018

@author: kosmo
"""

#셔뱅 - linux 에서 돌릴때 필요한듯
#상대경로를 사용할 때, 파일익스플로러 맞춰놔야 함

#!/usr/bin/env python3
import sys

#input_file = sys.argv[1]
#output_file = sys.argv[2]

input_file = "./Data/01_data.csv"
output_file = "./Output/out01.csv"

with open(input_file, 'r', newline='') as filereader:
    with open(output_file, 'w', newline='') as filewriter:
        header = filereader.readline()
        header = header.strip()
        header_list = header.split(',')
        print(header_list)
        filewriter.write(','.join(map(str, header_list)) + '\n')
        for row in filereader:
            row = row.strip()
            row_list = row.split(',')
            print(row_list)
            filewriter.write(','.join(map(str,row_list))+'\n')