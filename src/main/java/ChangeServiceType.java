import java.io.FileReader;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ChangeServiceType {

    public static void main(String[] args) throws Exception {
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/minikube-config";
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        CoreV1Api api = new CoreV1Api(client);
        String svc = "kubernetes-dashboard";
        String ns = "kube-system";
        String type = "NodePort";
        V1Patch patch = new V1Patch("[{\"op\": \"replace\",\"path\": \"/spec/type\",\"value\": \"" + type + "\"}]");
        try {
            api.patchNamespacedService(svc, ns, patch, null, null, null, null);
            System.out.println("Patch successfully.");
        } catch (ApiException e) {
            System.out.println("Patch failed: resp body=" + e.getResponseBody());
            throw e;
        }
    }

}
