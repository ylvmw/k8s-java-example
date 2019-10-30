import java.io.FileReader;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1beta1Api;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class DeleteDeploy {

    public static void main(String[] args) throws Exception {
        
        String kubeConfigPath = System.getProperty("user.home") +  "/.kube/config";
        // loading the out-of-cluster config, a kubeconfig from file-system
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        
        AppsV1beta1Api appApi = new AppsV1beta1Api(client);
        String namespace = "kube-system";
        String name = "octant";
        V1Status status = appApi.deleteCollectionNamespacedDeployment(namespace, null, null, null, "app="+name, null, null, null, null);
        System.out.println(status);
        System.out.println("Deleted depolyment: " + name + " in namespace: " + namespace);

    }

}
