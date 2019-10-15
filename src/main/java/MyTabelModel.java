import javax.swing.table.*;
/**
 * Allows the creation of a customised table model, that disallows editing of the componenets
 *
 * @author Alex McWhae
 */
class MyTableModel extends AbstractTableModel
{
    // column names for the table
    private String[] columnNames = {"Date", "Member ID", "Name", "Court ID", "Start Time", "End Time"};
    // the data to be displayed in the table
    private Object[][] data;
    /**
     * Constructor for objects of class TableModel
     *
     * @param data the 2dimensional array that holds all the data to be shown in the table
     */
    public MyTableModel(Object[][] data)
    {
        this.data = data;
    }

    /**
     * Gets the number of columns in the table
     *
     * @return int the number of columns
     */
    public int getColumnCount()
    {
        return columnNames.length;
    }

    /**
     * Gets the number of rows in the table
     *
     * @return int the number of rows
     */
    public int getRowCount()
    {
        return data.length;
    }

    /**
     * Gets the name of the specified column
     *
     * @param col the index of the column
     * @return String the column name
     */
    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    /**
     * Gets the value currently select in the table
     *
     * @param row the index of the row
     * @param col the index of the column
     * @return Object the object in the row, col specified
     */
    public Object getValueAt(int row, int col)
    {
        return data[row][col];
    }
}
