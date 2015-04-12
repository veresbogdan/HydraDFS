package hydradfs.service;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

import java.io.IOException;
import java.util.Random;

public class PeerService {

    private final static Integer port = 4444;

    public final static Random RND = new Random(42L);

    public PeerDHT createSimpleDHTPeer() throws IOException {
        return new PeerBuilderDHT (new PeerBuilder (new Number160(RND)).ports(port).start()).start();
    }
}
