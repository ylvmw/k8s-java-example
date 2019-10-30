import java.io.FileReader;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ListService {

    public static void main(String[] args) throws Exception {
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";
 
        ApiClient client =
            ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        CoreV1Api api = new CoreV1Api(client);
        
        String namespace = "default";
        V1ServiceList v1SvcList = api.listNamespacedService(namespace, null, null, null, null, null, null, null, null);
        for (V1Service svc : v1SvcList.getItems()) {
            System.out.println(svc);
        }
    }

}
