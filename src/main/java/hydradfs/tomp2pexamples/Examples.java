package hydradfs.tomp2pexamples;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.util.Random;

public class Examples {

    private static final Random RND = new Random(42L);
    private static final String KEY = "Max Powers";
    private static final int PEER_1 = 1;
    private static final int PEER_2 = 2;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        PeerDHT master = null;
        final int nrPeers = 10;
        final int port = 4001;

        try {
            PeerDHT[] peers = createAndAttachPeersDHT(nrPeers, port);

            System.out.println("Created 10 peers with ids: ");
            for (PeerDHT peerDHT: peers) {
                System.out.println(peerDHT.peerID());
            }

            bootstrap(peers);

            setupReplyHandler(peers);

            master = peers[0];
            Number160 number160 = Number160.createHash(KEY);

            examplePutGet(peers, number160);
            Thread.sleep(500);

        } finally {
            if (master != null) {
                master.shutdown();
            }
        }
    }

    public static PeerDHT[] createAndAttachPeersDHT(int nr, int port) throws IOException {
        PeerDHT[] peers = new PeerDHT[nr];

        for (int i = 0; i < nr; i++) {
            if (i == 0) {
                peers[0] = new PeerBuilderDHT(new PeerBuilder(new Number160(RND)).ports(port).start()).start();
            } else {
                peers[i] = new PeerBuilderDHT(new PeerBuilder(new Number160(RND)).masterPeer(peers[0].peer()).start()).start();
            }
        }

        return peers;
    }

    public static void bootstrap(PeerDHT[] peers) {
        for (PeerDHT peer : peers) {
            for (PeerDHT peer1 : peers) {
                peer.peerBean().peerMap().peerFound(peer1.peerAddress(), null, null, null);
            }
        }
    }

    private static void examplePutGet(final PeerDHT[] peers, final Number160 nr) throws IOException, ClassNotFoundException {
        FuturePut futurePut = peers[PEER_1].put(nr).data(new Data(peers[PEER_1].peerAddress())).start();

        futurePut.addListener(new BaseFutureAdapter<FuturePut>() {
            public void operationComplete(FuturePut future) throws Exception {
                System.out.println("\nPeer " + PEER_1 + " with id " + peers[PEER_1].peerID() +" stored [key: " + nr + ", value: " + peers[PEER_1].peerAddress());

                exampleGetNonBlocking(peers, nr);

            }
        });
    }

    private static void exampleGetNonBlocking(final PeerDHT[] peers, final Number160 nr) {
        FutureGet futureGet = peers[PEER_2].get(nr).start();
        // non-blocking operation
        futureGet.addListener(new BaseFutureAdapter<FutureGet>() {
            public void operationComplete(FutureGet future) throws Exception {
                System.out.println("result non-blocking: " + future.data().object() + " \nfrom key: " + nr);
                PeerAddress address = (PeerAddress) future.data().object();

                sendDataDirectly(peers, address);
            }

        });
    }

    private static void sendDataDirectly(final PeerDHT[] peers, PeerAddress address) {
        final FutureDirect futureDirect = peers[PEER_2].peer().sendDirect(address).object("The number 12345 belongs to Max Powers").start();

        futureDirect.addListener(new BaseFutureAdapter<FutureDirect>() {
            public void operationComplete(FutureDirect futureDirect1) throws Exception {
                System.out.println("Direct sending done! The reply message was: " + futureDirect1.object());
            }
        });
    }

    private static void setupReplyHandler(PeerDHT[] peers) {
        for (final PeerDHT peer : peers) {
            peer.peer().objectDataReply(new ObjectDataReply() {
                public Object reply(PeerAddress sender, Object request) throws Exception {
                    System.err.println("I'm " + peer.peerID() + " and I just got the message [" + request
                            + "] from " + sender.peerId());
                    return "Reply Hello world";
                }
            });
        }
    }
}
