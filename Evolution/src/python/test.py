import random
import Locator
from os.path import isfile
from os import getenv
import sys

humanID = sys.argv[1]
save = sys.argv[2:]
save = str(save).replace("[","").replace("]","").replace(",","").replace("'","")
pathToFile = getenv("APPDATA")+"\\Evolution\\saves\\"+save+"\\Entities\\"+humanID+".EvoDNA"

def fileChange(DNA):
    DNAFile = open(pathToFile,"a")
    DNAFile.write(DNA)
    DNAFile.close()
    
def genDna():
    for i in range(0,250000):
        DNA=str(random.randint(1,9))
        fileChange(DNA)
        
def fileMake():
    newfile = open(pathToFile,"w")
    newfile.close()
    
def main():
    if isfile(pathToFile) == True:
        DNA = open(pathToFile,"r")
        DNARead = DNA.read()
        if DNARead == "":
            DNA.close()
            genDna()
    else:
        fileMake()
        genDna()
main()
