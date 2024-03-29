# -*- coding: utf-8 -*-
"""
Created on Wed Dec 12 14:39:23 2018

@author: kosmo
"""

#!/usr/bin/env python3

import sys

input_key = None
input_value = 0
output_key = None
output_value = 0

for line in sys.stdin:
    line = line.strip()
    columns = line.split("\t")
    input_key = columns[0]
    input_value = int(columns[1])
    
    if output_key == input_key:
        output_value += input_value
    else:
        if output_key:
            print('%s\t%s' % (output_key,  output_value))
        
        output_value = input_value
        output_key = input_key
        
if(output_key == input_value):
    print('%s\t%s' % (input_key, output_value))