package marcomessini.mybookmarks;

/**
 * Created by marcomessini on 19/01/15.
 */
public class WebSite {
    public int id_WebSite;
    public int id_Groups;
    public String name;
    public String URL;
    public String hash;


    public WebSite(int id_WebSite , int id_Groups , String name, String URL, String hash) {
        this.id_WebSite = id_WebSite;
        this.id_Groups = id_Groups;
        this.name = name;
        this.URL = URL;
        this.hash = hash;
    }
}
