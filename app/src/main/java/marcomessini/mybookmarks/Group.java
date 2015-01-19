package marcomessini.mybookmarks;

/**
 * Created by marcomessini on 19/01/15.
 */
public class Group {
    public int id_group; //id gruppo
    public String name; //nome gruppo
    public int NSiti; //numero siti


    public Group(int id_group , String name ,int NSiti) {
        this.id_group = id_group;
        this.name = name;
        this.NSiti = NSiti;
    }
}
