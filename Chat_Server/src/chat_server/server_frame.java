package chat_server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class server_frame extends javax.swing.JFrame 
{
   ArrayList clientOutputStreams;                       //dynamic size array in java
   ArrayList<Socket> sockets;
   ArrayList<String> users;                             //users list in chat app
   HashMap<String,PrintWriter> map = new HashMap<String,PrintWriter>();
   HashMap<String,Socket> map1 = new HashMap<String,Socket>();

    public class EchoThread implements Runnable {
        String username;
        DataInputStream dataIn = null;
        ServerSocket ss;
        public void setuser(String name){
            this.username=name;
        }
        public EchoThread(){
            try {
                ss=new ServerSocket(2222);
                Socket s=ss.accept();
                dataIn=new DataInputStream(s.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(server_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      @Override
	public void run()
	{
            System.out.println("echo recived");
    	int bytesRead = 0;
    	byte[] inBytes = new byte[1];
    	while(bytesRead != -1)
    	{
            System.out.println("visajghd");
        	try{bytesRead = dataIn.read(inBytes, 0, inBytes.length);}catch (IOException e)       {}
        	if(bytesRead >= 0)
        	{
                    try {
                        sendToParticular(inBytes, bytesRead);
                    } catch (IOException ex) {
                        Logger.getLogger(server_frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
        	}
    	}
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(server_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        ta_chat.append("transmission over\n");
    	//sockets.remove(connection);
        }}

//        private void sendToParticular(byte[] byteArray, int q,String s) throws IOException {
//            DataOutputStream tempOut = null;
//            Socket temp = map1.get(s);
//            try {
//                tempOut = new DataOutputStream(temp.getOutputStream());
//            } catch (IOException ex) {
//                Logger.getLogger(server_frame.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            tempOut.write(byteArray, 0, q);
//            PrintWriter wr=new PrintWriter(map.get(s));
//            wr.println("audio recieved");
//        }
            private void sendToParticular(byte[] byteArray, int q) throws IOException {
            //DataOutputStream tempOut = null;
            
Iterator<Socket> sockIt = sockets.iterator();
    while(sockIt.hasNext())
    {
        Socket temp = sockIt.next();
        DataOutputStream tempOut = null;
        try
        {
            tempOut = new DataOutputStream(temp.getOutputStream());
        } catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try{tempOut.write(byteArray, 0, q);
            System.out.println("data written");
        }catch (IOException e){}
      }
        System.out.println("vhsafcjhSVC");
        }

   
    
   public class ClientHandler implements Runnable	//inner class
   {
       BufferedReader reader;       //bufferd reader class
       Socket sock;     //socket to accept communication with clients
       PrintWriter client;
       public ClientHandler(Socket clientSocket, PrintWriter user) //constructor of clientHandler
       {
            client = user;
            try 
            {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());      //reading from the client
                reader = new BufferedReader(isReader);
            }
            catch (Exception ex) 
            {
                ta_chat.append("Unexpected error... \n");
            }
       }

       @Override
       public void run() 
       {
            String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat" ,voice="voice";
            String[] data;

            try 
            {
                while ((message = reader.readLine()) != null) 
                {
                    ta_chat.append("Received: " + message + "\n");
                    data = message.split(":");      //split function
                    
                    for (String token:data) 
                    {
                        ta_chat.append(token + "\n");
                    }

                    if (data[2].equals(connect)) //on pressing connect button on client 
                    {
                        tellEveryone((data[0] + ":" + data[1] + ":" + chat));//displaying everyone that it is connected
                        userAdd(data[0],client,sock);//adding data to arraylist
                    } 
                    else if (data[2].equals(disconnect)) 
                    {
                        tellEveryone((data[0] + ":has disconnected." + ":" + chat));//displaying that it has disconnected
                        userRemove(data[0]);//removing user from arraylist
                    } 
                    else if (data[2].equals(chat)) //----------------------------------here we need to change the message part---------------------------------------------------------------------------------
                    {
                        tellParticular(message,data[3]);
                    } 
                    else if(data[2]==voice){
                        System.out.println("voice comm");
                        Thread echothread=new Thread(new EchoThread());
                         echothread.start();
                    }
                    else 
                    {
                        ta_chat.append("No Conditions were met. \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                ta_chat.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(client);
             } 
	} 
    }

    public server_frame() 
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();
        b_end = new javax.swing.JButton();
        b_users = new javax.swing.JButton();
        b_clear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_start.setText("START");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setText("END");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setText("Online Users");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setText("Clear");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_users, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start)
                    .addComponent(b_users))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_clear)
                    .addComponent(b_end))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_endActionPerformed
        try 
        {                                                                   //when endButton is pressed
            Thread.sleep(5000);                 //5000 milliseconds is five second.
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
        
        tellEveryone("Server:is stopping and all users will be disconnected.\n:Chat");
        ta_chat.append("Server stopping... \n");
        
        ta_chat.setText("");
    }//GEN-LAST:event_b_endActionPerformed

    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_startActionPerformed
        Thread starter = new Thread(new ServerStart());         //srever started 
        starter.start();
        
        ta_chat.append("Server started...\n");

    }//GEN-LAST:event_b_startActionPerformed

    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_usersActionPerformed
        ta_chat.append("\n Online users : \n");
        for (String current_user : users)           //the list of users present in the list created by addUser function
        {
            ta_chat.append(current_user);
            ta_chat.append("\n");
        }    
        
    }//GEN-LAST:event_b_usersActionPerformed

    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_clearActionPerformed
        ta_chat.setText("");                                                //clearing the console of server
    }//GEN-LAST:event_b_clearActionPerformed

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new server_frame().setVisible(true);
            }
        });
    }
    
    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();
            users = new ArrayList();  
            sockets=new ArrayList();
            try 
            {
                ServerSocket serverSock = new ServerSocket(2222);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				clientOutputStreams.add(writer);
                                sockets.add(clientSock);
				Thread listener = new Thread(new ClientHandler(clientSock, writer));
				listener.start();                                                       //reading will get start
				ta_chat.append("Got a connection. \n");
                                
                }
            }
            catch (Exception ex)
            {
                ta_chat.append("Error making a connection. \n");
            }
        }
    }
    
    public void userAdd (String data,PrintWriter writer,Socket sock) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        ta_chat.append("Before " + name + " added. \n");
        users.add(name);
        map.put(data, writer);
        map1.put(data,sock);
        ta_chat.append("After " + name + " added. \n");
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void userRemove (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void tellEveryone(String message) 
    {
	Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) 
        {   
            try 
            {
                PrintWriter writer = (PrintWriter) it.next();
		writer.println(message);
		ta_chat.append("Sending: " + message + "\n");
                writer.flush();
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling everyone. \n");
            }
        } 
    }
       public void tellParticular(String message,String user) 
    {
	 try 
            {
                PrintWriter writer = map.get(user);
		writer.println(message);
		ta_chat.append("Sending: " + message + "\n");
                writer.flush();
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling. \n");
            }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_clear;
    private javax.swing.JButton b_end;
    private javax.swing.JButton b_start;
    private javax.swing.JButton b_users;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea ta_chat;
    // End of variables declaration//GEN-END:variables
}
