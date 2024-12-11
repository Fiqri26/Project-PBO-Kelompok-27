public class Puzzle {
    private int id;
    private String imagePath;

    public Puzzle (int id, String imagePath){
        this.id = id;
        this.imagePath = imagePath;
    }

    public int getId (){
        return id;
    }

    public String getImagePath (){
        return imagePath;
    }
}
