/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validacija;

import domen.Korisnik;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cvetan
 */
public class ValidacijaKorisnik {

    public static void proveriSveInpute(String [] input) throws RuntimeException {
        for (String s : input) {
            if (s.isEmpty()) {
                throw new RuntimeException("Sva polja su obavezna!");
            }
        }
    }

    public static void proveriJmbg(String jmbg) throws RuntimeException {
        /*
        
         Maticni broj se sastoji od 13 cifara u obliku "DD MM GGG RR BBB K"
         DD - dan rodjenja
         MM - mesec rodjenja
         GGG - godina rodjenja
         RR - Region
         BBB - jedinstveni broj
         K - kontrolni broj koji se izracunava po formuli
        
         Kontrolni broj se izracunava formulom gde je DDMMGGGRRBBBK = ABVGDĐEŽZIJKL
        
         L = 11 - (( 7*(A+E) + 6*(B+Ž) + 5*(V+Z) + 4*(G+I) + 3*(D+J) + 2*(Đ+K) ) % 11)
        
         ako je L izmedju 1i 9 K jednako tom broju L = K,
         ako je L vece od 9 postaje 0
        
         */
        char[] karakteri = jmbg.toCharArray();
        int[] cifre = new int[jmbg.length()];

//        Konverzija stringa u niz cifara
        if (jmbg.length() != 13) {
            throw new RuntimeException("Matični broj se sastoji od 13 cifara! \n Proverite unos!");
        } else {
            for (int i = 0; i < karakteri.length; i++) {
                if (Character.isDigit(karakteri[i])) {
                    cifre[i] = Integer.parseInt(Character.toString(karakteri[i]));
                } else {
                    throw new RuntimeException("Matični broj se sastoji od 13 cifara! \n Proverite unos!");
                }
            }

        }

//        Parsiranje i provera datuma rodjenja
        DateFormat df = new SimpleDateFormat("ddMMyy");
        try {
            String datumString = jmbg.substring(0, 4) + jmbg.substring(5, 7);
            Date datum = df.parse(datumString);
        } catch (ParseException e) {
            throw new RuntimeException("Matični broj nije validan!");
        }

//        Racunanje i provera kontrolnog broja
        int unet = cifre[12];

        int a = cifre[0];
        int b = cifre[1];
        int c = cifre[2];
        int d = cifre[3];
        int e = cifre[4];
        int f = cifre[5];
        int g = cifre[6];
        int h = cifre[7];
        int i = cifre[8];
        int j = cifre[9];
        int k = cifre[10];
        int l = cifre[11];
        int m = cifre[12];

        int izracunat = 11 - ((7 * (a + g) + 6 * (b + h) + 5 * (c + i) + 4 * (d + j) + 3 * (e + k) + 2 * (f + l)) % 11);
        if (!(izracunat >= 1 && izracunat <= 9)) {
            izracunat = 0;
        }

        if (unet != izracunat) {
            throw new RuntimeException("Matični broj nije validan!");
        }
    }

    public static void proveriEmail(String email) throws RuntimeException {
        String patern = "[a-z][^,;:\"]*[a-z0-9]@[a-z0-9]+\\.[a-z]{2,}";
        if (!email.matches(patern)) {
            throw new RuntimeException("Email nije validan!");
        }
    }

    public static void proveriKorisnickoIme(String korisnickoIme) throws RuntimeException {
        if (korisnickoIme.length() < 3) {
            throw new RuntimeException("Korisničko ime mora da ima bar 3 karaktera!");
        }
    }

    public static void proveriTelefon(String telefon) throws RuntimeException {
        String patern = "0[1-9]{2}/[0-9]{3}-[0-9]{3,4}";
        if (!telefon.matches(patern)) {
            throw new RuntimeException("Telefon nije validan! \n Telefon mora da bude u formatu 0../...-...");
        }
    }
}
