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
        Bindings bindings = new Bindings().addProtocol(StandardProtocolFamily.INET).addAddress(InetAddress.getByName("127.0.0.1"));
        PeerDHT master = PeerService.createSimpleDHTPeer(bindings);

        PeerService.discoverNetwork(master.peer());

        DiscoveryServer discoveryServer = new DiscoveryServer(master, bindings);
        discoveryServer.run();
    }
}
