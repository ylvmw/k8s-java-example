import java.io.FileReader;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Node;
import io.kubernetes.client.models.V1NodeList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ListNodes {

    public static void main(String[] args) throws Exception {
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";
        
        ApiClient client =
            ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        CoreV1Api api = new CoreV1Api(client);
        V1NodeList nodeList = api.listNode(null, null, null, null, null, null, null, null);
        
        for (V1Node node : nodeList.getItems()) {
            System.out.println(node);
        }
    }

}
