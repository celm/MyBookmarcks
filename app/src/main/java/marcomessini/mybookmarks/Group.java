package marcomessini.mybookmarks;

/**
 * Created by marcomessini on 19/01/15.
 */
public class Group {
    public int id_group; //id gruppo
    public String name; //nome gruppo


    public Group(int id_group , String name ,int id_ws , String url_ws) {
        this.id_group = id_group;
        this.name = name;
    }
}
