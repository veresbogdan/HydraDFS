package hydradfs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    static Logger logger = LoggerFactory.getLogger(ChannelCreatorAdapter.class);

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
                    logger.info("peer online TCP:" + peerAddress);
                } else {
                    logger.info("offline TCP" + peerAddress);
                }
            }
        });

        FutureResponse fr2 = peer.pingRPC().pingUDP(peerAddress, cc, new DefaultConnectionConfiguration());

        fr2.addListener(new BaseFutureAdapter<FutureResponse>() {
            public void operationComplete(FutureResponse futureResponse) throws Exception {
                if (futureResponse.isSuccess()) {
                    logger.info("peer online UDP:" + peerAddress);
                } else {
                    logger.info("offline UDP" + peerAddress);
                }
            }
        });
    }
}
