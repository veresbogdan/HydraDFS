package hydradfs.service;

import net.tomp2p.connection.ChannelCreator;
import net.tomp2p.connection.DefaultConnectionConfiguration;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

public class ChannelCreatorAdapter extends BaseFutureAdapter<FutureChannelCreator> {

    private Peer peer;

    private PeerAddress peerAddress;

    public ChannelCreatorAdapter(Peer peer, PeerAddress peerAddress) {
        this.peer = peer;
        this.peerAddress = peerAddress;
    }

    public void operationComplete(FutureChannelCreator futureChannelCreator) throws Exception {
        ChannelCreator cc = futureChannelCreator.channelCreator();

        FutureResponse fr1 = peer.pingRPC().pingTCP(peerAddress, cc, new DefaultConnectionConfiguration());

        fr1.addListener(new BaseFutureAdapter<FutureResponse>() {
            public void operationComplete(FutureResponse futureResponse) throws Exception {
                if (futureResponse.isSuccess()) {
                    System.out.println("peer online TCP:" + peerAddress);
                } else {
                    System.out.println("offline TCP" + peerAddress);
                }
            }
        });

        FutureResponse fr2 = peer.pingRPC().pingUDP(peerAddress, cc, new DefaultConnectionConfiguration());

        fr2.addListener(new BaseFutureAdapter<FutureResponse>() {
            public void operationComplete(FutureResponse futureResponse) throws Exception {
                if (futureResponse.isSuccess()) {
                    System.out.println("peer online UDP:" + peerAddress);
                } else {
                    System.out.println("offline UDP" + peerAddress);
                }
            }
        });
    }
}
