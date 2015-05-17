package hydradfs.service;

import hydradfs.utils.MyData;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuturePutAdapter extends BaseFutureAdapter<FuturePut> {

    private static Logger logger = LoggerFactory.getLogger(ChannelCreatorAdapter.class);

    private Peer peer;
    private MyData<String> myData;

    public FuturePutAdapter(Peer peer, MyData<String> myData) {
        this.peer = peer;
        this.myData = myData;
    }

    @Override
    public void operationComplete(FuturePut futurePut) throws Exception {
        if (futurePut.isSuccess()) {
            logger.info("Peer " + peer.peerID() + " put into DHT: " + myData);
        } else {
            logger.error("Peer " + peer.peerID() + "tried to put: " + myData + " but it failed because: " + futurePut.failedReason());
        }
    }
}
