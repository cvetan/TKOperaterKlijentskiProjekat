/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.kontroler;

import domen.Radnik;
import gui.FrmGlavnaForma;
import gui.FrmLoginForma;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

/**
 *
 * @author cvetan
 */
public class KKILoginForma {
    
    public static void proveriStatusServera() {
        try {
            Komunikacija.vratiInstancu();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void login(JTextField jtfKorisnickoIme, JPasswordField jpfSifra, FrmLoginForma loginForma) {
        String korisnickoIme = jtfKorisnickoIme.getText().trim();
        String sifra = jpfSifra.getText();
        if(korisnickoIme.isEmpty() || sifra.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Niste uneli podatke za login!", "Prijava na sistem", JOptionPane.WARNING_MESSAGE);
        } else {
            Radnik r = new Radnik(korisnickoIme, sifra);
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            try {
                toZahtev.setOperacija(Konstante.LOGIN);
                toZahtev.setParametar(r);
                Komunikacija.vratiInstancu().posalji(toZahtev);
                toOdgovor = Komunikacija.vratiInstancu().procitaj();
                
                if(toOdgovor.isUspesan()) {
                    JOptionPane.showMessageDialog(null, "Uspešno ste se prijavili na sistem.", "Prijava na sistem", JOptionPane.INFORMATION_MESSAGE);
                    FrmGlavnaForma gf = new FrmGlavnaForma();
                    gf.setLocationRelativeTo(null);
                    gf.setVisible(true);
                    loginForma.dispose();
                } else {
                    throw new RuntimeException(toOdgovor.getRezultat());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(loginForma, e.getMessage(), "Neuspešna prijava!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
