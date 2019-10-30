import java.io.File;
import java.io.FileReader;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;

public class CreateService {

    public static void main(String[] args) throws Exception {
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";
        String namespace = "default";
        String filePath = System.getProperty("user.home") + "/k8s-script/octant_service.yaml";

        ApiClient client =
            ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        // the CoreV1Api loads default api-client from global configuration.        
        CoreV1Api api = new CoreV1Api(client);
        V1Service svcBody = (V1Service) Yaml.load(new File(filePath));        
        V1Service svc = api.createNamespacedService(namespace, svcBody, null, null, null);      
        System.out.println(svc.getKind() + " " + svc.getMetadata().getName() + " deployed.");
    }

}
