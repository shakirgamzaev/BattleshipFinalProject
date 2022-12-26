public class ScoresHistoryAndLoginInfo {

    private int NumberOfWins;
    private int NumberOfLosses;
    private int GameNumber;
    
    private String ClientfileName;
    private File Clientfile;
    private FileWriter fOutStream;
    private BufferedWriter outToFileStream;
    
    private Scanner FileReader;
    
    public ScoresHistoryAndLoginInfo(String ClientFileName)
    {
    this.ClientfileName = ClientFileName;
    this.Clientfile = new File(ClientfileName);
    //NumberOfWins = 0;
    //NumberOfLosses = 0;
    
    try
    {
    FileReader = new Scanner(Clientfile);
    fOutStream = new FileWriter(Clientfile,true);
    outToFileStream = new BufferedWriter(fOutStream);
    }
    
    catch (IOException e) {
    e.printStackTrace();
    }
    }
    
    
    public void WriteScoreToFileIfWon() throws FileNotFoundException
    {
    GetInfoFromFile();
    
    
    try
    {
    outToFileStream.write("Game: "+(GameNumber+1)+ "\n");
    outToFileStream.flush();
    outToFileStream.write("Number of Wins: " + (NumberOfWins + 1) + "\n");
    outToFileStream.flush();
    outToFileStream.write("Number Of Losses: " + NumberOfLosses + "\n");
    outToFileStream.flush();
    outToFileStream.close();
    FileReader.close();
    //outToFileStream.write("\n");
    }
    catch (IOException e) {
    e.printStackTrace();
    }
    
    
    }
    
    public void WriteScoreToFileIfLost() throws FileNotFoundException
    {
    
    GetInfoFromFile();
    
    try
    {
    outToFileStream.write("Game: "+(GameNumber + 1) +"\n");
    outToFileStream.flush();
    outToFileStream.write("Number of Wins: " + (NumberOfWins) + "\n");
    outToFileStream.flush();
    outToFileStream.write("Number Of Losses: " +(NumberOfLosses +1)+ "\n");
    outToFileStream.flush();
    
    outToFileStream.close();
    FileReader.close();
    }
    catch (IOException e) {
    e.printStackTrace();
    }
    
    }
    
    
    
    public void GetInfoFromFile()
    {
    int i=1;
    boolean BeginOfSection = true;
    
    
    while(FileReader.hasNextLine())
    {
    if(BeginOfSection == true)
    {
    String NumOfGame = FileReader.nextLine();
    String[] Split = NumOfGame.split("\\s+");
    int gameNumber = Integer.valueOf(Split[1]);
    setGameNumber(gameNumber);
    BeginOfSection = false;
    i++;
    
    }
    
    else if((i%2 == 0 && BeginOfSection == false && i%3 !=0 ) ||(i%2!=0 && BeginOfSection == false && i%3 != 0))
    {
    String WinsString = FileReader.nextLine();
    String[] Separated = WinsString.split("\\s+");
    int MaxWins = Integer.valueOf(Separated[3]);
    setNumberOfWins(MaxWins);
    i++;
    }
    
    
    else if(i%3 == 0  && BeginOfSection == false)
    {
    String LoseString = FileReader.nextLine();
    String[] Separated = LoseString.split("\\s+");
    int MaxLoses = Integer.valueOf(Separated[3]);
    BeginOfSection = true;
    setNumberOfLosses(MaxLoses);
    i++;
    }
    
    }
    
    
    }
    
    
    
    
    
    
    public void setGameNumber(int GameNumber)
    {
    GameNumber++;
    }
    
    
    public int GetGameNumber()
    {
    return this.GameNumber;
    }
    
    
    
    public int getNumberOfWins() {
    return NumberOfWins;
    }
    
    
    
    public void setNumberOfWins(int numberOfWins) {
    NumberOfWins = numberOfWins;
    }
    
    
    
    public int getNumberOfLosses() {
    return NumberOfLosses;
    }
    
    
    
    public void setNumberOfLosses(int numberOfLosses) {
    NumberOfLosses = numberOfLosses;
    }
    
    
    
    }