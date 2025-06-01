import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POITest {
    public static void main(String[] args) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            System.out.println("POI đã hoạt động!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
