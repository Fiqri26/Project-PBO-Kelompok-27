public class Puzzel {
    private int id;
    private int level;
    private String imagePath;

    public Puzzel(int id, int level, String imagePath) {
        this.id = id;
        this.level = level;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getImagePath() {
        return imagePath;
    }
}
