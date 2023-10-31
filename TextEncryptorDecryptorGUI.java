import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TextEncryptorDecryptorGUI {

    private static final String ALGORITHM = "AES";

    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private String key;

    public TextEncryptorDecryptorGUI() {
        frame = new JFrame("Text Encryptor/Decryptor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputArea = new JTextArea(10, 40);
        outputArea = new JTextArea(10, 40);

        JMenuBar menuBar = new JMenuBar();
        JMenu functionMenu = new JMenu("Function");
        JMenuItem encryptItem = new JMenuItem("Encrypt Text");
        encryptItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String inputText = inputArea.getText();
                    String encryptedText = encrypt(inputText, key);
                    outputArea.setText(encryptedText);
                    inputArea.setText("key:"+key);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error encrypting text: " + ex.getMessage());
                }
            }
        });
        functionMenu.add(encryptItem);

        JMenuItem decryptItem = new JMenuItem("Decrypt Text");
        decryptItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String inputText = outputArea.getText();
                    String decryptedText = decrypt(inputText, key);
                    inputArea.setText(decryptedText);
                    outputArea.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error decrypting text: " + ex.getMessage());
                }
            }
        });
        functionMenu.add(decryptItem);

        menuBar.add(functionMenu);
        frame.setJMenuBar(menuBar);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(outputPanel, BorderLayout.EAST);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
		frame.setResizable(false);

        key = JOptionPane.showInputDialog(frame, "Enter secret key(16/32 Characters):");
    }

    public static String encrypt(String input, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return DatatypeConverter.printBase64Binary(encryptedBytes);
    }

    public static String decrypt(String input, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedBytes = DatatypeConverter.parseBase64Binary(input);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TextEncryptorDecryptorGUI();
            }
        });
    }
}
