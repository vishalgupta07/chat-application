/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Vishal_Gupta
 */
public class SoundSender {
    public SoundSender(Socket sock) throws LineUnavailableException, IOException{
    AudioFormat af = new AudioFormat(8000.0f,8,1,true,false);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
    TargetDataLine microphone = (TargetDataLine)AudioSystem.getLine(info);
    microphone.open(af);
   //Socket conn = new Socket(SERVER,3000);
    microphone.start();
    DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
    int bytesRead = 0;
    byte[] soundData = new byte[1];
    long countTime=0,time=20000;
    while(bytesRead != -1)
    {
        bytesRead = microphone.read(soundData, 0, soundData.length);
        if(bytesRead >= 0)
        {
            dos.write(soundData, 0, bytesRead);
        }
	countTime++;
	if(countTime==time)break;
    }
    System.out.println("IT IS DONE.");
    }
}
