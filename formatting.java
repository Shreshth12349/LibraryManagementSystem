import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
public class formatting {
    public static Object[][] resultSetTo2DArray(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Object[]> rows = new ArrayList<>();
        Object[] columnNames = new Object[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i).toUpperCase();
        }
        rows.add(columnNames);
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getObject(i);
            }
            rows.add(rowData);
        }
        return rows.toArray(new Object[0][0]);
    }
    public static String format2DArray(Object[][] data) {
        StringBuilder formattedTable = new StringBuilder();

        int[] columnWidths = new int[data[0].length];
        for (Object[] row : data) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], String.valueOf(row[i]).length());
            }
        }
        formattedTable.append("+");
        for (int width : columnWidths) {
            formattedTable.append("-".repeat(width + 2)).append("+");
        }
        formattedTable.append("\n");

        for (Object[] row : data) {
            formattedTable.append("|");
            for (int i = 0; i < row.length; i++) {
                formattedTable.append(String.format(" %-" + columnWidths[i] + "s |", row[i]));
            }
            formattedTable.append("\n");
            formattedTable.append("+");
            for (int width : columnWidths) {
                formattedTable.append("-".repeat(width + 2)).append("+");
            }
            formattedTable.append("\n");
        }
        return formattedTable.toString();
    }
    public static int calculateDaysGap(String startDateString, String endDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYY-MM-DD");
        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            long differenceInMillis = endDate.getTime() - startDate.getTime();
            int daysGap = (int) (differenceInMillis / (1000 * 60 * 60 * 24));
            return daysGap;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
