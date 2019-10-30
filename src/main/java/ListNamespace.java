import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ListNamespace {

    public static void main(String[] args) throws FileNotFoundException, IOException, ApiException {
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/minikube-config";

        ApiClient client =
            ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        CoreV1Api api = new CoreV1Api(client);
        
        V1NamespaceList v1NsList = api.listNamespace(null, null, null, null, null, null, null, null);
        for (V1Namespace ns : v1NsList.getItems()) {
            System.out.println(ns.getMetadata().getName());
        }
    }

}
