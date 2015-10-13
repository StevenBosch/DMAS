package civilviolence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author maleco
 */
public class GUIFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     *
     * @param grid
     * @param param
     */
    public GUIFrame(Cell[][] grid, final HashMap<String, Integer> param) {
        initComponents();

        gridButtons = new JButton[param.get("LENGTH")][param.get("WIDTH")];

        // Set the layouts
        GridPanel.setLayout(
                new GridLayout(
                        param.get("LENGTH"),
                        param.get("WIDTH")
                ));
        ControlFrame.setLayout(new GridLayout(1, 3));

        // Maximize the screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set the panel split
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jSplitPane1.setDividerLocation(screenSize.height);
        jSplitPane1.setEnabled(false);
        jSplitPane2.setDividerLocation(screenSize.height-GridPanel.getWidth()/4);
        jSplitPane1.setEnabled(false);

        // Add the grid buttons
        setGridButtons(grid, param);

    }

    private void setGridButtons(final Cell[][] grid, final HashMap<String, Integer> param) {
        // Add the grid buttons
        for (int row = 0; row < param.get("LENGTH"); ++row) {
            for (int col = 0; col < param.get("WIDTH"); ++col) {

                // Create the button
                JButton btn = new javax.swing.JButton();

                int nrHostiles = grid[row][col].getNrHostiles();
                int nrCops = grid[row][col].getAgents().size();

                // Set the background
                if (nrHostiles == 0) {
                    btn.setBackground(new Color(0, 0, 255));
                } else if (nrCops == 0 & nrHostiles != 0) {
                    btn.setBackground(new Color(255, 0, 0));
                } else {
                    // Range from -1 to 1
                    float temp = ((((float) (nrCops - nrHostiles) / (nrCops + nrHostiles)) + 1) / 2) * 255;
                    btn.setBackground(new Color(
                            (int) (255 - temp),
                            0,
                            (int) temp
                    ));
                }

                // Set the text as number of neutrals
                btn.setFont(new Font("Arial", Font.PLAIN, 12));
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setForeground(Color.WHITE);
                btn.setText("" + grid[row][col].getNrNeutral());

                // Final variables for the actionlistener
                final int finalRow = row;
                final int finalCol = col;
                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        // Adjust the information box on buttonclick
                        // btn.addActionListener((ActionEvent e) -> {
                        infoField.setText(
                                "Current Epoch:\t\t" + param.get("EPOCH") +
                                "\n\n\t Number of neutrals on this site:\t\t" + grid[finalRow][finalCol].getNrNeutral() + '\n'
                                + "\t Number of neutrals saved on this site:\t" + grid[finalRow][finalCol].getNrNeutralsSaved()+ '\n'
                                + "\t Number of hostiles on this site:\t\t" + grid[finalRow][finalCol].getNrHostiles() + '\n'
                                + "\t Number of cops on this site:\t\t" + grid[finalRow][finalCol].getAgents().size()
                                + "\n\n"
                                + "\t Total number of neutrals at start:\t\t" + param.get("TOTALNRNEUTRAL") + '\n'
                                + "\t Total number of hostiles at start:\t\t" + param.get("TOTALNRHOSTILES") + '\n'
                                + "\t Total number of cops at start:\t\t" + param.get("NRCOPS") + '\n'   
                                + "\n"
                                + "\t Saved number of neutrals:\t\t" + param.get("SAVEDNRNEUTRALS") + '\n'
                                + "\t Killed number of neutrals:\t\t\t" + (param.get("TOTALNRNEUTRAL") - param.get("SAVEDNRNEUTRALS")) + '\n'
                                + "\t Killed number of cops:\t\t\t" + (param.get("NRCOPS") - param.get("REMAININGNRCOPS")) + '\n'
                                + "\t Killed number of hostiles:\t\t\t" + (param.get("TOTALNRHOSTILES") - param.get("REMAININGNRHOSTILES")) + '\n'
                                + "\n"
                                + "\t Remaining number of neutrals:\t\t" + param.get("REMAININGNRNEUTRALS") + '\n'
                                + "\t Remaining number of hostiles:\t\t" + param.get("REMAININGNRHOSTILES") + '\n'
                                + "\t Remaining number of cops :\t\t"    + param.get("REMAININGNRCOPS") + '\n'
                                + "\n"
                                + "\t Total success for this simulation:\t" + new DecimalFormat("##.###").format((double) param.get("LASTSUCCESS1")/(double) param.get("LASTSUCCESS2"))
                        );
                    }
                });
                // Add the button to the group and the panel
                gridButtons[row][col] = btn;
                GridPanel.add(btn);
            }
        }
    }

    public void updateGridButtons(final Cell[][] grid, final HashMap<String, Integer> param) {
        // Add the grid buttons
        for (int row = 0; row < param.get("LENGTH"); ++row) {
            for (int col = 0; col < param.get("WIDTH"); ++col) {
                
                int finalRow = row;
                int finalCol = col;
                int nrNeutral = grid[row][col].getNrNeutral();
                int nrHostiles = grid[row][col].getNrHostiles();
                int nrCops = grid[row][col].getAgents().size();

                // Set the background
                if (nrHostiles == 0) {
                    gridButtons[row][col].setBackground(new Color(0, 0, 255));
                } else if (nrCops == 0 & nrHostiles != 0) {
                    gridButtons[row][col].setBackground(new Color(255, 0, 0));
                } else {
                    // Range from -1 to 1
                    float temp = ((((float) (nrCops - nrHostiles) / (nrCops + nrHostiles)) + 1) / 2) * 255;
                    gridButtons[row][col].setBackground(new Color(
                            (int) (255 - temp),
                            0,
                            (int) temp
                    ));
                }
                // Set the text of the button (No of neutrals)
                gridButtons[row][col].setText("" + grid[finalRow][finalCol].getNrNeutral());
            }

        }
    }

    public void clickAButton() {
        gridButtons[0][0].doClick();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        GridPanel = new javax.swing.JPanel();
        RightPanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        infoField = new javax.swing.JTextPane();
        ControlFrame = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1024, 768));

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(25);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(600, 400));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(600, 400));

        GridPanel.setMinimumSize(new java.awt.Dimension(400, 400));
        GridPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        GridPanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout GridPanelLayout = new javax.swing.GroupLayout(GridPanel);
        GridPanel.setLayout(GridPanelLayout);
        GridPanelLayout.setHorizontalGroup(
            GridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        GridPanelLayout.setVerticalGroup(
            GridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(GridPanel);

        RightPanel.setLayout(new java.awt.GridLayout(0, 1));

        jSplitPane2.setDividerLocation(100);
        jSplitPane2.setDividerSize(1);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        infoField.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        jSplitPane2.setLeftComponent(infoField);

        ControlFrame.setPreferredSize(new java.awt.Dimension(173, 100));

        javax.swing.GroupLayout ControlFrameLayout = new javax.swing.GroupLayout(ControlFrame);
        ControlFrame.setLayout(ControlFrameLayout);
        ControlFrameLayout.setHorizontalGroup(
            ControlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        ControlFrameLayout.setVerticalGroup(
            ControlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );

        jSplitPane2.setBottomComponent(ControlFrame);

        RightPanel.add(jSplitPane2);

        jSplitPane1.setRightComponent(RightPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel ControlFrame;
    public javax.swing.JPanel GridPanel;
    public javax.swing.JPanel RightPanel;
    public javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JTextPane infoField;
    public javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    // End of variables declaration//GEN-END:variables

    // A grid to store the buttons
    public JButton[][] gridButtons;
}
