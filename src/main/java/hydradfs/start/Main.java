package hydradfs.start;

import hydradfs.service.DiscoveryServer;
import hydradfs.service.PeerService;
import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.StandardProtocolFamily;
import net.tomp2p.dht.PeerDHT;

import java.io.IOException;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws IOException {
    	
    	//do this for the first peer, otherwise do not use bindings (give arguments when running to determine which peer u are)
        //Bindings bindings = new Bindings().addInterface("en1");
        
        //otherwise if you are another peer use this
        Bindings bind = new Bindings();
        
        
        PeerDHT master = PeerService.createSimpleDHTPeer(bind);

        //manually the IP address of some peer that you know :) This one is the TAs..for now.
        PeerService.discoverNetwork(master.peer(), "192.168.1.42");

        DiscoveryServer discoveryServer = new DiscoveryServer(master, bind);
        discoveryServer.run();
    }
}
