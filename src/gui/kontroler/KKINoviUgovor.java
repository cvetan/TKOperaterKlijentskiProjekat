/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.kontroler;

import domen.Korisnik;
import domen.Tarifa;
import domen.Ugovor;
import domen.Usluga;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

/**
 *
 * @author cvetan
 */
public class KKINoviUgovor {

    public static void vratiKorisnike(JComboBox jcbKorisnici) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_KORISNIKE);
            toZahtev.setParametar(new Korisnik());
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                List<Korisnik> lista = (List<Korisnik>) toOdgovor.getOdgovor();
                for (Korisnik k : lista) {
                    jcbKorisnici.addItem(k);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void vratiUsluge(JComboBox jcbUsluge) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_USLUGE);
            toZahtev.setParametar(new Usluga());
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                List<Usluga> lista = (List<Usluga>) toOdgovor.getOdgovor();
                for (Usluga u : lista) {
                    jcbUsluge.addItem(u);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void vratiTarife(JComboBox jcbUsluge, JComboBox jcbTarife) {
        jcbTarife.removeAllItems();
        Usluga usluga = (Usluga) jcbUsluge.getSelectedItem();
        Tarifa tarifa = new Tarifa(usluga);
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_TARIFE_PO_USLUZI);
            toZahtev.setParametar(tarifa);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                List<Tarifa> lista = (List<Tarifa>) toOdgovor.getOdgovor();
                for (Tarifa t : lista) {
                    jcbTarife.addItem(t);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void vratiBrojUgovora(JTextField jtfBrojUgovora) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_POSLEDNJI_UGOVOR);
            DateFormat df = new SimpleDateFormat("yyyy");
            Date datum = new Date();
            String stringd = df.format(datum);
            Date godina = df.parse(stringd);
            Ugovor ugovor = new Ugovor(godina);
            toZahtev.setParametar(ugovor);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                Ugovor u = (Ugovor) toOdgovor.getOdgovor();
                int brojUgovora = u.getBrojUgovora() + 1;
                jtfBrojUgovora.setText(String.valueOf(brojUgovora));
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void vratiDatum(JTextField jtfGodina, JTextField jtfDatum) {
        DateFormat godina = new SimpleDateFormat("yyyy");
        DateFormat datum = new SimpleDateFormat("dd.MM.yyyy");
        jtfGodina.setText(godina.format(new Date()));
        jtfDatum.setText(datum.format(new Date()));
    }

    public static void sacuvajUgovor(JComboBox jcbKorisnici, JTextField jtfBrojUgovora, JTextField jtfGodina, JTextField jtfDatumPotpisivanja, ButtonGroup jbgUgovornaObaveza, JComboBox jcbTarifa) {
        try {
            Korisnik k = (Korisnik) jcbKorisnici.getSelectedItem();
            Tarifa t = (Tarifa) jcbTarifa.getSelectedItem();
            int brojUgovora = Integer.parseInt(jtfBrojUgovora.getText().trim());
            Date godina = vratiGodinu(jtfGodina);
            Date datumPotpisivanja = vratiDatum(jtfDatumPotpisivanja);
            int ugovornaObaveza = vratiVrednostRadioButtona(jbgUgovornaObaveza);
            Ugovor u = new Ugovor(brojUgovora, godina, datumPotpisivanja, ugovornaObaveza, k, t);
            try {
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
                toZahtev.setOperacija(Konstante.SACUVAJ_UGOVOR);
                toZahtev.setParametar(u);
                Komunikacija.vratiInstancu().posalji(toZahtev);
                toOdgovor = Komunikacija.vratiInstancu().procitaj();
                if(toOdgovor.isUspesan()) {
                    JOptionPane.showMessageDialog(null, toOdgovor.getRezultat(), "Novi ugovor", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new RuntimeException(toOdgovor.getRezultat());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Novi ugovor", JOptionPane.WARNING_MESSAGE);
        }

    }

    public static int vratiVrednostRadioButtona(ButtonGroup jbgUgovornaObaveza) {
        int broj = 0;
        if (jbgUgovornaObaveza.getSelection() == null) {
            throw new RuntimeException("Niste označili vrednost za ugovornu obavezu!");
        } else {
            for (Enumeration<AbstractButton> dugmad = jbgUgovornaObaveza.getElements(); dugmad.hasMoreElements();) {
                AbstractButton dugme = dugmad.nextElement();
                if (dugme.isSelected()) {
                    String vrednost = dugme.getText().trim();
                    switch (vrednost) {
                        case ("6 meseci"):
                            broj = 6;
                            break;
                        case ("12 meseci"):
                            broj = 12;
                            break;
                        case ("24 meseca"):
                            broj = 24;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return broj;
    }

    public static Date vratiGodinu(JTextField jtfGodina) throws RuntimeException {
        DateFormat df = new SimpleDateFormat("yyyy");
        String tekst = jtfGodina.getText().trim();
        Date godina = new Date();
        try {
            godina = df.parse(tekst);
            return godina;
        } catch (Exception e) {
            throw new RuntimeException("Neispravna vrednost za godinu!");
        }
    }

    public static Date vratiDatum(JTextField jtfDatumPotpisivanja) throws RuntimeException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String tekst = jtfDatumPotpisivanja.getText().trim();
        Date datum = new Date();
        try {
            datum = df.parse(tekst);
            return datum;
        } catch (Exception e) {
            throw new RuntimeException("Neispravna vrednost za datum!");
        }
    }
}
