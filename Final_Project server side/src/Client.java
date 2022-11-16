import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
    public  static Mangment_Accounts accounts=new Mangment_Accounts();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    boolean flag=true;
    private String help = "opertaions key words\n 1) get clients list-get all clients connected right now";
    private Scanner sc = new Scanner(System.in);


    public Mangment_Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Mangment_Accounts accounts) {
        this.accounts = accounts;
    }



    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            //send to the server
            //this.accounts=new Mangment_Accounts();
           // this.user=accounts.getAccounts();
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //to get from server and read it
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        } catch (IOException e) {
            e.printStackTrace();
            closeStream(socket, bufferedReader, bufferedWriter);
        }
    }

    public void send() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

             while  (socket.isConnected()&&flag) {
                System.out.println("chat menu:\n 1) help\n 2) get users online \n 3) send group message \n 4) send private message \n 5) quit");
                int userChoice = sc.nextInt();
                switch   (userChoice) {
                    case 1:
                        System.out.println(help);
                        break;
                    case 2:
                        getClientsList();
                        break;
                    case 3:
                        sendMessage();
                        break;
                    case 4:
                        getClientsList();
                        System.out.println("who would you like to send secret?");
                        String name = sc.next();
                        System.out.println("type anything to send a message secretly to " + name);
                        String message = sc.nextLine();

                        String[] messageArr = sc.nextLine().split(" ");
                        for (int i = 0; i < messageArr.length; i++) {
                            message += (messageArr[i] + " ");

                        }
                        message = "<private><" + name + ">" + userName + " has whispered you:" + message;
                        bufferedWriter.write(message);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        break;
                    case 5:
                             bufferedWriter.write("quit");
                             bufferedWriter.newLine();
                             bufferedWriter.flush();
                             closeStream(socket,bufferedReader,bufferedWriter);
                             flag=false;
                       break ;
                }
            }
        }
        catch (
                IOException e) {
            System.out.println("Oh oh, something went wrong, failed to send message");
            e.printStackTrace();
            closeStream(socket, bufferedReader, bufferedWriter);
        }

    }

    //send to the server that we want to get the clients list
    public void getClientsList() {
        try {
            bufferedWriter.write("get clients list");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void leave() {
        try {
            bufferedWriter.write("quit");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedReader.close();
            socket.isClosed();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String userName, String message) {

        try {
            message = (this.userName + ": " + message);

            bufferedWriter.write("<private><" + userName + ">" + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            return " you successfully sent a secret to: " + userName;

        } catch (IOException e) {
            e.getCause();
            e.printStackTrace();
            return "failed to send secret to: " + userName;
        }

    }

    public void sendMessage() {
        System.out.println("type anything to send a message");
        String message = sc.nextLine();

        String[] messageArr = sc.nextLine().split(" ");

        for (int i = 0; i < messageArr.length; i++) {
            message += (messageArr[i] + " ");

        }
        try {
            bufferedWriter.write(userName + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            e.getCause();
            e.printStackTrace();
        }
    }

    public void receiveMessage() {// here we're about to create a new thread, so we'll be able to send and receive messages at the same time


        new Thread(new Runnable() {


            @Override
            public void run() {
                String groupMessage;
                while (socket.isConnected()) {
                    try {

                        groupMessage = bufferedReader.readLine();
                        System.out.println(groupMessage);
                        if (groupMessage==null){
                            socket.close();
                            Thread.currentThread().stop();
                        }

                    } catch (IOException e) {
                        System.out.println("oh oh something went wrong, system failed to receive message ");
                        e.printStackTrace();

                        closeStream(socket, bufferedReader, bufferedWriter);
                        Thread.currentThread().stop();
                    }
                }

            }
        }).start();
    }

    public void closeStream(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            if (bufferedReader != null) bufferedReader.close();

            if (bufferedWriter != null) bufferedWriter.close();

            if (socket != null) socket.close();


        } catch (IOException e) {
            System.out.println("failed to close connection");
            e.printStackTrace();
        }
    }
    public void shutdown(){
        try{
           bufferedWriter.close();
           bufferedReader.close();
           if(!socket.isClosed()){
               socket.close();
           }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        Socket socket = new Socket("localhost", 5004);
        Client client = new Client(socket, "user");
        while (flag) {
            System.out.println("chat menu:\n 1) Login\n 2) Register \n 3) exit ");
            int choose=Integer.parseInt(sc.nextLine());
            String user="";
            if(choose==1) {
                boolean bol = true;
                    System.out.println("please enter user name");
                    String userName = sc.nextLine();
                    user = userName;
                boolean exists = false;
                    while (bol) {
                            if(userName.length()>1)
                                exists =accounts.if_is_exists(userName);
                    if (exists) {
                        System.out.println("please enter passowrd");
                        String password = sc.nextLine();
                        String pas = accounts.getAccounts().get(userName);
                        if (password.equals(pas)) {
                            System.out.println("correct password");
                            client.setUserName(user);
                            client.receiveMessage();
                            client.send();
                            break;
                        } else {
                            System.out.println("password not correct ");
                            break;
                        }
                    }
                    else{
                        System.out.println("you dont have any account please ragister");
                         break;
                    }
                }
            }
            else if(choose==2) {
                boolean bol = true;
                while (bol) {
                    System.out.println("please enter user name");
                    String userName = sc.nextLine();
                    boolean exists =accounts.if_is_exists(userName);
                    if (!exists) {
                        System.out.println("please enter passowrd");
                        String password = sc.nextLine();
                        accounts.create(userName, password);
                        //users=accounts.getAccounts();
                        System.out.println("new account created ");
                        break;
                    } else {
                        System.out.println("your account alrady exist ");
                        break;
                    }
                }
                //exit
            }
            else{
                    break;
                }
//            Socket socket = new Socket("localhost", 5004);
//            Client client = new Client(socket, user);
//           client.receiveMessage();
//            client.send();
            }
        }
    }

