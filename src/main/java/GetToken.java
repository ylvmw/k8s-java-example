import java.io.FileReader;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Secret;
import io.kubernetes.client.models.V1ServiceAccount;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class GetToken {

    public static void main(String[] args) throws Exception {
        // file path to your KubeConfig
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/minikube-config";
        String namespace = "default";
        String serviceaccount = "default";

        // loading the out-of-cluster config, a kubeconfig from file-system
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        CoreV1Api api = new CoreV1Api(client);

        // invokes the CoreV1Api client
        V1ServiceAccount sa = api.readNamespacedServiceAccount(serviceaccount, namespace, null, false, false);
        System.out.println(sa.getSecrets().get(0).getName());
        
        V1Secret secret = api.readNamespacedSecret(sa.getSecrets().get(0).getName(), namespace, null, false, false);
        String token = new String(secret.getData().get("token"));
        System.out.println(token);
        
        V1NamespaceList v1NsList = api.listNamespace(null, null, null, null, null, null, null, null);
        for (V1Namespace ns : v1NsList.getItems()) {
            System.out.println(ns.getMetadata().getName());
        }
    }

}
