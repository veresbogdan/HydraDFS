package hydradfs.service;

import hydradfs.utils.Util;
import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.DiscoverNetworks;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class DiscoveryServer implements Runnable {

    private PeerDHT master;

    private Bindings bindings;
    
    private static Logger logger = LoggerFactory.getLogger(DiscoveryServer.class);

    public DiscoveryServer(PeerDHT peerDHT, Bindings bindings) {
        this.master = peerDHT;
        this.bindings = bindings;
    }

    public void run() {
        try {
            startServer();
        } catch (IOException | InterruptedException e) {
        	Util.logException(logger, e);
        }
    }

    private void startServer() throws IOException, InterruptedException {
        logger.info("Server started Listening to: " + DiscoverNetworks.discoverInterfaces(bindings));
        logger.info("address visible to outside is " + master.peerAddress());

        while (true) {
            for (PeerAddress pa : master.peerBean().peerMap().all()) {
                logger.info("PeerAddress: " + pa);
                Peer masterPeer = master.peer();

                FutureChannelCreator fcc = masterPeer.connectionBean().reservation().create(1, 1);
                fcc.addListener(new ChannelCreatorAdapter(masterPeer, pa));

            }

            Thread.sleep(1500);
        }
    }
}
