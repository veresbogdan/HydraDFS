package hydradfs.service;

import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.DiscoverNetworks;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

import java.io.IOException;

public class DiscoveryServer implements Runnable {

    private PeerDHT master;

    private Bindings bindings;

    public DiscoveryServer(PeerDHT peerDHT, Bindings bindings) {
        this.master = peerDHT;
        this.bindings = bindings;
    }

    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            //TODO log error
            e.printStackTrace();
        } catch (InterruptedException e) {
            //TODO log error
            e.printStackTrace();
        }
    }

    private void startServer() throws IOException, InterruptedException {
        //TODO to be replaced with logs
        System.out.println("Server started Listening to: " + DiscoverNetworks.discoverInterfaces(bindings));
        System.out.println("address visible to outside is " + master.peerAddress());

        while (true) {
            for (PeerAddress pa : master.peerBean().peerMap().all()) {
                System.out.println("PeerAddress: " + pa);
                Peer masterPeer = master.peer();

                FutureChannelCreator fcc = masterPeer.connectionBean().reservation().create(1, 1);
                fcc.addListener(new ChannelCreatorAdapter(masterPeer, pa));

            }

            Thread.sleep(1500);
        }
    }
}
