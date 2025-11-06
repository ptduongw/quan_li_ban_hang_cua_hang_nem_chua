/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import controller.SalesController; 
import java.util.List;
import javax.swing.table.DefaultTableModel; 
import model.Product;
import java.sql.SQLException; 
import javax.swing.ListSelectionModel; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList; 
import model.Customer;   
import model.OrderDetail; 
import javax.swing.JOptionPane; 

public class SalesView extends javax.swing.JFrame {
    private SalesController salesController;
    
    private List<OrderDetail> cart = new ArrayList<>();

    private Customer currentCustomer = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SalesView.class.getName());

    public SalesView() {
        initComponents();
      
        this.salesController = new SalesController();
        
        setupTables();
        
        loadProductsTable();
        
        addProductsTableClickListener();
        
        addBillTableListener();
    }
    
    private void setupTables() {
        //Thiet lap bang product
        DefaultTableModel productModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productModel.addColumn("ID");
        productModel.addColumn("Ten san pham");
        productModel.addColumn("Don vi");
        productModel.addColumn("Gia ban");
        productModel.addColumn("Ton kho");
        productsTable.setModel(productModel); 
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Bang hoa don
        DefaultTableModel billModel = new DefaultTableModel() {
             @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
        };
        billModel.addColumn("ID San pham");
        billModel.addColumn("Ten san pham");
        billModel.addColumn("So luong");
        billModel.addColumn("Don gia");
        billModel.addColumn("Thanh tien");
        billTable.setModel(billModel); 
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void loadProductsTable() {
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.setRowCount(0); 
        List<Product> products = salesController.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getUnit(),
                p.getPrice(),
                p.getQuantity() //Ton kho
            });
        }
    }
    
    private void addProductsTableClickListener() {
        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int selectedRow = productsTable.getSelectedRow();
                    if (selectedRow == -1) return;
                    int productId = (int) productsTable.getValueAt(selectedRow, 0);
                    Product productToAdd = salesController.getProductById(productId);
                    if (productToAdd == null) {
                         JOptionPane.showMessageDialog(SalesView.this, "Khong tim thay san pham!", "Loi", JOptionPane.ERROR_MESSAGE);
                         return;
                    }
                    addProductToCart(productToAdd);
                }
            }
        });
    }

    private void addBillTableListener() {
        billTable.getModel().addTableModelListener(e -> {
            if (e.getType() == 0) { 
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 2) { 
                    try {
                        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
                        int newQuantity = Integer.parseInt(model.getValueAt(row, 2).toString());
                        double salePrice = (double) model.getValueAt(row, 3);
                        int productId = (int) model.getValueAt(row, 0);
                        Product product = salesController.getProductById(productId);
                        if (newQuantity > product.getQuantity()) {
                             JOptionPane.showMessageDialog(this, "So luong ton kho khong du! (Con lai: " + product.getQuantity() + ")", "Loi Ton Kho", JOptionPane.WARNING_MESSAGE);
                             model.setValueAt(cart.get(row).getQuantity(), row, 2); //Tra lai so luong cu
                             return;
                        }
                        if (newQuantity <= 0) {
                            cart.remove(row);
                            model.removeRow(row);
                        } else {
                            double newLineTotal = newQuantity * salePrice;
                            model.setValueAt(newLineTotal, row, 4); //Cap nhat cot thanh tien
                            cart.get(row).setQuantity(newQuantity);
                        }
                        updateTotalAmount(); 
                    } catch (Exception ex) {
                         System.err.println("Loi cap nhat bang hoa don: " + ex.getMessage());
                         DefaultTableModel model = (DefaultTableModel) billTable.getModel();
                         model.setValueAt(cart.get(row).getQuantity(), row, 2); 
                    }
                }
            }
        });
    }

    private void addProductToCart(Product product) {
        DefaultTableModel billModel = (DefaultTableModel) billTable.getModel();
        for (int i = 0; i < cart.size(); i++) {
            OrderDetail detail = cart.get(i);
            if (detail.getProductId() == product.getId()) {
                int newQuantity = detail.getQuantity() + 1;
                if (newQuantity > product.getQuantity()) {
                     JOptionPane.showMessageDialog(this, "So luong ton kho khong du! (Con lai: " + product.getQuantity() + ")", "Loi Ton Kho", JOptionPane.WARNING_MESSAGE);
                     return;
                }
                detail.setQuantity(newQuantity);
                double thanhTien = newQuantity * detail.getSalePrice();
                billModel.setValueAt(newQuantity, i, 2); //Cap nhat cot So Luong
                billModel.setValueAt(thanhTien, i, 4); 
                updateTotalAmount(); 
                return;
            }
        }
        if (product.getQuantity() < 1) {
             JOptionPane.showMessageDialog(this, "San pham nay da het hang!", "Loi Ton Kho", JOptionPane.WARNING_MESSAGE);
             return;
        }
        int quantity = 1;
        double salePrice = product.getPrice();
        double lineTotal = quantity * salePrice;
        OrderDetail newDetail = new OrderDetail(0, product.getId(), quantity, salePrice);
        cart.add(newDetail);
        billModel.addRow(new Object[]{
            product.getId(),
            product.getName(),
            quantity,
            salePrice,
            lineTotal
        });
        updateTotalAmount(); 
    }

    private void updateTotalAmount() {
        double total = 0.0;
        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            try {
                total += (double) model.getValueAt(i, 4);
            } catch (Exception e) {
                 System.err.println("Loi khi tinh tong tien: " + e.getMessage());
            }
        }
        lblTotalAmount.setText(String.format("%,.0f VND", total));
    }


    private void clearBill() {
        cart.clear();
        DefaultTableModel billModel = (DefaultTableModel) billTable.getModel();
        billModel.setRowCount(0);
        txtCustomerPhone.setText("");
        txtCustomerName.setText("");
        currentCustomer = null;
        lblTotalAmount.setText("0 VND");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtSearchProduct = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billTable = new javax.swing.JTable();
        btnRemoveItem = new javax.swing.JButton();
        txtCustomerPhone = new javax.swing.JTextField();
        txtCustomerName = new javax.swing.JTextField();
        btnFindCustomer = new javax.swing.JButton();
        lblTotalAmount = new javax.swing.JLabel();
        btnPay = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtSearchProduct.setText("SẢN PHẨM");
        txtSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchProductActionPerformed(evt);
            }
        });

        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(productsTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        billTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(billTable);

        btnRemoveItem.setText("DELETE");
        btnRemoveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveItemActionPerformed(evt);
            }
        });

        txtCustomerPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerPhoneActionPerformed(evt);
            }
        });

        txtCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerNameActionPerformed(evt);
            }
        });

        btnFindCustomer.setText("FIND CUSTOMER");
        btnFindCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindCustomerActionPerformed(evt);
            }
        });

        lblTotalAmount.setText("Total");

        btnPay.setText("Pay");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        jLabel1.setText("SĐT");

        jLabel2.setText("Tên Khách Hàng");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTotalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCustomerName)
                                .addComponent(txtCustomerPhone)
                                .addComponent(btnPay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                            .addComponent(btnFindCustomer, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(btnRemoveItem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(2, 2, 2)
                        .addComponent(txtCustomerPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFindCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotalAmount)
                        .addGap(18, 18, 18)
                        .addComponent(btnPay)))
                .addGap(2, 2, 2)
                .addComponent(btnRemoveItem))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindCustomerActionPerformed
        // TODO add your handling code here:
        String phone = txtCustomerPhone.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap So Dien Thoai de tim!", "Thieu Thong Tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Customer foundCustomer = salesController.findCustomerByPhone(phone);
        if (foundCustomer != null) {
            txtCustomerName.setText(foundCustomer.getFullName());
            currentCustomer = foundCustomer;
            JOptionPane.showMessageDialog(this, "Da tim thay khach hang: " + foundCustomer.getFullName(), "Thong Bao", JOptionPane.INFORMATION_MESSAGE);
        } else {
            txtCustomerName.setText("Khach moi"); 
            currentCustomer = null;
            JOptionPane.showMessageDialog(this, "Khong tim thay khach hang. Day la khach moi.", "Thong Bao", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnFindCustomerActionPerformed

    private void btnRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveItemActionPerformed
        // TODO add your handling code here:
        int selectedRow = billTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui long chon mot san pham trong hoa don de xoa!", "Chua Chon Mon", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int productIdToRemove = (int) billTable.getValueAt(selectedRow, 0);
    for (int i = 0; i < cart.size(); i++) {
        if (cart.get(i).getProductId() == productIdToRemove) {
            cart.remove(i);
            break;
        }
    }
    DefaultTableModel model = (DefaultTableModel) billTable.getModel();
    model.removeRow(selectedRow);
    updateTotalAmount();
    System.out.println("Da xoa san pham ID " + productIdToRemove + " khoi gio hang.");
    }//GEN-LAST:event_btnRemoveItemActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        // TODO add your handling code here:
        if (cart.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Gio hang dang trong!", "Loi", JOptionPane.ERROR_MESSAGE);
        return;
    }
    String phone = txtCustomerPhone.getText().trim();
    String name = txtCustomerName.getText().trim();
    if (phone.isEmpty() || name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui long nhap day du So Dien Thoai va Ten Khach Hang!", "Thieu Thong Tin", JOptionPane.WARNING_MESSAGE);
        return;
    }
    Customer customerToPay;
    if (currentCustomer != null && currentCustomer.getPhone().equals(phone)) {
        customerToPay = currentCustomer;
    } else {
        customerToPay = new Customer(phone, name);
    }
    String totalText = lblTotalAmount.getText().replace(" VND", "").replace(",", "");
    double totalAmount = Double.parseDouble(totalText);
    int newOrderId = salesController.processPayment(customerToPay, cart, totalAmount);
    if (newOrderId != -1) {
        JOptionPane.showMessageDialog(this, "Thanh toan thanh cong! ID don hang moi la: " + newOrderId, "Thanh Cong", JOptionPane.INFORMATION_MESSAGE);
        clearBill();
        loadProductsTable(); // Cap nhat lai so luong ton kho
    } else {
        JOptionPane.showMessageDialog(this, "Thanh toan that bai! (Kiem tra ton kho hoac log).", "Loi", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnPayActionPerformed

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void txtSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchProductActionPerformed

    private void txtCustomerPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerPhoneActionPerformed
        // TODO add your handling code here:
        String phone = txtCustomerPhone.getText().trim();
        
        if (!Character.isDigit(phone) && phone != KeyEvent.VK_BACK_SPACE && phone != KeyEvent.VK_DELETE) {
            evt.consume(); // không cho ký tự này xuất hiện trong ô
    }
    }//GEN-LAST:event_txtCustomerPhoneActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new SalesView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable billTable;
    private javax.swing.JButton btnFindCustomer;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTotalAmount;
    private javax.swing.JTable productsTable;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtCustomerPhone;
    private javax.swing.JTextField txtSearchProduct;
    // End of variables declaration//GEN-END:variables
}
