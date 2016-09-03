package hasoffer.task.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;

import java.util.List;

/**
 * Date : 2016/6/8
 * Function :
 */
//@Controller
//@RequestMapping(value = "/serverip")
public class IPController {

    static BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("AKIAI2KXGSAA6ML4ZSJQ", "vDUeGxdjPeH1ulHark/VhKlAkD4d9L/wVpBINxep");
    static AmazonEC2Client client = new AmazonEC2Client(basicAWSCredentials)
            .withRegion(Regions.AP_SOUTHEAST_1);

    private static void instance() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("AKIAI2KXGSAA6ML4ZSJQ", "vDUeGxdjPeH1ulHark/VhKlAkD4d9L/wVpBINxep");
        AmazonEC2Client client = new AmazonEC2Client(basicAWSCredentials)
                .withRegion(Regions.AP_SOUTHEAST_1);
        DescribeInstancesResult describeInstancesResult = client.describeInstances();
        List<Reservation> reservations = describeInstancesResult.getReservations();
        for (Reservation reservation : reservations) {
            List<Instance> instances = reservation.getInstances();
            if (instances != null) {
                for (Instance instance : instances) {
                    List<Tag> tags = instance.getTags();
                    if (tags != null) {
                        for (Tag tag : tags) {
                            if ("Name".equalsIgnoreCase(tag.getKey()) && "dns1".equalsIgnoreCase(tag.getValue())) {
                                System.out.println(instance);

                            }
                        }
                    }
                }
            }
        }
    }

    public static void changeIp(String instanceId) {
        instanceId = "i-2a01078e";

        DisassociateAddressRequest disassociateAddressRequest = new DisassociateAddressRequest();

    }

    public static void ip() {

        //分配ip
        addressInfo(client);
        AllocateAddressResult allocateAddressResult = client.allocateAddress();
        System.out.println(allocateAddressResult);
        addressInfo(client);

        //绑定ip
        AssociateAddressRequest associateAddressRequest = new AssociateAddressRequest();
        associateAddressRequest.setInstanceId("i-2a01078e");
        associateAddressRequest.setAllocationId(allocateAddressResult.getAllocationId());
        AssociateAddressResult associateAddressResult = client.associateAddress(associateAddressRequest);

        addressInfo(client);

        //解绑ip
        DisassociateAddressRequest disassociateAddressRequest = new DisassociateAddressRequest();
        disassociateAddressRequest.setAssociationId(associateAddressResult.getAssociationId());
        client.disassociateAddress(disassociateAddressRequest);

        addressInfo(client);

        //释放ip
        ReleaseAddressRequest releaseAddressRequest = new ReleaseAddressRequest();
        releaseAddressRequest.setAllocationId(allocateAddressResult.getAllocationId());
        client.releaseAddress(releaseAddressRequest);

        addressInfo(client);

    }

    private static void addressInfo(AmazonEC2Client client) {
        System.out.println("*******************addressInfo*****************");

        DescribeAddressesResult describeAddressesResult = client.describeAddresses();
        List<Address> addresses = describeAddressesResult.getAddresses();
        for (Address address : addresses) {
            System.out.println(address);
        }
        System.out.println("*******************addressInfo*****************");

    }

}
