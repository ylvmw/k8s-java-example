import java.io.FileReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import sun.security.x509.X509CertImpl;

import com.google.gson.JsonObject;

import io.kubernetes.client.util.KubeConfig;

public class CreateCodeStreamK8SEndpoint {

    public static void main(String[] args) throws Exception {
        // file path to your KubeConfig
        String kubeConfigPath = System.getProperty("user.home") + "/.kube/minikube-config";

        // loading the out-of-cluster config, a kubeconfig from file-system
        KubeConfig kubeconfig = KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath));
        System.out.println("Server: " + kubeconfig.getServer());
        System.out.println("CA data: " + kubeconfig.getCertificateAuthorityData());
        System.out.println("Client CA data: " + kubeconfig.getClientCertificateData());
        System.out.println("Client key data: " + kubeconfig.getClientKeyData());
        String fingerprint = getHttpsCertFingerprint(kubeconfig.getServer());

        String reqBody = constructReqBody("minikube", "minikube", "demo", kubeconfig.getServer(), kubeconfig.getCertificateAuthorityData(), kubeconfig.getClientCertificateData(), kubeconfig.getClientKeyData(), fingerprint);

        System.out.println(reqBody);
        
    }
    
    private static String constructReqBody(String name, String desc, String prj, String k8sUrl, String certAuthorityData, String certData, String certKeyData, String fingerprint) {
        JsonObject props = new JsonObject();
        props.addProperty("kubernetesURL", k8sUrl);
        props.addProperty("authType", "certificate");
        props.addProperty("certAuthorityData", certAuthorityData);
        props.addProperty("certData", certData);
        props.addProperty("certKeyData", certKeyData);
        props.addProperty("fingerprint", fingerprint);
        
        JsonObject doc = new JsonObject();
        doc.addProperty("name", name);
        doc.addProperty("description", desc);
        doc.addProperty("isRestricted", "");
        doc.addProperty("type", "k8s");
        doc.addProperty("project", prj);
        doc.add("properties", props);
        return doc.toString();
    }
    
    private static String getHttpsCertFingerprint(String server) throws Exception {
        URL url = new URL(server);
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }};
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((arg0, arg1) -> true);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.connect();
        
        Certificate[] certs = conn.getServerCertificates();
        if (certs == null || certs.length == 0) {
            throw new Exception("No certificate found.");
        } else {
            return ((X509CertImpl) certs[0]).getFingerprint("SHA-1");
        }
    }

}
