package hydradfs.service;

import hydradfs.utils.MyData;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number640;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FutureGetAdapter extends BaseFutureAdapter<FutureGet> {

    private static Logger logger = LoggerFactory.getLogger(ChannelCreatorAdapter.class);

    private Peer peer;

    public FutureGetAdapter(Peer peer) {
        this.peer = peer;
    }

    @Override
    public void operationComplete(FutureGet futureGet) throws Exception {
        if (futureGet.isSuccess()) {
            Map<Number640, Data> map = futureGet.dataMap();

            for (Data data : map.values()) {
                @SuppressWarnings("unchecked")
                MyData<String> myData = (MyData<String>) data.object();
                logger.info("Peer " + peer.peerID() + " got from DHT: key: " + myData.key() + ", domain: " +
                        myData.domain() + ", content: " + myData.content() + ", data: " + myData.data());
            }
        } else {
            logger.error("Peer " + peer.peerID() + "tried to get data from DHT but it failed because: " + futureGet.failedReason());
        }
    }
}
