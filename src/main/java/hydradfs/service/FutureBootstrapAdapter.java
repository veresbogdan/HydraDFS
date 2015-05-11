package hydradfs.service;

import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureBootstrapAdapter extends BaseFutureAdapter<FutureBootstrap> {

    private static Logger logger = LoggerFactory.getLogger(ChannelCreatorAdapter.class);

    private Peer peer;

    public FutureBootstrapAdapter(Peer peer) {
        this.peer = peer;
    }

    @Override
    public void operationComplete(FutureBootstrap futureBootstrap) throws Exception {
        if (futureBootstrap.isSuccess()) {
            logger.info("Bootstrap successful: known peers: " + peer.peerBean().peerMap().all());
        } else {
            logger.error("failed " + futureBootstrap.failedReason());
        }
    }
}
