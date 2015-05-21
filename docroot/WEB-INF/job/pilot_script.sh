#!/bin/sh 
#
# SemanticSearch-portlet - pilot script
#
# The following script does:
#   o The execution start/end dates
#   o Listing of the worker node' /Users/ccarrubba directory
#   o Listing of the worker node' /Users/ccarrubba/Documents/hostname/hostname-portlet current directory
#   o Shows the template input file
#   o Simulates the creation of an output file  

# Get the hostname
HOSTNAME=MacBook-Pro-di-Carla.local

# In order to avoid concurrent accesses to files, the 
# portlet uses filename prefixes like
# <timestamp>_<username>_filename
# for this reason the file must be located before to use it
INFILE=

echo "--------------------------------------------------"
echo "SemanticSearch-portlet job landed on: '"MacBook-Pro-di-Carla.local"'"
echo "--------------------------------------------------"
echo "Job execution starts on: '"Gio 28 Feb 2013 12:12:18 CET"'"

echo "---[WN HOME directory]----------------------------"
ls -l /Users/ccarrubba

echo "---[WN Working directory]-------------------------"
ls -l /Users/ccarrubba/Documents/hostname/hostname-portlet

echo "---[Your message]---------------------------------"
cat 
echo

#
# Following statement simulates a produced job file
#
OUTFILE=job_output.txt
echo "--------------------------------------------------" > 
echo "hostname job landed on: '"MacBook-Pro-di-Carla.local"'" >> 
echo "infile:  '""'"
echo "outfile: '""'"
echo "--------------------------------------------------" >> 
cat  >> 

#
# At the end of the script file it's a good practice to 
# collect all generated job files into a single tar.gz file
# the generated archive may include the input files as well
#
tar cvfz SemanticSearch_portlet-Files.tar.gz  

echo "Job execution ends on: '"Gio 28 Feb 2013 12:12:18 CET"'"
