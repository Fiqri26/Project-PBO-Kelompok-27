public class Puzzel {
    private int id;
    private String imagePath;

    public Puzzel(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }
}
