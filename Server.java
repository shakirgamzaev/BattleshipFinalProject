public class Server {

    int port;
    ServerSocket server=null;
    Socket client=null;
    Socket client2 = null;
    ExecutorService pool = null;
    Integer clientcount=0;
   
    //HashMap<Integer,Socket> clientsArray = new HashMap<>();
   
    public static void main(String[] args) throws IOException {
    // i will try to change server port num
        Server serverobj=new Server(7000);
        serverobj.startServer();
    }
   
   
    Server(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(2);
    }

   
   
   
   
   
   
   
    public void startServer() throws IOException {
       
        server=new ServerSocket(7000);
        System.out.println("Server Booted");
        while(true)
        {
            client=server.accept();
            System.out.println("Connection with Client 1 accepted");
            clientcount++;
            client2 = server.accept();
            System.out.println("connection with Client 2 accepted");
            //clientsArray.put(clientcount, client);
            ServerThread runnable= new ServerThread(client,client2,clientcount,this);
            pool.execute(runnable);
        }
       
    }

   
   
   
   
 
    /**************************************************************************************************/
    /***************************************************************************************************/
   
   
   
   
    private static class ServerThread implements Runnable {

    Server server=null;
    Socket client=null;
    BufferedReader cin;
    PrintStream cout;
    BufferedReader cin2;
    PrintStream cout2;
    //Scanner Keyboard=new Scanner(System.in);
    int id;
    String s;
    ScoresHistoryAndLoginInfo client1HistoryScores;
    ScoresHistoryAndLoginInfo client2HistoryScores;
    Login LoginDataBase;
   
    ServerThread(Socket client,Socket client2, int count ,Server server ) throws IOException {

    this.client=client;
    this.server=server;
    this.id=count;
   

    cin=new BufferedReader(new InputStreamReader(client.getInputStream()));
    cout=new PrintStream(client.getOutputStream());
    cin2=new BufferedReader(new InputStreamReader(client2.getInputStream()));
    cout2=new PrintStream(client2.getOutputStream());

    //Initiate ScoresHistory for client 1 and client 2, later we write win and losses to the files for permanent storage
    client1HistoryScores = new ScoresHistoryAndLoginInfo("C:/filesForData/ScoresHistoryClient1.txt");
    client2HistoryScores = new ScoresHistoryAndLoginInfo("C:/filesForData/ScoresHistoryClien2.txt");
    LoginDataBase = new Login("C:/filesForData/BattleShipsLogins.txt");
   
    }

       //In this section a Server will send an invitation to a client from another to start the game
   
   
   

    @Override
    public void run() {

    //receive Login and password from client1
   
   
   
   
   
   
   
    int x=1;
   
    int Client1Turn;
    int Client2Turn;

    int GridSize = GridSize();
   
    int ScoreClient1 = 0;
    int ScoreClient2 = 0;
   
    boolean Client1Won = false;
    boolean Client2Won = false;
    boolean Client1Lost = false;
    boolean Client2Lost = false;
   
    Character[][] BattleArenaGridClient1= GenerateMap(GridSize);
    Character[][] BattleArenaGridClient2= GenerateMap(GridSize);
   
    Client1Turn = TossCoin();
    if(Client1Turn == 1)
    {
    Client2Turn = 0;
    }
   
    else
    {
    Client2Turn = 1;
    Client1Turn = 0;
    }
   
   
   
   
    boolean Player1Exited = false;
    boolean Player2Exited = false;

    //1st info sent to client 1: connection num
    cout.println("1");
    //1st info sent to client 1: connection num
    cout2.println("2");
   
   
   

    try {
    //login and password from client1
   
    Boolean HasClient1LoggedIn = false;
    Boolean HasClient2LoggedIn = false;
    String Client1Login = cin.readLine();
    String Client1Password = cin.readLine();
    //login and password from client 2
    String Client2Login = cin2.readLine();
    String Client2Password = cin2.readLine();
    HasClient1LoggedIn = LoginByClient(Client1Login, Client1Password,1);
    HasClient2LoggedIn = LoginByClient(Client2Login, Client2Password,2);
    cout.println(HasClient1LoggedIn.toString());
    cout2.println(HasClient2LoggedIn.toString());
   
    while(HasClient1LoggedIn == false || HasClient2LoggedIn == false)
    {
   
    if(HasClient1LoggedIn == true && HasClient2LoggedIn == false)
    {
    cout.println(HasClient1LoggedIn);
    Client2Login = cin2.readLine();
    Client2Password = cin2.readLine();
    HasClient2LoggedIn = LoginByClient(Client2Login, Client2Password,2);
    cout2.println(HasClient2LoggedIn.toString());
    }
    else if(HasClient1LoggedIn == false && HasClient2LoggedIn == true)
    {
    cout2.println(HasClient2LoggedIn);
    Client1Login = cin.readLine();
    Client1Password = cin.readLine();
    HasClient1LoggedIn = LoginByClient(Client1Login, Client1Password,1);
    cout.println(HasClient1LoggedIn.toString());
    }
   
    /*cout.println(HasClient1LoggedIn.toString());
    cout2.println(HasClient2LoggedIn.toString());*/
    }
   
   
    //start of game after login
    if(HasClient1LoggedIn == true && HasClient2LoggedIn == true)
    {
   
    //1st piece of info received from client 1
        try {
        String Invitation = cin.readLine();
       
        if(Invitation.contains("invite"))
        {
        //2nd piece of info sent to Client2: request to play
        cout2.println("You are invited to play a battleship game, please type whether you want?; Type 'accept' to accept or 'reject' otherwise ");
        String AnswerFromClient2 = cin2.readLine();
       
       
        if(AnswerFromClient2.contains("accept"))
        {
        //2nd piece of info sent to Clien1 : accept or reject answer from Clien2
        cout.println(AnswerFromClient2);
       
        /*3rd piece of info sent to client 1*/
        cout.println("Client Number 1");

        /*3rd piece of info sent to client 2*/
        cout2.println("Client Number 2");


        int NumberOfShipsPerClient = GenerateNumberOfShips();


        /*4thpiece of info sent to client 1: NumberOfShipsPerClient*/////
        cout.println(String.valueOf(NumberOfShipsPerClient));
        ////////////////////////////////////

        /*4th piece of info sent to client 2: NumberOfShipsPerClient*/////
        cout2.println(String.valueOf(NumberOfShipsPerClient));
        /////////////////////////////////////////////////////////////////


        /*5th piece of info sent to Client1: GridSize*/
        cout.println(String.valueOf(GridSize));
        ////////////////////////////////////////////

        /*5th piece of info sent to Client2: GridSize*/
        cout2.println(String.valueOf(GridSize));
        ///////////////////////////////////////


        //this function corresponds with Client: placeshipsFunction
        try {
        FillUpGridWithClientCoordinates(cin, BattleArenaGridClient1,NumberOfShipsPerClient);
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        /**********************************/

        //this function corresponds with Client: placeshipsFunction
        try {
        FillUpGridWithClientCoordinates(cin2, BattleArenaGridClient2,NumberOfShipsPerClient);
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
       
        ShowMapOfClients(BattleArenaGridClient1,1);
        ShowMapOfClients(BattleArenaGridClient2,2);

        try{
        while(true)
        {


        // 1st piece  after filling up grid with coordinates sent to client 1: Clients1 turn
        cout.println(String.valueOf(Client1Turn));
        cout.flush();
        //////////////////////////////////////////
        // 1 piece of info after filling up grid with coordinates sent to client2: Clients2 turn
        cout2.println(String.valueOf(Client2Turn));
        cout2.flush();
        ///////////////////////////////////////////////////////


        if(Client1Turn == 1)
        {

        // 2nd piece of info after filling up grid with coordinates sent to client 1: Text where to hit a ship
        //cout.println("Type where you want to hit a ship");  
        //cout.flush();

        // 1st piece of info received from client 1 after filling up coordinates
        String HitPoint = cin.readLine(); //1st in from client  = 40,50 for example

        String HitPointCorrected = HitPoint.replaceAll("\\s+", "");


        if(HitPointCorrected.contains("bye"))
        {
        cout2.println("Your opponent exited the game, game over");
        cout2.flush();
        Player1Exited = true;
        x = 0;
        break;
        }



        else if((HitPointCorrected != null) && (HitPointCorrected.contains("bye")== false))
        {
        boolean Hit = CheckIfOpponentHitYou(HitPoint,BattleArenaGridClient2);
        if(Hit == true)
        {
        //2nd piece of info sent to Client2 after fillin up the grid
        cout2.println("Your Opponent hit you at point: ");
        cout2.flush();
        //3rd piece of info sent to client 2 after grid fill: Actual hitpoint as string
        cout2.println(HitPointCorrected);
        cout2.flush();
        ScoreClient1++;
        //2nd piece of info sent to client1 after grid fill up: Client1 current score
        /*cout2.println(String.valueOf(ScoreClient1));
        cout2.flush();
        */

        ShowMapOfClients(BattleArenaGridClient1,1);
        ShowMapOfClients(BattleArenaGridClient2,2);

        if(ScoreClient1>=NumberOfShipsPerClient)
        {
        //2nd piece of info sent to client1 after grid fill up: Client1 current score
        cout.println(String.valueOf(ScoreClient1));
        cout.flush();

        /**4th piece of info sent to client2 after grid fill up: Client2 lost the game*/
        cout2.println("true");
        cout2.flush();
        x=0;

        client1HistoryScores.WriteScoreToFileIfWon();
        client2HistoryScores.WriteScoreToFileIfLost();

        break;
        }

        else
        {
        //2nd piece of info sent to client1 after grid fill up: Client1 current score
        cout.println(String.valueOf(ScoreClient1));
        cout.flush();

        /**4th piece of info sent to client2 after grid fill up: Client2 lost the game*/
        cout2.println("false");
        cout2.flush();
        Client2Turn = 1;
        Client1Turn = 0;
        }



        }




        else
        {


        //2nd piece of info sent to Client2 after fillin up the grid
        cout2.println("Your opponent hit but missed your ship at point: ");
        cout2.flush();

        //3rd piece of info sent to client2 after grid fill up
        cout2.println(HitPointCorrected);
        cout2.flush();

        //4th piece of info sent to client2 : whether he lost the game or not
        cout2.println("false");
        cout2.flush();

        //2nd piece of info sent to client1
        cout.println(String.valueOf(ScoreClient1));
        cout.flush();

        Client2Turn = 1;
        Client1Turn = 0;
        }

        }



        }


        /**************************
        * **********************
        * *************
        * PLAYER 2 TURN
        */



        else
        {
        // 2nd piece of info after filling up grid with coordinates sent to client 1: Text where to hit a ship
        //cout.println("Type where you want to hit a ship");  
        //cout.flush();

        // 1st piece of info received from client 1 after filling up coordinates
        String HitPoint = cin2.readLine(); //1st in from client  = 40,50 for example

        String HitPointCorrected = HitPoint.replaceAll("\\s+", "");


        if(HitPointCorrected.contains("bye"))
        {
        cout.println("Your opponent exited the game, game over");
        cout.flush();
        Player1Exited = true;
        x = 0;
        break;
        }



        else if((HitPointCorrected != null) && (HitPointCorrected.contains("bye")== false))
        {
        boolean Hit = CheckIfOpponentHitYou(HitPoint,BattleArenaGridClient1);
        if(Hit == true)
        {
        //2nd piece of info sent to Client2 after fillin up the grid
        cout.println("Your Opponent hit you at point: ");
        cout.flush();
        //3rd piece of info sent to client 2 after grid fill: Actual hitpoint as string
        cout.println(HitPointCorrected);
        cout.flush();
        ScoreClient2++;
        //2nd piece of info sent to client1 after grid fill up: Client1 current score
        /*cout2.println(String.valueOf(ScoreClient1));
        cout2.flush();
        */

        ShowMapOfClients(BattleArenaGridClient1,1);
        ShowMapOfClients(BattleArenaGridClient2,2);

        if(ScoreClient2>=NumberOfShipsPerClient)
        {
        //2nd piece of info sent to client1 after grid fill up: Client1 current score
        cout2.println(String.valueOf(ScoreClient2));
        cout2.flush();

        /**4th piece of info sent to client2 after grid fill up: Client2 lost the game*/
        cout.println("true");
        cout.flush();

        client2HistoryScores.WriteScoreToFileIfWon();
        client1HistoryScores.WriteScoreToFileIfLost();
        x = 0;
        break;
        }

        else
        {
        //2nd piece of info sent to client1 after grid fill up: Client1 current score
        cout2.println(String.valueOf(ScoreClient2));
        cout2.flush();

        /**4th piece of info sent to client2 after grid fill up: Client2 lost the game*/
        cout.println("false");
        cout.flush();
        Client2Turn = 0;
        Client1Turn = 1;
        }



        }




        else
        {


        //2nd piece of info sent to Client1 after fillin up the grid
        cout.println("Your opponent hit but missed your ship at point: ");
        cout.flush();

        //3rd piece of info sent to client1 after grid fill up
        cout.println(HitPointCorrected);
        cout.flush();

        //4th piece of info sent to client1 : whether he lost the game or not
        cout.println("false");
        cout.flush();

        //2nd piece of info sent to client2
        cout2.println(String.valueOf(ScoreClient1));
        cout2.flush();


        Client2Turn = 0;
        Client1Turn = 1;
        }

        }


        }  // end of mainGame while loop
        }
        cin.close();
            client.close();
            cout.close();
            cin2.close();
            cout2.close();
            if(x==0) {
            System.out.println( "Server cleaning up." );
            System.exit(0);
            }



        }
        catch (IOException e1)
        {
        e1.printStackTrace();
        }


        } //end of outermost If
       
       
       
        else
        {
        System.out.println("Client rejected proposal, game terminated");
        cin.close();
        client.close();
        cout.close();
        cin2.close();
        cout2.close();
        x=0;
        if(x == 0)
        {
        System.out.println( "Server cleaning up." );
        System.exit(0);
        }
       
        }
       
       
       
        }
        }// The outermost Try end

        catch (IOException e)
        {

        }
    }
   
   
   
   
   
} catch (IOException e2) {
e2.printStackTrace();
}
   
   
   
   
   
   
    } // function Run() end bracket

   
   
   
    private Boolean LoginByClient(String ClientLogin,String ClientPassword,int ClientNum)
    {
    LoginDataBase.SetLoginAndPassword(ClientNum);
   
    String Login = LoginDataBase.GetLogin();
    Login.replace(" ", "");
   
    String Password = LoginDataBase.GetPassword();
    Password.replace(" ", "");
    if(ClientLogin.equals(Login) && ClientPassword.equals(Password))
    {
    return true;
    }
    else
    {
    return false;
    }
   
    }

private boolean CheckIfOpponentHitYou(String HitPoint,Character[][] BattleGrid)
    {
    String HitPointCorrected = HitPoint.replaceAll(" ", "");
String[] HitPointSep = HitPointCorrected.split(",");

if(BattleGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] == 'S')
{
BattleGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] = 'X';
return true;
}

else
{
BattleGrid[Integer.valueOf(HitPointSep[0])][Integer.valueOf(HitPointSep[1])] = 'M';
return false;
}

}



private void FillUpGridWithClientCoordinates(BufferedReader cin,Character[][] BattleGrid,int NumOfShips) throws IOException
    {
    for(int i = 0;i<NumOfShips;i++)
    {
    String CoordinateFromClient = cin.readLine();
    String CorrectedCoordinates = CoordinateFromClient.replaceAll("\\s","");
    String[] CoordinatesSplit = CoordinateFromClient.split(",");
    String C1 = CoordinatesSplit[0];
    String C2 = CoordinatesSplit[1];
    BattleGrid[Integer.parseInt(C1)][Integer.parseInt(C2)] = 'S';
    }
   
   
    }
   


   
    private void ShowMapOfClients(Character[][] BattleArenaGrid,int clientNum)
    {
    System.out.println("client number : " + clientNum);
    System.out.print("[");
    for(int i = 0;i<BattleArenaGrid.length;i++)
    {
    for(int j = 0;j<BattleArenaGrid[i].length;j++)
    {
    System.out.print(BattleArenaGrid[i][j] + ",");
    }
    System.out.println();
    }
    System.out.println("]");
   
    }
   
   
   
   
   
   
private int TossCoin()
{
int min = 0;
   int max = 1;  
     
   
   int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
   return random_int;
}





private int GenerateNumberOfShips()
{
//change this later to 5 and 10
int min = 2;
   int max = 4;  
     
   
   int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
   return random_int;
   
}


private int GridSize()
{
int min = 8;
    int max = 24;  
     //Generate random int value from 8 to 24
   
    int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
    return random_int;
}



private Character[][] GenerateMap(int SizeOfGridToGenerate)
{
   
    Character[][] BattlefieldMap = new Character[SizeOfGridToGenerate][SizeOfGridToGenerate];
    for(int i = 0;i<BattlefieldMap.length;i++)
    {
    for(int j = 0;j<BattlefieldMap[i].length;j++)
    {
    BattlefieldMap[i][j] = ' ';
    }
    }
    return BattlefieldMap;

}
    }

   
}