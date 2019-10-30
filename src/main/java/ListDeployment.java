import java.io.FileReader;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1beta1Api;
import io.kubernetes.client.models.AppsV1beta1Deployment;
import io.kubernetes.client.models.AppsV1beta1DeploymentList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ListDeployment {

    public static void main(String[] args) throws Exception {
        // file path to your KubeConfig
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";
        String namespace = "default";

        ApiClient client =
            ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        AppsV1beta1Api api = new AppsV1beta1Api(client);
        
        AppsV1beta1DeploymentList v1DeployList = api.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null);
        for (AppsV1beta1Deployment deploy : v1DeployList.getItems()) {
            System.out.println(deploy);
        }
    }

}
