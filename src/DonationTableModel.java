import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DonationTableModel extends AbstractTableModel {
    private final String[] COLUMNS;
    private List<Object[]> donations;
    private final int restaurantId;
    private final String filter;
    private final boolean isRestaurantView;

    public DonationTableModel(int restaurantId, String filter) {
        this.restaurantId = restaurantId;
        this.filter = filter;
        this.isRestaurantView = true;
        this.COLUMNS = new String[]{"ID", "Food Type", "Quantity", "Expiry Date", "Status"};
        refreshData();
    }

    public DonationTableModel(boolean forNGO) {
        this.restaurantId = -1;
        this.filter = "Available";
        this.isRestaurantView = false;
        this.COLUMNS = new String[]{"ID", "Food Type", "Quantity", "Expiry Date", "Location"};
        refreshData();
    }

    public void refreshData() {
        DonationDAO donationDAO = new DonationDAO();
        if (isRestaurantView) {
            this.donations = donationDAO.getDonationsByRestaurant(restaurantId, filter);
        } else {
            this.donations = donationDAO.getAvailableDonations();
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return donations.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return donations.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    public int getDonationIdAt(int rowIndex) {
        return (int) donations.get(rowIndex)[0];
    }
}