package hydradfs.service;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class PeerService {

    private final static Integer port = 4000;

    public final static Random RND = new Random(42L);

    public static PeerDHT createSimpleDHTPeer(Bindings bindings) throws IOException {
        return new PeerBuilderDHT (new PeerBuilder (new Number160(RND)).ports(port).bindings(bindings).start()).start();
    }

    public static void discoverNetwork(Peer peer) throws UnknownHostException {
        FutureDiscover futureDiscover = peer.discover().inetAddress(InetAddress.getLocalHost()).ports(port).start();

        futureDiscover.addListener(new BaseFutureAdapter<FutureDiscover>() {
            @Override public void operationComplete(FutureDiscover futureDiscover) throws Exception {
                if (futureDiscover.isSuccess()) {
                    System.out.println("Discover success: found that my outside address is " + futureDiscover.peerAddress());
                } else {
                    System.out.println("failed " + futureDiscover.failedReason());
                }
            }
        });
    }
}
