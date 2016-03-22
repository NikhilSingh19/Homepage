import statistics
import pickle

def loopThrough():
        print("Enter the number of the option you wish to choose")
        print("1-Search by First Name")
        print("2-Search by Last Name")
        choice=input("What is your choice? ")
        search=""
        player=[]
        final=[]
        try:
                int(choice)
        except Exception:
                print("Choice is not Valid")
                return
        if int(choice)==1:
            search=input("What is the first name of the player? ")
            for line in processedData:
                if line[9].lower()==search.lower():
                    player.append(line)
        if int(choice)==2:
            search=input("What is the last name of the player? ")
            for line in processedData:
                if line[10].lower()==search.lower():
                    player.append(line)
        if int(choice)==0:
            return "0"
        elif len(player)>1:
            print("There are more than one players with that name")
            y=0
            for line in player:
                print(y,line[9],line[10])
                y=y+1
            number=input("What is the number of the player you wish to find? ")
            number=int(number)
            final=player[number]
        else:
            final=player[0]
        return final

def findData(player):
    x=0
    while x==0:
        print("Enter the selection you wish to view or 30 to exit")
        print("0-Number")
        print("1-Date of Birth")
        print("2-Birth City")
        print("3-Country")
        print("4-Height")
        print("5-Weight")
        print("6-Shooting Hand")
        print("7-Age")
        print("9-First Name")
        print("10-Last Name")
        print("11-Team Name")
        print("14-Position")
        print("15-Games Played")
        print("16-Goals")
        print("17-Assists")
        print("18-Points")
        selection=input("What is your selection? ")
        try:
                int(selection)
        except Exception:
                print("Choice is not valid")
                return
        if selection == 30:
            break
        else:
            print()
            print(player[int(selection)])
            print()

def findData2(player1,player2):
    x=0
    while x==0:
        print("Enter the selection you wish to view or 30 to exit")
        print("0-Number")
        print("1-Date of Birth")
        print("2-Birth City")
        print("3-Country")
        print("4-Height")
        print("5-Weight")
        print("6-Shooting Hand")
        print("7-Age")
        print("9-First Name")
        print("10-Last Name")
        print("11-Team Name")
        print("14-Position")
        print("15-Games Played")
        print("16-Goals")
        print("17-Assists")
        print("18-Points")
        selection=input("What is your selection? ")
        try:
                int(selection)
        except Exception:
                print("Choice is not valid")
                return
        if selection == 30:
            break
        else:
            print()
            print(player1[9],player1[10],player1[int(selection)])
            print(player2[9],player2[10],player2[int(selection)])
            print()

def OnePlayer():
    x=0
    while x==0:               
        try:
            player=loopThrough()
            print(player[0],player[9],player[10],player[11])
            findData(player)
            break
        except Exception:
            print("")

def TwoPlayer():
    x=0
    while x==0:
        try:
            print("Pick the first Player")
            print()
            player1=loopThrough()
            print("Pick the second Player")
            print()
            player2=loopThrough()
            print("Player 1:",player1[0],player1[9],player1[10],player1[11])
            print("Player 2:",player2[0],player2[9],player2[10],player2[11])
            findData2(player1,player2)
            break
        except Exception:
            print("Error")
x=0
while x==0:
    fileName=input("What is the name of the file you wish to read from? ")
    try:
        inputFile=open(fileName,"r")
        break
    except Exception:
        print("File Not Found")
rawData=[]
x=0
for line in inputFile:
    if x==0:
        x=1
    else:
        rawData.append(line)
processedData=[]
for line in rawData:
    processedData.append(line.split("\t"))
outputFile=open("output.pkl","wb")
pickle.dump(processedData,outputFile)
outputFile.close()

class Run:
        def __init__ (self):
                x=0
        def main():
                x=0
                while x==0:
                    print("Enter your selection or 0 to quit")
                    print("1-Analyze data from 1 player")
                    print("2-Analyze data from 2 players")
                    selection=input("What is your selection")
                    if int(selection)==0:
                        break
                    elif int(selection)==1:
                        OnePlayer()
                    else:
                        TwoPlayer()

Run.main()
