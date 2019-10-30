import java.io.FileNotFoundException;
import java.io.FileReader;

import io.kubernetes.client.util.KubeConfig;

public class ParseKubeConfig {

    public static void main(String[] args) throws FileNotFoundException {
        // file path to your KubeConfig
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";

        // loading the out-of-cluster config, a kubeconfig from file-system
        KubeConfig kubeconfig = KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath));
        System.out.println("Server: " + kubeconfig.getServer());
        System.out.println("CA data: " + kubeconfig.getCertificateAuthorityData());
        System.out.println("Client CA data: " + kubeconfig.getClientCertificateData());
        System.out.println("Client key data: " + kubeconfig.getClientKeyData());
    }

}
