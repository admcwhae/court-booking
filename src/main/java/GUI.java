import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.time.*;
/**
 * Makes a GUI using Java GUI components, with all the same functionality as the UI version
 *
 * @author Alex McWhae
 */
public class GUI
{
    private Club sportsClub;
    private JFrame mainFrame;
    private JTable infoTable;
    private JScrollPane tablePane;
    private JButton showAvailButton;
    private JButton showCourtButton;
    private JButton showMemberButton;
    private JButton addBookingButton;
    private JButton deleteBookingButton;
    private JButton saveExitButton;
    /**
     * Constructor
     *
     * @param sportsClub the club object
     */
    public GUI(Club sportsClub)
    {
        this.sportsClub = sportsClub;
    }

    /**
     * Runs the GUI
     */
    public void run()
    {
        mainFrame = new JFrame("BookingManager");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(1280, 720));
        mainFrame.setLayout(new BorderLayout());

        // Makes it so upon closing the window, save and exit runs
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we)
            {
                exit();
            }
        });

        // PANELS
        JPanel menuPanel = new JPanel();
        JPanel tablePanel = new JPanel();
        // Set size and layout of menu panel
        menuPanel.setPreferredSize(new Dimension(200, 720));
        menuPanel.setLayout(new GridLayout(0,1));
        // Creates table object
        infoTable = new JTable();
        infoTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        infoTable.setFillsViewportHeight(true);
        //Create the scroll pane and add the table to it.
        tablePane = new JScrollPane();

        // BUTTONS
        showAvailButton();
        showCourtButton();
        showMemberButton();
        addBookingButton();
        deleteBookingButton();
        exitButton();
        // Add buttons to panel
        menuPanel.add(showAvailButton);
        menuPanel.add(showCourtButton);
        menuPanel.add(showMemberButton);
        menuPanel.add(addBookingButton);
        menuPanel.add(deleteBookingButton);
        menuPanel.add(saveExitButton);
        // Add to layout
        mainFrame.add(menuPanel, BorderLayout.WEST);
        mainFrame.add(tablePane, BorderLayout.CENTER);
        // Make visible
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Creates and adds the listener with required functionality for Show Available Courts button
     */
    public void showAvailButton()
    {
        showAvailButton = new JButton("Show Available Courts");
        showAvailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get names of sports offered by club for combobox
                ArrayList<String> sportsList = sportsClub.getSportsNamesToString();
                String[] sportsArr = new String[sportsList.size()];
                sportsArr = sportsList.toArray(sportsArr);

                // gets the next 7 days for combobox
                String[] datesArr = new String[7];
                for (int i = 0; i < datesArr.length; i++)
                {
                    datesArr[i] = LocalDate.now().plusDays(i).toString();
                }

                JComboBox sportBox = new JComboBox(sportsArr);
                JComboBox dateBox = new JComboBox(datesArr);

                JPanel panel = new JPanel(new GridLayout(0,1));
                panel.add(new JLabel("Sport"));
                panel.add(sportBox);
                panel.add(new JLabel("Date"));
                panel.add(dateBox);

                int result = JOptionPane.showConfirmDialog(null, panel, "Show Available Courts",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                // if selected ok
                if (result == JOptionPane.OK_OPTION)
                {
                    int sportIndex = sportBox.getSelectedIndex();
                    String dateString = dateBox.getSelectedItem().toString();
                    LocalDate date = LocalDate.parse(dateString);

                    ArrayList<String> availableCourts = sportsClub.getSport(sportIndex).getAvailableCourtsGraph(date);
                    availableCourts.add(0, "|-------------------------------------------------------------|");
                    availableCourts.add(1,String.format("|        Available Courts on %s for %-18s|", date, sportsClub.getSport(sportIndex).getName()));
                    availableCourts.add(2, "|-------------------------------------------------------------|");
                    availableCourts.add(3, "|time | 9   10  11  12  13  14  15  16  17  18  19  20  21  22|");
                    availableCourts.add(4, "|  id | |   |   |   |   |   |   |   |   |   |   |   |   |   | |");
                    availableCourts.add("|-------------------------------------------------------------|");
                    availableCourts.add("|                                                             |");
                    availableCourts.add("|                                      x's denote booked court|");
                    availableCourts.add("|-------------------------------------------------------------|");
                    // creates list from array
                    JList list = new JList(availableCourts.toArray());
                    // sets font to monospaced font
                    list.setFont(new Font("Courier New",Font.BOLD, 20));

                    // Display graph in table pane
                    mainFrame.remove(tablePane);
                    tablePane = new JScrollPane(list);
                    mainFrame.add(tablePane, BorderLayout.CENTER);
                    refreshGUI();
                }

            }
        });
    }

    /**
     * Creates and adds the listener with required functionality for Show Court Bookings button
     */
    public void showCourtButton()
    {
        showCourtButton = new JButton("Show Court Bookings");
        showCourtButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try
                {
                    Object[][] data = null;
                    String courtId = JOptionPane.showInputDialog(mainFrame, "Please Enter the Court Id:");
                    if (courtId != null)
                    {
                        // gets the required data for that court
                        data = getCourtData(Integer.parseInt(courtId));
                        mainFrame.remove(tablePane);
                        // creates table with new data
                        createTablePane(data);
                        refreshGUI();
                    }
                }
                catch (MyException ex)
                {
                    errorDialog(ex.getMessage());
                }
                catch (Exception NumberFormatException)
                {
                    errorDialog("You must enter an Integer");
                }
            }
        });
    }

    /**
     * Creates and adds the listener with required functionality for Show Member Bookings button
     */
    public void showMemberButton()
    {
        showMemberButton = new JButton("Show Member Bookings");
        showMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try
                {
                    String memberId = JOptionPane.showInputDialog(mainFrame, "Please Enter the Member Id:");
                    if (memberId != null)
                    {
                        // gets the required data for that member
                        Object[][] data = getMemberData(Integer.parseInt(memberId));
                        mainFrame.remove(tablePane);
                        // creates table with new data
                        createTablePane(data);
                        refreshGUI();
                    }
                }
                catch (MyException ex)
                {
                    errorDialog(ex.getMessage());
                }
                catch (Exception NumberFormatException)
                {
                    errorDialog("You must enter an Integer");
                }
            }
        });
    }

    /**
     * Creates and adds the listener with required functionality for Add Booking button
     */
    public void addBookingButton()
    {
        addBookingButton = new JButton("Add Booking");
        addBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try
                {
                    ArrayList<String> sportsList = sportsClub.getSportsNamesToString();
                    String[] sportsArr = new String[sportsList.size()];
                    sportsArr = sportsList.toArray(sportsArr);
                    // makes data array
                    String[] datesArr = new String[7];
                    for (int i = 0; i < datesArr.length; i++)
                    {
                        datesArr[i] = LocalDate.now().plusDays(i).toString();
                    }
                    // possible start and end times
                    String[] startArr = {"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"};
                    String[] endArr = {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"};

                    JTextField memberIdField = new JTextField();
                    JComboBox sportBox = new JComboBox(sportsArr);
                    JComboBox dateBox = new JComboBox(datesArr);
                    JComboBox startBox = new JComboBox(startArr);
                    JComboBox endBox = new JComboBox(endArr);
                    // adds to panel
                    JPanel panel = new JPanel(new GridLayout(0,1));
                    panel.add(new JLabel("Member Id"));
                    panel.add(memberIdField);
                    panel.add(new JLabel("Sport"));
                    panel.add(sportBox);
                    panel.add(new JLabel("Date"));
                    panel.add(dateBox);
                    panel.add(new JLabel("Start Time"));
                    panel.add(startBox);
                    panel.add(new JLabel("End Time"));
                    panel.add(endBox);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Add Booking",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    // if ok selected
                    if (result == JOptionPane.OK_OPTION)
                    {
                        // gets input in the required formats
                        int memberId = Integer.parseInt(memberIdField.getText());
                        String sportName = sportsClub.getSport(sportBox.getSelectedIndex()).getName();
                        LocalDate date = LocalDate.parse(dateBox.getSelectedItem().toString());
                        LocalTime startTime = LocalTime.of(Integer.parseInt(startBox.getSelectedItem().toString()),0);
                        LocalTime endTime = LocalTime.of(Integer.parseInt(endBox.getSelectedItem().toString()),0);

                        // checks that data entered is valid, catching any exceptions if invalid
                        sportsClub.getMember(memberId);
                        sportsClub.validMemberFinancial(memberId);
                        if (!sportsClub.getMember(memberId).playsSport(sportName))
                            throw new MyException("Member Id: " + memberId + " does not play " + sportName);
                        sportsClub.validBookingDate(date, memberId);
                        sportsClub.validBookingStartTime(startTime);
                        sportsClub.validBookingEndTime(startTime, endTime, sportName);
                        // adds the booking
                        sportsClub.addBooking(memberId, sportName, date, startTime, endTime);
                        // show successful booking
                        JOptionPane.showMessageDialog(mainFrame, "Booking Succesful", "Success", JOptionPane.INFORMATION_MESSAGE);

                        Object[][] data = getMemberData(memberId);
                        mainFrame.remove(tablePane);
                        createTablePane(data);
                        refreshGUI();
                    }
                }
                catch (MyException ex)
                {
                    errorDialog(ex.getMessage());
                }
                catch (NumberFormatException ex)
                {
                    errorDialog("Member Id must be an integer");
                }
            }
        });
    }

    /**
     * Creates and adds the listener with required functionality for Delete Booking button
     */
    public void deleteBookingButton()
    {
        deleteBookingButton = new JButton("Delete Booking");
        deleteBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try
                {
                    int row = infoTable.getSelectedRow();
                    String[] bookingDelete = new String[infoTable.getColumnCount()];
                    for (int i = 0; i < infoTable.getColumnCount(); i++)
                    {
                        bookingDelete[i] = infoTable.getValueAt(row, i).toString();
                    }
                    JOptionPane confirmDelete = new JOptionPane();
                    String[] options = {"Yes", "No"};
                    int selectedOption = confirmDelete.showOptionDialog(mainFrame, "Are you sure you want to delete booking for\n"
                                    + "MemberID: " + bookingDelete[1]
                                    + "\nDate: " + bookingDelete[0],
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[1]
                    );
                    if (selectedOption == 1)
                    {
                    }
                    else
                    {
                        int memberId = Integer.parseInt(infoTable.getValueAt(row, 1).toString());
                        int courtId = Integer.parseInt(infoTable.getValueAt(row, 3).toString());
                        String sportName = sportsClub.sportHasCourt(courtId);
                        LocalDate date = LocalDate.parse(infoTable.getValueAt(row, 0).toString());
                        LocalTime startTime = LocalTime.of(Integer.parseInt(infoTable.getValueAt(row, 4).toString().substring(0,2)),0);
                        LocalTime endTime = LocalTime.of(Integer.parseInt(infoTable.getValueAt(row, 5).toString().substring(0,2)),0);

                        // deletes the booking
                        sportsClub.removeBooking(memberId, courtId, sportName, date, startTime, endTime);

                        Object[][] data = getMemberData(memberId);
                        mainFrame.remove(tablePane);
                        createTablePane(data);
                        refreshGUI();
                    }
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    JOptionPane error = new JOptionPane();
                    error.showMessageDialog(mainFrame,"No Booking selected", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (MyException ex)
                {
                    errorDialog(ex.getMessage());
                }
            }
        });
    }

    /**
     * Creates and adds the listener with required functionality for exit button
     */
    public void exitButton()
    {
        saveExitButton = new JButton("Save and Exit");
        saveExitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
    }

    /**
     * Gets the data for the table based on the given memberId
     *
     * @param memberId the member id to display
     * @return Object[][] the array of data to display
     */
    public Object[][] getMemberData(int memberId) throws MyException
    {
        ArrayList<Booking> bookings = sportsClub.getMember(memberId).getBookings();

        Object[][] data = new String[bookings.size()][6];
        for (int i = 0; i < bookings.size(); i++)
        {
            data[i][0] = bookings.get(i).getDate().toString();
            data[i][1] = Integer.toString(bookings.get(i).getMemberId());
            data[i][2] = sportsClub.getMember(memberId).getName();
            data[i][3] = Integer.toString(bookings.get(i).getCourtId());
            data[i][4] = bookings.get(i).getStartTime().toString();
            data[i][5] = bookings.get(i).getEndTime().toString();
        }

        return data;
    }

    /**
     * Gets the data for the table based on the given courtId
     *
     * @param courtId the court id to display
     * @return Object[][] the array of data to display
     */
    public Object[][] getCourtData(int courtId) throws MyException
    {
        String sportName = sportsClub.sportHasCourt(courtId);
        ArrayList<Booking> bookings = sportsClub.getSport(sportName).getCourt(courtId).getCourtBookings();

        Object[][] data = new String[bookings.size()][6];
        for (int i = 0; i < bookings.size(); i++)
        {
            data[i][0] = bookings.get(i).getDate().toString();
            data[i][1] = Integer.toString(bookings.get(i).getMemberId());
            data[i][2] = sportsClub.getMember(bookings.get(i).getMemberId()).getName();
            data[i][3] = Integer.toString(bookings.get(i).getCourtId());
            data[i][4] = bookings.get(i).getStartTime().toString();
            data[i][5] = bookings.get(i).getEndTime().toString();
        }

        return data;
    }

    /**
     * Creates an error dialog with the passed in message
     *
     * @param message, the error message
     */
    public void errorDialog(String message)
    {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creates the table from the given data
     */
    public void createTablePane(Object[][] data)
    {
        infoTable = new JTable(new MyTableModel(data));
        infoTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        infoTable.setFillsViewportHeight(true);
        infoTable.setAutoCreateRowSorter(true);
        //Makes it sorted on date in descending order
        infoTable.getRowSorter().toggleSortOrder(0);
        infoTable.getRowSorter().toggleSortOrder(0);
        //Create the scroll pane and add the table to it.
        tablePane = new JScrollPane(infoTable);
        // adds table pane to main frame
        mainFrame.add(tablePane, BorderLayout.CENTER);
    }

    /**
     * Refreshes the GUI showing updated data
     */
    public void refreshGUI()
    {
        mainFrame.repaint();
        mainFrame.revalidate();
    }

    /**
     * Exits the applications first prompting the user if the do actually want to exit
     */
    public void exit()
    {
        String ObjButtons[] = {"Exit", "Cancel"};
        int result = JOptionPane.showOptionDialog(mainFrame, "Are you sure you want to exit?", "Exit",
                JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
        // if yes selected
        if (result == 0)
        {
            try
            {
//                sportsClub.writeBookingsToFile();
            }
            catch(Exception ex)
            {
                errorDialog(ex.getMessage());
            }
            // exits program
            System.exit(0);
        }
    }
}


