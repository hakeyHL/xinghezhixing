package hasoffer.webcommon.akka;

import akka.actor.UntypedActor;
import hasoffer.akka.pojo.AkkaJobMessage;
import hasoffer.core.bo.user.DeviceRequestBo;
import hasoffer.core.user.IDeviceService;
import hasoffer.core.user.impl.DeviceServiceImpl;
import org.springframework.web.context.ContextLoader;

public class DeviceLogActor extends UntypedActor {

//    private List<DeviceRequestBo> deviceList = new ArrayList<DeviceRequestBo>();

    private IDeviceService deviceService = (IDeviceService) ContextLoader.getCurrentWebApplicationContext().getBean(DeviceServiceImpl.class);

    @Override
    public void onReceive(Object message) throws Exception {
//        int batchSize=1000;
        if (message instanceof AkkaJobMessage) {
            AkkaJobMessage jobMessage = (AkkaJobMessage) message;
            if (jobMessage.getObject() instanceof DeviceRequestBo) {
//                deviceList.add((DeviceRequestBo) jobMessage.getObject());
//                if (deviceList.size() % batchSize == 0) {
//                    deviceService.batchSaveDeviceRequest(deviceList);
//                    deviceList.clear();
//                }
                deviceService.saveDeviceRequest((DeviceRequestBo) jobMessage.getObject());
            }
        } else {
            unhandled(message);
        }
    }
}
