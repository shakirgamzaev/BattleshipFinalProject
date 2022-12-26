public class Client {
   
    public static void main(String args[]) throws Exception
{
Socket sk=new Socket("127.0.0.1",7000);
//BufferedReader In = new BufferedReader(new InputStreamReader(sk.getInputStream()));
BufferedReader In = new BufferedReader(new InputStreamReader(sk.getInputStream()));
PrintStream Out = new PrintStream(sk.getOutputStream());
BufferedReader keyBoard=new BufferedReader(new InputStreamReader(System.in));
//ArrayList<String> MyShipsPosition = new ArrayList<String>();
Character[][] MyGrid;

//login section


String YourTurn;
String ClientNumber;
String NumOfShips;
String SizeOfGrid;

//1st info from server: whether i connected first or not
String ConnectedPosition;
ConnectedPosition = In.readLine();

System.out.println("Please Enter your login below");
String Login = keyBoard.readLine();
Out.println(Login);
System.out.println("Please enter your password below");
String Password = keyBoard.readLine();
Out.println(Password);

String LoginResponse = In.readLine();
//String OpponentLogin = In.readLine();

if(LoginResponse.contains("true"))
{
System.out.println("successfully loggedin");
}

else
{
while(true)
{
System.out.println("Please Enter your login below");
Login = keyBoard.readLine();
Out.println(Login);

System.out.println("Please enter your password below");
Password = keyBoard.readLine();
Out.println(Password);

LoginResponse = In.readLine();
if(LoginResponse.contains("true"))
{
System.out.println("Successfully logged in");
break;
}
}

}




if(Integer.valueOf(ConnectedPosition) == 1)
{
System.out.println("Type whether you want to invite an opponent to play, 'invite' if yes, no otherwise");

//1st info sent to Server : invitation to another player to join the game
String invite = keyBoard.readLine();
if(invite.contains("invite"))
{
//1st out to Server
Out.println("invite");
//2nd info from server: Accept or reject from opponent
String Answer = In.readLine();
if(Answer.contains("accept"))
{
System.out.println("your request to play was accepted from an opponent, game begins");
//execute while loop
/* 3rd info from Server */
ClientNumber = In.readLine();//this is the first piece of info = Client number 1
System.out.println(ClientNumber);
//////////////////////////////////

/* 4th piece of info from server: Numof Ships to place on grid*/
NumOfShips = In.readLine();
System.out.println("Number of Ships: " + NumOfShips);
/////////////////////////////////


/*5th piece of info from server: Size of grid e.g 10x10*/
SizeOfGrid = In.readLine();
System.out.println("GridSize: " +SizeOfGrid);
/////////////////////////////////////////////


MyGrid = new Character[Integer.parseInt(SizeOfGrid)][Integer.parseInt(SizeOfGrid)];
GenerateMapWithSpaceChars(MyGrid);


// this step involves a client placing a ship into cells and sending info to server method of fillupgrid()

PlacementOfShips(Integer.parseInt(NumOfShips),keyBoard,MyGrid,Out,Integer.parseInt(SizeOfGrid));

//boolean YouWon = false;
int MyScore = 0;

while (true)
{

/** 1st piece of info received from serverafter placement of ships from server*/
YourTurn =  In.readLine(); // = "1" in string The turn of this client

//prints to console client's turn
System.out.println(YourTurn);

String yourTurnCorrected = YourTurn.replaceAll(" ", "");


if(Integer.parseInt(yourTurnCorrected) == 1)
{
System.out.println("Your Turn");
System.out.println("Type where you want to hit an opponent");

String s;
s = keyBoard.readLine();    // 1st piece of info out to server

/////////////////////////////////////


if ( s.contains("bye")  )
{
System.out.println("Connection ended by client");
Out.println(s);
Out.flush();
break;
}

else
{
// 1st piece of info sent to server
Out.println(s);
Out.flush();

/**2nd piece of info received from server: my current score*/
String MyScoreText = In.readLine();
MyScore = Integer.valueOf(MyScoreText);

if(MyScore == Integer.valueOf(NumOfShips))
{
System.out.println("Congratualtions you won the game !!!!!!!!!!");
break;
}


else
{
System.out.println("your current score is: " + MyScore);

}
}
}

/*************************
*
*
*
*
*
*/



else if(Integer.parseInt(yourTurnCorrected) == 0)
{


System.out.println("Your opponent's turn");
/** 2nd piece of info received from Server: Text*/
String TextReceived = In.readLine();
/** 3rd piece of info received from Server: Hitpoint*/
String HitPoint = In.readLine();
if(HitPoint == null)
{
System.out.println("The String returned from server is null");
}



else
{

System.out.println(TextReceived + HitPoint);  

/**4th piece of info received from server: Did i lose the game or not*/
String LostGame = In.readLine();

if(LostGame.contains("false"))
{
String HitPointCorrected  = HitPoint.replaceAll("\\s","");
String[] HitPointSep = HitPointCorrected.split(",");

if(MyGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] == 'S')
{
MyGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] = 'X';
ShowMyGrid(MyGrid);
}

else
{
MyGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] = 'M';
ShowMyGrid(MyGrid);
}
String s;
s = keyBoard.readLine();

if ( s.contains("exit") )
{
System.out.println("Connection ended by client");
Out.println(s);
Out.flush();
break;
}
else
{
continue;
}
}


else if(LostGame.contains("true") )
{
System.out.println("Oh No You lost the game, maybe next time you will have more luck.");
break;
}

else
{
System.out.println(LostGame);
}


}
}


}
sk.close();
In.close();
Out.close();
keyBoard.close();

}


}


else
{
Out.println("cancel the game");
sk.close();
In.close();
Out.close();
keyBoard.close();
}

}




// Client 2 that accepts




else
{
//2nd info from server : request to play
String InviteToAccept = In.readLine();
System.out.println(InviteToAccept);
String answer = keyBoard.readLine();
if(answer.contains("accept"))
{
Out.println("accept");


ClientNumber = In.readLine();//this is the first piece of info = Client number 1
System.out.println(ClientNumber);
//////////////////////////////////

/* 4th piece of info from server: Numof Ships to place on grid*/
NumOfShips = In.readLine();
System.out.println("Number of Ships: " + NumOfShips);
/////////////////////////////////


/*5th piece of info from server: Size of grid e.g 10x10*/
SizeOfGrid = In.readLine();
System.out.println("GridSize: " +SizeOfGrid);
/////////////////////////////////////////////


MyGrid = new Character[Integer.parseInt(SizeOfGrid)][Integer.parseInt(SizeOfGrid)];
GenerateMapWithSpaceChars(MyGrid);


// this step involves a client placing a ship into cells and sending info to server method of fillupgrid()

PlacementOfShips(Integer.parseInt(NumOfShips),keyBoard,MyGrid,Out,Integer.parseInt(SizeOfGrid));

//boolean YouWon = false;
int MyScore = 0;

while (true)
{

/** 1st piece of info received from serverafter placement of ships from server*/
YourTurn =  In.readLine(); // = "1" in string The turn of this client

//prints to console client's turn
System.out.println(YourTurn);

String yourTurnCorrected = YourTurn.replaceAll(" ", "");


if(Integer.parseInt(yourTurnCorrected) == 1)
{
System.out.println("Your Turn");
System.out.println("Type where you want to hit an opponent");

String s;
s = keyBoard.readLine();    // 1st piece of info out to server

/////////////////////////////////////


if ( s.contains("bye")  )
{
System.out.println("Connection ended by client");
Out.println(s);
Out.flush();
break;
}

else
{
// 1st piece of info sent to server
Out.println(s);
Out.flush();

/**2nd piece of info received from server: my current score*/
String MyScoreText = In.readLine();
MyScore = Integer.valueOf(MyScoreText);

if(MyScore == Integer.valueOf(NumOfShips))
{
System.out.println("Congratualtions you won the game !!!!!!!!!!");
break;
}


else
{
System.out.println("your current score is: " + MyScore);

}
}
}

/*************************
*
*
*
*
*
*/



else if(Integer.parseInt(yourTurnCorrected) == 0)
{


System.out.println("Your opponent's turn");
/** 2nd piece of info received from Server: Text*/
String TextReceived = In.readLine();
/** 3rd piece of info received from Server: Hitpoint*/
String HitPoint = In.readLine();
if(HitPoint == null)
{
System.out.println("The String returned from server is null");
}



else
{

System.out.println(TextReceived + HitPoint);  

/**4th piece of info received from server: Did i lose the game or not*/
String LostGame = In.readLine();

if(LostGame.contains("false"))
{
String HitPointCorrected  = HitPoint.replaceAll("\\s","");
String[] HitPointSep = HitPointCorrected.split(",");

if(MyGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] == 'S')
{
MyGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] = 'X';
ShowMyGrid(MyGrid);
}

else
{
MyGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] = 'M';
ShowMyGrid(MyGrid);
}
String s;
s = keyBoard.readLine();

if ( s.contains("exit") )
{
System.out.println("Connection ended by client");
Out.println(s);
Out.flush();
break;
}
else
{
continue;
}
}


else if(LostGame.contains("true") )
{
System.out.println("Oh No You lost the game, maybe next time you will have more luck.");
break;
}

else
{
System.out.println(LostGame);
}


}
}


}
sk.close();
In.close();
Out.close();
keyBoard.close();




}
else
{
Out.println("bye");
}

}


}

   
   
   
    // this section below works fine, coordinates sent to server successfully
   
    private static void ShowMyGrid(Character[][] ShipsGrid)
    {
    System.out.print("[");
    for(int i = 0;i<ShipsGrid.length;i++)
    {
    for(int j = 0;j<ShipsGrid[i].length;j++)
    {
               System.out.print(ShipsGrid[i][j]+"," );
    }
    System.out.println();
    }
   
    }
   
   
   
    private static void GenerateMapWithSpaceChars(Character[][] ShipsGrid)
    {
    for(int i = 0;i<ShipsGrid.length;i++)
    {
    for(int j = 0;j<ShipsGrid[i].length;j++)
    {
    ShipsGrid[i][j] = ' ';
    }
    }
    }
   
   
   
   
   
private static void PlacementOfShips(int NumOfShips,BufferedReader keyBoard,Character[][] ShipsGrid,PrintStream Out,int GridSize) throws IOException
{
for(int i = 0;i<NumOfShips;i++)
{
System.out.println("Type where you want to place your ship number: " + (i+1));
//boolean toBreak = false;
String coordinateToSend = "";

int cX;
int cY;


String coordinate = keyBoard.readLine();
String CorrectedCoordinates = coordinate.replaceAll("\\s","");


String[] coordinates = coordinate.split(",");
int c1 = Integer.parseInt(coordinates[0]);
int c2 = Integer.parseInt(coordinates[1]);


coordinateToSend = CorrectedCoordinates;
cX = c1;
cY = c2;


   ShipsGrid[cX][cY] = 'S';
   Out.println(coordinateToSend);
   Out.flush();
}
}
   
}