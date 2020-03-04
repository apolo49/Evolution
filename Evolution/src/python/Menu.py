from lib.PIL import Image as OpenImage,ImageTk
from tkinter import Tk, BooleanVar,Label,Entry,Button,Checkbutton,CENTER,Listbox,END,RIGHT
from hashlib import sha3_256
from ctypes import *
import string
import os
import Locator
import random
import sys

window = Tk()
#pathToImage = Locator.main("res\\textures\\Small Logo.png") #Find the small version of the logo
#img = ImageTk.PhotoImage(OpenImage.open(pathToImage).resize((256,82),OpenImage.ANTIALIAS)) #Resize the small logo
QuitFlagPath = os.getenv('APPDATA')+"\\Evolution\\flags and misc\\QuitFlag.flg"
ChosenWorldPath = os.getenv('APPDATA')+"\\Evolution\\flags and misc\\chosenWorldFlag.flg"
SeedInfoPath = os.getenv('APPDATA')+"\\Evolution\\flags and misc\\SeedInfoFlag.flg"
NewWorldPath = os.getenv('APPDATA')+"\\Evolution\\flags and misc\\NewWorldflag.flg"


try:
    quitflag = open(QuitFlagPath,'w')
    quitflag.write("")
    quitflag.close()
    chosenWorldflag = open(ChosenWorldPath,'w')
    chosenWorldflag.write("")
    chosenWorldflag.close()
    SeedInfoflag = open(SeedInfoPath,'w')
    SeedInfoflag.write("")
    SeedInfoflag.close()
    NewWorldflag = open(NewWorldPath,'w')
    NewWorldflag.write("")
    NewWorldflag.close()
    CurrentVersionNumber = open(os.getenv('APPDATA')+"\\Evolution\\flags and misc\\VersionNumber.dat").read()
    files = []
    for (dirpath, dirnames, filenames) in os.walk(os.getenv('APPDATA')+"\\Evolution\\saves"):
        files.extend(dirnames)
        break
    
except:
    if not os._exists(os.getenv("APPDATA")+"\\Evolution"):
        os.mkdir(os.getenv("APPDATA")+"\\Evolution")
        os.mkdir(os.getenv("APPDATA")+"\\Evolution\\flags and misc")
    elif not os._exists(os.getenv("APPDATA")+"\\Evolution\\flags and misc"):
        os.mkdir(os.getenv("APPDATA")+"\\Evolution\\flags and misc")
    quitflag = open(QuitFlagPath,'w')
    quitflag.write("")
    quitflag.close()
    chosenWorldflag = open(ChosenWorldPath,'w')
    chosenWorldflag.write("")
    chosenWorldflag.close()
    SeedInfoflag = open(SeedInfoPath,'w')
    SeedInfoflag.write("")
    SeedInfoflag.close()
    NewWorldflag = open(NewWorldPath,'w')
    NewWorldflag.write("")
    NewWorldflag.close()
    
finally:
    def Remove():
        for label in window.grid_slaves():
            label.grid_forget()
        for label in window.pack_slaves():
            label.pack_forget()
        for label in window.place_slaves():
            label.place_forget()

    def QuitGame():
        quitflag = open(QuitFlagPath,'w')
        quitflag.write("1")
        quitflag.close()
        quit()

    def Options():
        window.title("Options")
        Remove()
        Label(window,text="WIP").grid(row=0,column=0)
        Button(window,command=lambda:MainMenu()).grid(row=1,column=0)

    def ChooseWorld():
        window.title("Choose World")
        Remove()
        f= []
        for i in files:
            for (dirpath, dirnames, filenames) in os.walk(os.getenv('APPDATA')+"\\Evolution\\saves\\"+i):
                try:
                    if "WorldData.dat" in filenames:
                        lines = open(os.getenv('APPDATA')+"\\Evolution\\saves\\"+i+"\\WorldData.dat","r").readlines()
                        for j in lines:
                            if "Version: " in j:
                                VersionNo = j.replace("Version: ","")
                                VersionNo = VersionNo.replace("\n","")
                                if VersionNo != CurrentVersionNumber:
                                    VersionNo = VersionNo+" -UNSAFE - Use At Own Discretion."
                            if "World Name: " in j:
                                WorldName = j.replace("World Name: ","")
                                WorldName = WorldName.replace("\n","")
                                f.append(WorldName+"                 "+VersionNo)
                                break
                    else:
                        f.append(i+"                 Legacy Version -unable to retrieve version number. UNSAFE")
                        break
                except:
                    f.append(i+"                 Legacy Version or Error -unable to retrieve version number. UNSAFE")
                    break
                break
        WorldList = Listbox(window)
        WorldList.place(relwidth=1,relheight=0.7,relx=0,rely=0.)
        for i in f:
            WorldList.insert(END,i)
        CreateButton = Button(window,text="Create World",command=lambda:CreateWorld()).place(relwidth=0.4,relheight=0.1,relx=0.6,rely=0.7)
        PlayButton = Button(window,text="Load World",command=lambda:play(WorldList.get(WorldList.curselection()))).place(relwidth=0.4,relheight=0.3,relx=0.1,rely=0.7)
        Button(window,text="Return to main menu",command=lambda:MainMenu()).place(relwidth=0.4,relheight=0.1,relx=0.6,rely=0.9)

    def CreateWorld():
        window.title("Create World")
        Remove()
        WorldName = Entry(window)
        WorldName.insert(0,"New World")
        WorldName.bind("<FocusIn>", lambda args: WorldName.delete('0', 'end'))
        Seed = Entry(window)
        WorldName.place(relwidth=0.8,relheight=0.3,relx=0.1,rely=0.1)
        PlayButton = Button(window,text="Create World",command=lambda:makeWorld(WorldName.get(),"")).place(relwidth=0.25,relheight=0.2,relx=0.65,rely=0.8)
        BackButton = Button(window,text="Back",command=lambda:ChooseWorld()).place(relwidth=0.25,relheight=0.2,relx=0.1,rely=0.8)
        AdvButton = Button(window,text="Advanced Mode",command=lambda:AdvMode()).place(relwidth=0.5,relheight=0.3,relx=0.25,rely=0.45)

    def AdvMode():
        window.title("Create World")
        Remove()
        WorldName = Entry(window)
        WorldName.insert(0,"New World")
        WorldName.bind("<FocusIn>", lambda args: WorldName.delete('0', 'end'))
        Seed = Entry(window)
        Seed.insert(0,"World Seed")
        Seed.bind("<FocusIn>", lambda args: Seed.delete('0', 'end'))
        WorldName.place(relwidth=0.8,relheight=0.3,relx=0.1,rely=0.1)
        Seed.place(relwidth=0.8,relheight=0.3,relx=0.1,rely=0.45)
        NormalMode = Button(window,text="Normal Mode",command=lambda:CreateWorld()).place(relwidth=0.25,relheight=0.2,relx=0.375,rely=0.8)
        PlayButton = Button(window,text="Create World",command=lambda:makeWorld(WorldName.get(),Seed.get())).place(relwidth=0.25,relheight=0.2,relx=0.65,rely=0.8)
        BackButton = Button(window,text="Back",command=lambda:ChooseWorld()).place(relwidth=0.25,relheight=0.2,relx=0.1,rely=0.8)

    def makeWorld(world,seed):
        chosenWorldflag = open(ChosenWorldPath,'w')
        if world != None or world == "New World":
            chosenWorldflag.write(world)
        else:
            chosenWorldflag.write("")
        chosenWorldflag.close()
        quitflag = open(QuitFlagPath,'w')
        quitflag.write("0")
        quitflag.close()
        SeedInfoflag = open(SeedInfoPath,'w')
        if seed == "World Seed" or seed == None or seed == "":
            seed = random.randint(0,sys.maxsize)
        elif seed.upper().isupper():
            seed = round((int(sha3_256(seed.encode('utf-8')).hexdigest(),16)/13),0).as_integer_ratio()[0]*round((int(sha3_256(seed.encode('utf-8')).hexdigest(),16)/13),0).as_integer_ratio()[1]
        seed=str(seed)
        SeedInfoflag.write(seed)
        SeedInfoflag.close()
        NewWorldflag = open(NewWorldPath,'w')
        NewWorldflag.write("1")
        NewWorldflag.close()
        
        exit()

    def play(world):
        try:
            for i in files:
                for (dirpath, dirnames, filenames) in os.walk(os.getenv('APPDATA')+"\\Evolution\\saves\\"+i):
                    if "WorldData.dat" in filenames:
                        lines = open(os.getenv('APPDATA')+"\\Evolution\\saves\\"+i+"\\WorldData.dat","r").readlines()
                        for j in lines:
                            if "Path Name: " in j:
                                j = j.replace("Path Name: ","")
                                j = j.replace("\n","")
                                print(world)
                                if j in world:
                                    world=j
                                    break
            chosenWorldflag = open(ChosenWorldPath,'w')
            chosenWorldflag.write(world)
            chosenWorldflag.close()
            quitflag = open(QuitFlagPath,'w')
            quitflag.write("0")
            quitflag.close()
            SeedInfoflag = open(SeedInfoPath,'w')
            SeedGrab = open(os.getenv("APPDATA")+"\\Evolution\\saves\\"+world+"\\maps\\seed.flg","r")
            SeedInfoflag.write(SeedGrab.read())
            SeedGrab.close()
            SeedInfoflag.close()
            NewWorldflag = open(NewWorldPath,'w')
            NewWorldflag.write("0")
            NewWorldflag.close()
        except:
            ErrorScreen()
        exit()
        
    def MainMenu():
        Remove()
        window.title("Main Menu")
        play = Button(window,text="Play!",command=lambda:ChooseWorld())
        play.place(relwidth=0.8,relheight=0.1,relx=0.5,rely=0.2,anchor=CENTER)
        options = Button(window,text="Options",command=lambda:Options())
        options.place(relwidth=0.8,relheight=0.1,relx=0.5,rely=0.5,anchor=CENTER)
        quitButton = Button(window,text="Quit Game",command=lambda:QuitGame())
        quitButton.place(relwidth=0.8,relheight=0.1,relx=0.5,rely=0.8,anchor=CENTER)
        VersionNo = Label(window,text = "Current Version: "+CurrentVersionNumber).place(width = 120,height = 20,x = 0.0,rely=0.925)

    def ErrorScreen():
        Remove()
        button = Button(text = "Return to Menu",command=lambda:MainMenu())
        button.place(relwidth=0.8,relheight=0.1,relx=0.5,rely=0.8,anchor=CENTER)
        label = Label(text = "Error has occured", font= ("Helvetica", 72)).place(relwidth=0.8,relheight=0.1,relx=0.5,rely=0.2,anchor=CENTER)

    MainMenu()

    window.mainloop()
