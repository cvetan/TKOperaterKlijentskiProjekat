/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.kontroler;

import domen.Adresa;
import domen.Korisnik;
import domen.Mesto;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;
import validacija.ValidacijaKorisnik;

/**
 *
 * @author cvetan
 */
public class KKINoviKorisnik {

    public static void srediComboBox(JComboBox jcbMesto) {
        jcbMesto.removeAllItems();
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_MESTA);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            
            if(toOdgovor.isUspesan()) {
                List<Mesto> lista = (List<Mesto>) toOdgovor.getOdgovor();
                for(Mesto m: lista) {
                    jcbMesto.addItem(m);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void sacuvajKorisnika(JTextField jtfJmbg, JTextField jtfIme, JTextField jtfPrezime, JTextField jtfKorisnickoIme, JTextField jtfUlica, JTextField jtfBroj, JComboBox jcbMesto, JTextField jtfTelefon, JTextField jtfEmail, int operacija) {
        String jmbg = jtfJmbg.getText().trim();
        String ime = jtfIme.getText().trim();
        String prezime = jtfPrezime.getText().trim();
        String korisnickoIme = jtfKorisnickoIme.getText().trim();
        String ulica = jtfUlica.getText().trim();
        String broj = jtfBroj.getText().trim();
        String telefon = jtfTelefon.getText().trim();
        String email = jtfEmail.getText().trim();
        Mesto m = (Mesto) jcbMesto.getSelectedItem();
        
        String [] input = new String[] {jmbg, ime, prezime, korisnickoIme, ulica, broj, telefon, email};
        try {
            ValidacijaKorisnik.proveriSveInpute(input);
            ValidacijaKorisnik.proveriJmbg(jmbg);
            ValidacijaKorisnik.proveriKorisnickoIme(korisnickoIme);
            ValidacijaKorisnik.proveriTelefon(telefon);
            ValidacijaKorisnik.proveriEmail(email);
            
            Adresa a = new Adresa(ulica, broj, m);
            Korisnik k = new Korisnik(jmbg, ime, prezime, korisnickoIme, a, telefon, email);
            
            try {
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                TransferObjekatOdgovor toOdoOdgovor = new TransferObjekatOdgovor();
                
                toZahtev.setOperacija(operacija);
                toZahtev.setParametar(k);
                Komunikacija.vratiInstancu().posalji(toZahtev);
                toOdoOdgovor = Komunikacija.vratiInstancu().procitaj();
                
                if(toOdoOdgovor.isUspesan()) {
                    JOptionPane.showMessageDialog(null, toOdoOdgovor.getRezultat(), "", JOptionPane.INFORMATION_MESSAGE);
                    ocistiFormu(jtfJmbg, jtfIme, jtfPrezime, jtfKorisnickoIme, jtfUlica, jtfBroj, jcbMesto, jtfTelefon, jtfEmail);
                    omoguciIzmenuVrednostiPolja(jtfJmbg, jtfKorisnickoIme);
                } else {
                    throw new RuntimeException(toOdoOdgovor.getRezultat());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Neispravan unos!", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void postaviVrednostiKorisnikaNaFormu(JTextField jtfJmbg, JTextField jtfIme, JTextField jtfPrezime, JTextField jtfKorisnickoIme, JTextField jtfUlica, JTextField jtfBroj, JComboBox jcbMesto, JTextField jtfTelefon, JTextField jtfEmail, Korisnik korisnik) {
        jtfJmbg.setText(korisnik.getJmbg());
        jtfIme.setText(korisnik.getIme());
        jtfPrezime.setText(korisnik.getPrezime());
        jtfKorisnickoIme.setText(korisnik.getKorisnickoIme());
        jtfUlica.setText(korisnik.getAdresa().getUlica());
        jtfBroj.setText(korisnik.getAdresa().getBroj());
        jtfTelefon.setText(korisnik.getTelefon());
        jtfEmail.setText(korisnik.getEmail());
        
        for(int i=0; i< jcbMesto.getItemCount();i++) {
            Mesto m = (Mesto) jcbMesto.getItemAt(i);
            if(m.getPtt() == korisnik.getAdresa().getMesto().getPtt()) {
                jcbMesto.setSelectedIndex(i);
                break;
            }
        }
    }
    
    public static void onemoguciIzmenuVrednostiPolja(JTextField jtfJmbg, JTextField jtfKorisnickoIme) {
        jtfJmbg.setEditable(false);
        jtfKorisnickoIme.setEditable(false);
    }
    
    public static void omoguciIzmenuVrednostiPolja(JTextField jtfJmbg, JTextField jtfKorisnickoIme) {
        jtfJmbg.setEditable(true);
        jtfKorisnickoIme.setEditable(true);
    }
    
    public static void ocistiFormu(JTextField jtfJmbg, JTextField jtfIme, JTextField jtfPrezime, JTextField jtfKorisnickoIme, JTextField jtfUlica, JTextField jtfBroj, JComboBox jcbMesto, JTextField jtfTelefon, JTextField jtfEmail) {
        jtfJmbg.setText("");
        jtfIme.setText("");
        jtfPrezime.setText("");
        jtfKorisnickoIme.setText("");
        jcbMesto.setSelectedIndex(0);
        jtfUlica.setText("");
        jtfBroj.setText("");
        jtfTelefon.setText("");
        jtfEmail.setText("");
    }

}
